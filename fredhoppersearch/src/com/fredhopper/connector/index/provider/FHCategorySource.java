/*******************************************************************************
 * Copyright 2016 Fredhopper B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.fredhopper.connector.index.provider;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.variants.model.VariantProductModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.fredhopper.connector.index.dao.FindCatalogVersionStrategy;


/**
 * Utility class to identify and collect relevant categories to export
 */
public class FhCategorySource
{

	private static final Logger LOG = Logger.getLogger(CategoryCodeProvider.class);
	private CategoryService categoryService;
	private ModelService modelService;

	private FindCatalogVersionStrategy findCatalogVersionStrategy;
	private boolean includeClassificationClasses;

	private String categoriesQualifier;
	private Set<String> rootCategoryCodes;


	public Collection<CategoryModel> getRootCategories()
	{
		final Set<CategoryModel> rootCategories = new HashSet<>();
		for (final String rootCategoryCode : rootCategoryCodes)
		{
			rootCategories
					.add(categoryService.getCategoryForCode(getFindCatalogVersionStrategy().findCatalogVersion(), rootCategoryCode));
		}
		return rootCategories;
	}


	public Collection<CategoryModel> findCategoriesForProduct(final ProductModel product)
	{
		final Set<ProductModel> products = findProductFamily(product);
		final Set<CategoryModel> categories = getDirectSuperCategories(products);

		if (categories != null && !categories.isEmpty())
		{
			final Set<CategoryModel> rootCategories = lookupRootCategories(product.getCatalogVersion());

			final Set<CategoryModel> allCategories = new HashSet<>();
			for (final CategoryModel category : categories)
			{
				allCategories.addAll(getAllCategories(category, rootCategories));
			}
			return allCategories;
		}
		else
		{
			return Collections.emptyList();
		}
	}

	protected Set<ProductModel> findProductFamily(final ProductModel product)
	{
		if (product instanceof VariantProductModel)
		{
			// Collect all the variant products and all their super variants, until the final base product is hit
			final Set<ProductModel> products = new HashSet<>();

			ProductModel currentProduct = product;
			while (currentProduct instanceof VariantProductModel)
			{
				products.add(currentProduct);
				currentProduct = ((VariantProductModel) currentProduct).getBaseProduct();
			}

			products.add(currentProduct);
			return products;
		}
		else if (product != null)
		{
			return Collections.singleton(product);
		}
		return Collections.emptySet();
	}

	protected Set<CategoryModel> getDirectSuperCategories(final Set<ProductModel> products)
	{
		final Set<CategoryModel> categories = new HashSet<>();

		for (final ProductModel product : products)
		{
			final Collection<CategoryModel> directCategories = getModelService().getAttributeValue(product,
					getCategoriesQualifier());
			if (directCategories != null && !directCategories.isEmpty())
			{
				categories.addAll(directCategories);
			}
		}

		return categories;
	}

	protected Collection<CategoryModel> getAllCategories(final CategoryModel directCategory,
			final Set<CategoryModel> rootCategories)
	{
		if (isBlockedCategory(directCategory))
		{
			// This category is blocked - ignore it and all super categories
			return Collections.emptyList();
		}

		if (CollectionUtils.isNotEmpty(rootCategories))
		{
			// We have root categories - use collect
			return collectSuperCategories(directCategory, rootCategories, new HashSet<CategoryModel>(3));
		}
		else
		{
			// Traverse all the super-categories
			final Collection<CategoryModel> categories = new ArrayList<>();
			categories.add(directCategory);
			for (final CategoryModel superCategory : directCategory.getAllSupercategories())
			{
				if (!isBlockedCategory(superCategory))
				{
					categories.add(superCategory);
				}
			}
			return categories;
		}
	}

	protected boolean isBlockedCategory(final CategoryModel category)
	{
		return category instanceof ClassificationClassModel && !isIncludeClassificationClasses();
	}

	protected Set<CategoryModel> collectSuperCategories(final CategoryModel category, final Set<CategoryModel> rootCategories,
			final Set<CategoryModel> path)
	{
		if (category == null || isBlockedCategory(category))
		{
			// If this category is blocked or null then return empty set as this whole branch is not viable
			return Collections.emptySet();
		}

		if (path.contains(category))
		{
			// Loop detected, category has already been seen. this whole branch is not viable
			return Collections.emptySet();
		}

		// This category is ok, so add it to our path
		path.add(category);

		if (rootCategories.contains(category))
		{
			// We have found the root, so that is the end of this path
			return path;
		}
		else
		{
			final List<CategoryModel> superCategories = category.getSupercategories();
			if (superCategories == null || superCategories.isEmpty())
			{
				// No super categories, and we haven't found the root yet, so this whole branch is not viable
				return Collections.emptySet();
			}

			if (superCategories.size() == 1)
			{
				// Optimization for 1 super-category we can reuse our 'path' set
				return collectSuperCategories(superCategories.iterator().next(), rootCategories, path);
			}
			else
			{
				final HashSet<CategoryModel> result = new HashSet<>();

				for (final CategoryModel superCategory : superCategories)
				{
					if (!isBlockedCategory(superCategory))
					{
						// Collect the super category branch for each super-category with a copy of the path so far
						// Combine together the results
						result.addAll(collectSuperCategories(superCategory, rootCategories, new HashSet<CategoryModel>(path)));
					}
				}

				return result;
			}
		}
	}

	protected Set<CategoryModel> lookupRootCategories(final CatalogVersionModel catalogVersion)
	{
		final Set<String> categoryCodes = getRootCategoryCodes();
		if (categoryCodes != null && !categoryCodes.isEmpty())
		{
			final Set<CategoryModel> result = new HashSet<>();

			for (final String categoryCode : categoryCodes)
			{
				try
				{
					result.add(getCategoryService().getCategoryForCode(catalogVersion, categoryCode));
				}
				catch (final UnknownIdentifierException ex)
				{
					LOG.warn("Failed to load category [" + categoryCode + "] from catalog version ["
							+ catalogVersionToString(catalogVersion) + "]", ex);
				}
			}
			if (result.isEmpty())
			{
				LOG.error("Failed to find any Category with code [" + categoryCodes + "] in catalog version ["
						+ catalogVersionToString(catalogVersion) + "]");
			}
			else
			{
				return result;
			}
		}

		return Collections.emptySet();
	}

	protected String catalogVersionToString(final CatalogVersionModel catalogVersion)
	{
		if (catalogVersion == null)
		{
			return "<null>";
		}
		else if (catalogVersion.getCatalog() == null)
		{
			return "<null>:" + catalogVersion.getVersion();
		}
		return catalogVersion.getCatalog().getId() + ":" + catalogVersion.getVersion();
	}

	public CategoryService getCategoryService()
	{
		return categoryService;
	}

	@Required
	public void setCategoryService(final CategoryService categoryService)
	{
		this.categoryService = categoryService;
	}

	public ModelService getModelService()
	{
		return modelService;
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	public FindCatalogVersionStrategy getFindCatalogVersionStrategy()
	{
		return findCatalogVersionStrategy;
	}

	@Required
	public void setFindCatalogVersionStrategy(final FindCatalogVersionStrategy findCatalogVersionStrategy)
	{
		this.findCatalogVersionStrategy = findCatalogVersionStrategy;
	}

	public boolean isIncludeClassificationClasses()
	{
		return includeClassificationClasses;
	}

	public void setIncludeClassificationClasses(final boolean includeClassificationClasses)
	{
		this.includeClassificationClasses = includeClassificationClasses;
	}

	public String getCategoriesQualifier()
	{
		return categoriesQualifier;
	}

	public void setCategoriesQualifier(final String categoriesQualifier)
	{
		this.categoriesQualifier = categoriesQualifier;
	}

	public Set<String> getRootCategoryCodes()
	{
		return rootCategoryCodes;
	}

	public void setRootCategoryCodes(final Set<String> rootCategoryCodes)
	{
		this.rootCategoryCodes = rootCategoryCodes;
	}

}

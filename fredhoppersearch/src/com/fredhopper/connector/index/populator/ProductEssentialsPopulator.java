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
package com.fredhopper.connector.index.populator;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;

import com.fredhopper.connector.config.data.IndexConfig;
import com.fredhopper.connector.index.converter.ItemToConvert;
import com.fredhopper.connector.index.filter.CategoryFilterStrategy;
import com.fredhopper.connector.index.provider.FHCategorySource;
import com.fredhopper.core.connector.index.generate.data.FhProductData;
import com.fredhopper.core.connector.index.generate.validator.SanitizeIdStrategy;
import com.google.common.base.Preconditions;


/**
 *
 */
public class ProductEssentialsPopulator implements Populator<ItemToConvert<ProductModel>, FhProductData>
{

	private CategoryFilterStrategy categoryFilterStrategy;
	private FHCategorySource categorySource;
	private SanitizeIdStrategy sanitizeIdStrategy;


	@Override
	public void populate(final ItemToConvert<ProductModel> source, final FhProductData target) throws ConversionException
	{
		Preconditions.checkArgument(source != null);
		Preconditions.checkArgument(target != null);

		final IndexConfig indexConfig = source.getIndexConfig();
		final ProductModel product = source.getItem();

		Preconditions.checkArgument(indexConfig != null);
		Preconditions.checkArgument(CollectionUtils.isNotEmpty(indexConfig.getLocales()));

		target.setProductId(getSanitizeIdStrategy().sanitizeId(product.getCode()));

		final Collection<CategoryModel> input = categoryFilterStrategy.filterCategories(product.getSupercategories(),
				getCategorySource().getCategories());

		if (CollectionUtils.isNotEmpty(input))
		{
			final Set<String> categoryIds = new HashSet<>();

			input.forEach(category -> categoryIds.add(getSanitizeIdStrategy().sanitizeId(category.getCode())));

			final HashMap<Locale, Set<String>> output = new HashMap<>();
			indexConfig.getLocales().forEach(locale -> output.put(locale, categoryIds));

			target.setCategories(output);
		}
		else
		{
			throw new ConversionException("Product: " + product.getCode() + " does not belong to any categories.");
		}
	}

	public CategoryFilterStrategy getCategoryFilterStrategy()
	{
		return categoryFilterStrategy;
	}

	public void setCategoryFilterStrategy(final CategoryFilterStrategy categoryFilterStrategy)
	{
		this.categoryFilterStrategy = categoryFilterStrategy;
	}

	public FHCategorySource getCategorySource()
	{
		return categorySource;
	}

	public void setCategorySource(final FHCategorySource categorySource)
	{
		this.categorySource = categorySource;
	}

	public SanitizeIdStrategy getSanitizeIdStrategy()
	{
		return sanitizeIdStrategy;
	}

	public void setSanitizeIdStrategy(final SanitizeIdStrategy sanitizeIdStrategy)
	{
		this.sanitizeIdStrategy = sanitizeIdStrategy;
	}

}

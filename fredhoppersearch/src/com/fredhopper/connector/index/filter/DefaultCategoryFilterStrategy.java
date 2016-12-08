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
package com.fredhopper.connector.index.filter;



import de.hybris.platform.category.model.CategoryModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.util.Assert;

import com.fredhopper.connector.index.dao.impl.FindProductCatalogVersionStrategy;


/**
 * Default implementation of {@link CategoryFilterStrategy}
 */
public class DefaultCategoryFilterStrategy implements CategoryFilterStrategy
{

	private FindProductCatalogVersionStrategy findCatalogVersionStrategy;

	/**
	 *
	 * @return list of categoryModels without classificationClassModels
	 */
	@Override
	public List<CategoryModel> filterCategories(final Collection<CategoryModel> categories, final Collection<CategoryModel> roots)
	{
		final List<CategoryModel> onlyCategories = new ArrayList<>();
		Assert.notNull(categories, "categories must not be null");
		for (final CategoryModel category : categories)
		{
			if (category.getCatalogVersion().getCatalog().getId().equals(findCatalogVersionStrategy.getProductCatalogId())
					&& category.getCatalogVersion().getVersion().equals(findCatalogVersionStrategy.getProductCatalogVersionId()))
			{
				for (final CategoryModel root : roots)
				{
					if (category.getAllSupercategories().contains(root))
					{
						onlyCategories.add(category);
					}
				}
			}
		}
		return onlyCategories;
	}

	public FindProductCatalogVersionStrategy getFindCatalogVersionStrategy()
	{
		return findCatalogVersionStrategy;
	}

	public void setFindCatalogVersionStrategy(final FindProductCatalogVersionStrategy findCatalogVersionStrategy)
	{
		this.findCatalogVersionStrategy = findCatalogVersionStrategy;
	}


}

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
package com.fredhopper.connector.template.index.dao.impl;

import de.hybris.platform.category.model.CategoryModel;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.fredhopper.connector.index.dao.FhCategoryDao;


/**
 *
 */
public class DefaultFhCategoryDao implements FhCategoryDao
{

	@Override
	public Collection<CategoryModel> getCategories(final CategoryModel rootCategory)
	{

		return buildCategoriesCollection(rootCategory);
	}


	private Collection<CategoryModel> buildCategoriesCollection(final CategoryModel source)
	{
		final Set<CategoryModel> categories = new HashSet<>();

		categories.add(source);
		for (final CategoryModel category : source.getCategories())
		{
			final Collection<CategoryModel> children = buildCategoriesCollection(category);
			categories.addAll(children);
		}
		return categories;
	}

}

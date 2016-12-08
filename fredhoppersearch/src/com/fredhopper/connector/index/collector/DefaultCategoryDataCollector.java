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
package com.fredhopper.connector.index.collector;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;

import com.fredhopper.connector.config.data.IndexConfig;
import com.fredhopper.connector.index.converter.ItemToConvert;
import com.fredhopper.connector.index.dao.FhCategoryDao;
import com.fredhopper.connector.index.provider.FhCategorySource;
import com.fredhopper.core.connector.index.generate.collector.CategoryDataCollector;
import com.fredhopper.core.connector.index.generate.data.FhCategoryData;



/**
 * Default implementation of {@link CategoryDataCollector}
 */
public class DefaultCategoryDataCollector extends AbstractConfigurableCollector implements CategoryDataCollector
{
	private String universe;
	private FhCategorySource categorySource;
	private Converter<ItemToConvert<CategoryModel>, FhCategoryData> converter;
	private FhCategoryDao fhCategoryDao;

	@Override
	public Iterator<FhCategoryData> iterator()
	{
		final IndexConfig indexConfig = getIndexConfig();
		final Set<FhCategoryData> categoryData = new HashSet<>();

		categoryData.add(buildTopLevelCategoryData());

		for (final CategoryModel rootCategory : getCategorySource().getRootCategories())
		{
			final Collection<CategoryModel> subCategory = fhCategoryDao.getCategories(rootCategory);
			for (final CategoryModel categoryToConvert : subCategory)
			{
				categoryData.add(buildCategoryData(categoryToConvert, converter, indexConfig));
			}
		}


		return categoryData.iterator();
	}

	protected FhCategoryData fixUniverseParent(final FhCategoryData categoryData)
	{
		if (categoryData.getParentId() == null)
		{
			categoryData.setParentId(universe);
		}
		return categoryData;
	}

	private FhCategoryData buildTopLevelCategoryData()
	{
		final FhCategoryData topCategory = new FhCategoryData();
		topCategory.setCategoryId(universe);
		topCategory.setParentId(universe);


		final HashMap<Locale, String> topCategoryNames = new HashMap<>();
		final Set<Locale> locales = getIndexConfig().getLocales();
		if (locales != null && !locales.isEmpty())
		{
			for (final Locale locale : locales)
			{
				topCategoryNames.put(locale, universe);
			}
		}

		topCategory.setNames(topCategoryNames);
		return topCategory;

	}

	private FhCategoryData buildCategoryData(final CategoryModel category,
			final Converter<ItemToConvert<CategoryModel>, FhCategoryData> converter, final IndexConfig indexConfig)
	{
		final ItemToConvert<CategoryModel> source = new ItemToConvert<>(category, indexConfig);
		return fixUniverseParent(converter.convert(source));
	}

	public FhCategorySource getCategorySource()
	{
		return categorySource;
	}

	public void setCategorySource(final FhCategorySource categorySource)
	{
		this.categorySource = categorySource;
	}

	public Converter<ItemToConvert<CategoryModel>, FhCategoryData> getConverter()
	{
		return converter;
	}

	public void setConverter(final Converter<ItemToConvert<CategoryModel>, FhCategoryData> converter)
	{
		this.converter = converter;
	}

	public String getUniverse()
	{
		return universe;
	}

	public void setUniverse(final String universe)
	{
		this.universe = universe;
	}

	public FhCategoryDao getFhCategoryDao()
	{
		return fhCategoryDao;
	}

	public void setFhCategoryDao(final FhCategoryDao fhCategoryDao)
	{
		this.fhCategoryDao = fhCategoryDao;
	}


}

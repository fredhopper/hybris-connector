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
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.HashMap;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Required;

import com.fredhopper.connector.index.converter.ItemToConvert;
import com.fredhopper.core.connector.index.generate.data.FhCategoryData;
import com.fredhopper.core.connector.index.generate.validator.SanitizeIdStrategy;
import com.google.common.base.Preconditions;


/**
 * Populator responsible for converting categories to FhCategoryData
 */
public class CategoryPopulator implements Populator<ItemToConvert<CategoryModel>, FhCategoryData>
{

	private ParentCategoryResolver parentCategoryResolver;

	private SanitizeIdStrategy sanitizeIdStrategy;

	@Override
	public void populate(final ItemToConvert<CategoryModel> source, final FhCategoryData target) throws ConversionException
	{
		Preconditions.checkArgument(source != null);
		Preconditions.checkArgument(target != null);

		final CategoryModel category = source.getItem();
		final Set<Locale> locales = source.getIndexConfig().getLocales();

		target.setCategoryId(getSanitizeIdStrategy().sanitizeId(category.getCode()));
		target.setNames(mapNames(category, locales));
		final Optional<CategoryModel> parent = parentCategoryResolver.resolve(category);
		if (parent.isPresent())
		{
			target.setParentId(getSanitizeIdStrategy().sanitizeId(parent.get().getCode()));
		}
	}

	private HashMap<Locale, String> mapNames(final CategoryModel category, final Set<Locale> locales)
	{
		final HashMap<Locale, String> namesMap = new HashMap<>();
		locales.forEach(locale -> namesMap.put(locale, category.getName(locale)));
		return namesMap;
	}

	public ParentCategoryResolver getParentCategoryResolver()
	{
		return parentCategoryResolver;
	}

	@Required
	public void setParentCategoryResolver(final ParentCategoryResolver parentCategoryResolver)
	{
		this.parentCategoryResolver = parentCategoryResolver;
	}

	public SanitizeIdStrategy getSanitizeIdStrategy()
	{
		return sanitizeIdStrategy;
	}

	@Required
	public void setSanitizeIdStrategy(final SanitizeIdStrategy sanitizeIdStrategy)
	{
		this.sanitizeIdStrategy = sanitizeIdStrategy;
	}

}

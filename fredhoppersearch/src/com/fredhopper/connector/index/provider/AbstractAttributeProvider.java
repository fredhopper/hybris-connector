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

import de.hybris.platform.core.model.product.ProductModel;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Locale;
import java.util.Optional;

import com.fredhopper.connector.config.data.MetaAttributeData;
import com.fredhopper.core.connector.index.generate.data.FhAttributeBaseType;
import com.fredhopper.core.connector.index.generate.data.FhAttributeData;
import com.fredhopper.core.connector.index.generate.validator.SanitizeIdStrategy;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;


/**
 *
 */
public abstract class AbstractAttributeProvider implements AttributeProvider
{
	private SanitizeIdStrategy sanitizeIdStrategy;

	@Override
	public Collection<FhAttributeData> getAttribute(final ProductModel product, final MetaAttributeData metaAttribute,
			final Collection<Locale> locales)
	{

		final HashBasedTable<Optional<String>, Optional<Locale>, String> values = getAttributeValue(locales, product,
				metaAttribute);

		if (values != null && !values.isEmpty())
		{
			final FhAttributeData attributeData = new FhAttributeData(metaAttribute.getBaseType());
			attributeData.setAttributeId(metaAttribute.getAttributeId());
			attributeData.setItemId(getSanitizeIdStrategy().sanitizeId(product.getCode()));
			attributeData.setValues(values);
			return Arrays.asList(attributeData);
		}
		return Collections.emptyList();
	}

	protected void addValues(final Table<Optional<String>, Optional<Locale>, String> table, final Locale locale,
			final Object values, final MetaAttributeData metaAttribute)
	{

		if (values != null)
		{
			final Optional<Locale> loc = locale != null ? Optional.of(locale) : Optional.empty();
			if (values instanceof Collection)
			{
				for (final Object value : (Collection) values)
				{
					final Optional<String> valueId = Optional.ofNullable(generateAttributeValueId(metaAttribute, value));
					table.put(valueId, loc, value.toString());
				}
			}
			else
			{
				final Optional<String> valueId = Optional.ofNullable(generateAttributeValueId(metaAttribute, values));
				table.put(valueId, loc, values.toString());
			}
		}
	}


	private String generateAttributeValueId(final MetaAttributeData metaAttribute, final Object val)
	{
		return requiresAttributeValueId(metaAttribute) ? getSanitizeIdStrategy().sanitizeIdWithNumber(val.toString()) : null;
	}

	protected abstract HashBasedTable<Optional<String>, Optional<Locale>, String> getAttributeValue(
			final Collection<Locale> locales, final ProductModel product, final MetaAttributeData metaAttribute);

	protected boolean requiresAttributeValueId(final MetaAttributeData metaAttribute)
	{
		final FhAttributeBaseType type = metaAttribute.getBaseType();
		if (type == FhAttributeBaseType.INT || type == FhAttributeBaseType.FLOAT || type == FhAttributeBaseType.TEXT
				|| type == FhAttributeBaseType.ASSET)
		{
			return false;
		}
		return true;
	}

	protected boolean isLocalizedAttribute(final MetaAttributeData metaAttribute)
	{
		final FhAttributeBaseType type = metaAttribute.getBaseType();
		if (type == FhAttributeBaseType.INT || type == FhAttributeBaseType.FLOAT || type == FhAttributeBaseType.TEXT)
		{
			return false;
		}
		return true;
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

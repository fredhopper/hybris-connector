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

import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.classification.ClassificationService;
import de.hybris.platform.classification.features.Feature;
import de.hybris.platform.classification.features.FeatureValue;
import de.hybris.platform.classification.features.LocalizedFeature;
import de.hybris.platform.core.model.product.ProductModel;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import com.fredhopper.connector.config.data.MetaAttributeData;
import com.fredhopper.core.connector.index.generate.data.FhAttributeBaseType;
import com.google.common.collect.HashBasedTable;


/**
 * Prototype {@link AttributeProvider} implementation for {@link ClassAttributeAssignmentModel}
 */
public class ClassificationAttributeProvider extends AbstractAttributeProvider
{
	private ClassAttributeAssignmentModel classAttributeAssignment;

	private ClassificationService classificationService;

	@Override
	protected HashBasedTable<Optional<String>, Optional<Locale>, String> getAttributeValue(final Collection<Locale> locales,
			final ProductModel product, final MetaAttributeData metaAttribute)
	{
		final Feature feature = classificationService.getFeature(product, classAttributeAssignment);
		final List<FeatureValue> featureValues = feature.getValues();

		final HashBasedTable<Optional<String>, Optional<Locale>, String> table = HashBasedTable.create();
		if (!isLocalizedAttribute(metaAttribute))
		{
			for (final FeatureValue value : featureValues)
			{
				final String tableValue = value.getValue().toString();
				table.put(Optional.empty(), Optional.empty(), tableValue);

			}
		}
		else
		{
			if (metaAttribute.getBaseType() == FhAttributeBaseType.ASSET)
			{
				if (feature instanceof LocalizedFeature)
				{
					final Map<Locale, List<FeatureValue>> valueMap = ((LocalizedFeature) feature).getValuesForAllLocales();
					for (final Locale loc : locales)
					{
						final List<FeatureValue> locValues = valueMap.get(loc);
						for (final FeatureValue value : locValues)
						{
							final String tableValue = value.getValue().toString();
							final Optional<Locale> locale = Optional.of(loc);
							table.put(Optional.empty(), locale, tableValue);

						}
					}
				}

			}

			if (feature instanceof LocalizedFeature)
			{
				final Map<Locale, List<FeatureValue>> valueMap = ((LocalizedFeature) feature).getValuesForAllLocales();

				for (final Locale loc : locales)
				{
					final List<FeatureValue> localizedValues = valueMap.get(loc);
					for (final FeatureValue featureValue : localizedValues)
					{
						final String value = featureValue.getValue().toString();
						final Optional<String> nameValue = Optional.of(getSanitizeIdStrategy().sanitizeIdWithNumber(value));
						final Optional<Locale> locale = Optional.of(loc);
						table.put(nameValue, locale, value);

					}
				}
			}
			else
			{
				for (final FeatureValue featureValue : featureValues)
				{
					final String value = featureValue.getValue().toString();
					final Optional<String> nameValue = Optional.of(getSanitizeIdStrategy().sanitizeIdWithNumber(value));
					table.put(nameValue, Optional.empty(), value);

				}
			}
		}

		return table;
	}


	/**
	 * @return the classificationService
	 */
	public ClassificationService getClassificationService()
	{
		return classificationService;
	}

	/**
	 * @param classificationService
	 *           the classificationService to set
	 */
	public void setClassificationService(final ClassificationService classificationService)
	{
		this.classificationService = classificationService;
	}

	public ClassAttributeAssignmentModel getClassAttributeAssignment()
	{
		return classAttributeAssignment;
	}

	public void setClassAttributeAssignment(final ClassAttributeAssignmentModel classAttributeAssignment)
	{
		this.classAttributeAssignment = classAttributeAssignment;
	}

}

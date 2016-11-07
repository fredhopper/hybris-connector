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
package com.fredhopper.connector.config;

import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.util.Utilities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import org.springframework.beans.factory.annotation.Required;

import com.fredhopper.connector.config.data.IndexConfig;
import com.fredhopper.connector.config.data.MetaAttributeData;
import com.fredhopper.core.connector.index.generate.data.FhAttributeBaseType;
import com.fredhopper.model.export.config.FredhopperIndexConfigModel;
import com.fredhopper.model.export.data.MetaAttributeModel;


/**
 *
 */
public class DefaultIndexConfigService implements IndexConfigService
{


	private FlexibleSearchService flexibleSearchService;


	@Override
	public IndexConfig getIndexConfig(final String indexCode)
	{
		final FredhopperIndexConfigModel indexConfigModel = getConfigModel(indexCode);
		final IndexConfig indexConfig = new IndexConfig();


		final Set<MetaAttributeModel> attributes = indexConfigModel.getMetaAttributes();
		final HashSet<Locale> locales = getLocales(indexConfigModel);



		final ArrayList<MetaAttributeData> baseAttributes = new ArrayList<>();
		final ArrayList<MetaAttributeData> variantAttributes = new ArrayList<>();

		for (final MetaAttributeModel metaAttributeModel : attributes)
		{
			if (metaAttributeModel.isVariantAttribute())
			{
				variantAttributes.add(convertMetaAttribute(metaAttributeModel, locales));
			}
			else
			{
				baseAttributes.add(convertMetaAttribute(metaAttributeModel, locales));
			}
		}

		indexConfig.setLocales(locales);
		indexConfig.setBaseMetaAttributes(baseAttributes);
		indexConfig.setVariantMetaAttributes(variantAttributes);

		return indexConfig;
	}


	private MetaAttributeData convertMetaAttribute(final MetaAttributeModel metaAttibute, final Set<Locale> locales)
	{
		final MetaAttributeData attributeData = new MetaAttributeData();

		attributeData.setBaseType(FhAttributeBaseType.valueOf(metaAttibute.getBaseType().toString()));
		attributeData.setAttributeId(metaAttibute.getAttributeId());
		attributeData.setVariantAttribute(Boolean.valueOf(metaAttibute.isVariantAttribute()));
		attributeData.setQualifier(metaAttibute.getQualifier());
		attributeData.setProvider(metaAttibute.getProvider());
		attributeData.setClassAttributeAssignment(metaAttibute.getClassAttributeAssignment());
		final HashMap<Locale, String> names = new HashMap<>();
		for (final Locale locale : locales)
		{
			names.put(locale, metaAttibute.getName(locale));
		}
		attributeData.setNames(names);
		return attributeData;
	}


	private HashSet<Locale> getLocales(final FredhopperIndexConfigModel indexConfigModel)
	{

		final Collection<String> localesList = indexConfigModel.getLocales();
		final HashSet<Locale> locales = new HashSet<>();
		for (final String localeString : localesList)
		{
			final String[] loc = Utilities.parseLocaleCodes(localeString);
			final Locale locale = new Locale(loc[0], loc[1], loc[2]);
			locales.add(locale);
		}
		return locales;
	}


	private FredhopperIndexConfigModel getConfigModel(final String code)
	{
		final FredhopperIndexConfigModel example = new FredhopperIndexConfigModel();
		example.setCode(code);
		return flexibleSearchService.getModelByExample(example);

	}

	public FlexibleSearchService getFlexibleSearchService()
	{
		return flexibleSearchService;
	}

	@Required
	public void setFlexibleSearchService(final FlexibleSearchService flexibleSearchService)
	{
		this.flexibleSearchService = flexibleSearchService;
	}


}

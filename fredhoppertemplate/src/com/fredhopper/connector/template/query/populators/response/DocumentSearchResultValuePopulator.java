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
package com.fredhopper.connector.template.query.populators.response;

import de.hybris.platform.commercefacades.product.ImageFormatMapping;
import de.hybris.platform.commerceservices.search.resultdata.SearchResultValueData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.DocumentData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;

import com.fredhopper.webservice.client.Attribute;
import com.fredhopper.webservice.client.AttributeTypeFormat;
import com.fredhopper.webservice.client.Item;
import com.fredhopper.webservice.client.Searchterms;
import com.fredhopper.webservice.client.Value;


public class DocumentSearchResultValuePopulator implements Populator<DocumentData<Searchterms, Item>, SearchResultValueData>
{
	private CommonI18NService commonI18NService;
	private ImageFormatMapping imageFormatMapping;

	@Override
	public void populate(final DocumentData<Searchterms, Item> source, final SearchResultValueData target)
	{
		target.setValues(extractValues(source));
	}

	protected Map<String, Object> extractValues(final DocumentData<Searchterms, Item> source)
	{
		final Map<String, Object> valueMap = new HashMap<>();
		final Item item = source.getDocument();
		String baseCode = null;
		String variantCode = null;
		for (final Attribute attribute : item.getAttribute())
		{
			if (attribute.getName().matches("image_\\d+"))
			{
				addImageValue(attribute, valueMap);
			}
			else if (attribute.getName().matches("price_[a-z]+"))
			{
				addPriceValue(attribute, valueMap);
			}
			else if (attribute.getName().equals("code"))
			{
				baseCode = attribute.getValue().get(0).getValue();
			}
			else if (attribute.getName().equals("variant_code"))
			{
				variantCode = attribute.getValue().get(0).getValue();
			}
			else if (attribute.getBasetype().equals(AttributeTypeFormat.SET) && !attribute.getName().equals("url"))
			{
				final List<String> values = new ArrayList<>();
				for (final Value value : attribute.getValue())
				{
					values.add(value.getValue());
				}
				valueMap.put(attribute.getName(), values);
			}
			else
			{
				valueMap.put(attribute.getName(), attribute.getValue().get(0).getValue());
			}
		}

		if (StringUtils.isNotBlank(variantCode))
		{
			valueMap.put("code", variantCode);
		}
		else if (StringUtils.isNotBlank(baseCode))
		{
			valueMap.put("code", baseCode);
		}

		return valueMap;
	}

	private void addImageValue(final Attribute attribute, final Map<String, Object> valueMap)
	{
		final String key = getImageFormatMapping().getMediaFormatQualifierForImageFormat(attribute.getName());
		valueMap.put(key, attribute.getValue().get(0).getValue());
	}

	private void addPriceValue(final Attribute attribute, final Map<String, Object> valueMap)
	{
		final String currencyIso = getCommonI18NService().getCurrentCurrency().getIsocode();
		if (attribute.getName().toUpperCase().endsWith(currencyIso))
		{
			valueMap.put("priceValue", new Double(attribute.getValue().get(0).getValue()));
		}
	}

	public CommonI18NService getCommonI18NService()
	{
		return commonI18NService;
	}

	@Required
	public void setCommonI18NService(final CommonI18NService commonI18NService)
	{
		this.commonI18NService = commonI18NService;
	}

	public ImageFormatMapping getImageFormatMapping()
	{
		return imageFormatMapping;
	}

	@Required
	public void setImageFormatMapping(final ImageFormatMapping imageFormatMapping)
	{
		this.imageFormatMapping = imageFormatMapping;
	}
}

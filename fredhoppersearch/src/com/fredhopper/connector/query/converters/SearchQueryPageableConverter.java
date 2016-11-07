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
package com.fredhopper.connector.query.converters;

import de.hybris.platform.commerceservices.i18n.CommerceCommonI18NService;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SearchQueryPageableData;
import de.hybris.platform.converters.impl.AbstractPopulatingConverter;

import java.util.Locale;

import com.fredhopper.connector.query.data.FhSearchQueryData;
import com.fredhopper.lang.query.Query;


public class SearchQueryPageableConverter extends AbstractPopulatingConverter<SearchQueryPageableData<FhSearchQueryData>, Query>
{

	private CommerceCommonI18NService commerceCommonI18NService;
	private String universe;

	@Override
	protected Query createTarget()
	{
		final Locale locale = commerceCommonI18NService.getLocaleForLanguage(commerceCommonI18NService.getCurrentLanguage());
		return new Query(universe, locale.toString());
	}



	public String getUniverse()
	{
		return universe;
	}


	public void setUniverse(final String universe)
	{
		this.universe = universe;
	}



	public CommerceCommonI18NService getCommerceCommonI18NService()
	{
		return commerceCommonI18NService;
	}



	public void setCommerceCommonI18NService(final CommerceCommonI18NService commerceCommonI18NService)
	{
		this.commerceCommonI18NService = commerceCommonI18NService;
	}

}

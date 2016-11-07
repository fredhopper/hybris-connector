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
package com.fredhopper.connector.query.populators.request;

import de.hybris.platform.acceleratorservices.config.SiteConfigService;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SearchQueryPageableData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import com.fredhopper.connector.query.data.FhSearchQueryData;
import com.fredhopper.lang.query.Query;


/**
 *
 */
public class SearchPagePopulator implements Populator<SearchQueryPageableData<FhSearchQueryData>, Query>
{
	private SiteConfigService siteConfigService;

	@Override
	public void populate(final SearchQueryPageableData<FhSearchQueryData> source, final Query target) throws ConversionException
	{
		final PageableData pageableData = source.getPageableData();
		final int pageSize = getSiteConfigService().getInt("fredhopper.search.pageSize", 20);
		target.setListViewSize(pageSize);
		if (pageableData != null)
		{
			target.setListStartIndex(pageableData.getCurrentPage() * pageSize);
		}
	}

	public SiteConfigService getSiteConfigService()
	{
		return siteConfigService;
	}

	public void setSiteConfigService(final SiteConfigService siteConfigService)
	{
		this.siteConfigService = siteConfigService;
	}
}

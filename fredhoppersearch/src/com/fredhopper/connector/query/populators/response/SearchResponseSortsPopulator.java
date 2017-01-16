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
package com.fredhopper.connector.query.populators.response;


import de.hybris.platform.commerceservices.search.facetdata.FacetSearchPageData;
import de.hybris.platform.commerceservices.search.pagedata.SortData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryData;
import de.hybris.platform.converters.Populator;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

import com.fredhopper.connector.query.data.FhSearchQueryData;
import com.fredhopper.connector.query.data.FhSearchResponse;


/**
 * {@link Populator} from a {@link FhSearchResponse} to Hybris' {@link SortData}
 */
public class SearchResponseSortsPopulator<I> implements Populator<FhSearchResponse, FacetSearchPageData<SolrSearchQueryData, I>>
{
	private List<String> searchResultSortOptions;

	@Override
	public void populate(final FhSearchResponse source, final FacetSearchPageData<SolrSearchQueryData, I> target)
	{
		target.setSorts(buildSorts(source.getRequest()));
	}

	protected List<SortData> buildSorts(final FhSearchQueryData request)
	{
		final List<SortData> result = new ArrayList<>();

		final List<String> sorts = getSearchResultSortOptions();
		if (CollectionUtils.isNotEmpty(sorts))
		{
			for (final String sort : sorts)
			{
				final SortData sortData = createSortData(request.getSort(), sort);
				result.add(sortData);
			}
		}
		return result;
	}

	protected SortData createSortData(final String currentSortCode, final String sort)
	{
		final SortData sortData = new SortData();
		sortData.setCode(sort);
		sortData.setName(sort);
		if (currentSortCode != null && currentSortCode.equals(sort))
		{
			sortData.setSelected(true);
		}
		return sortData;
	}

	public List<String> getSearchResultSortOptions()
	{
		return searchResultSortOptions;
	}

	@Required
	public void setSearchResultSortOptions(final List<String> searchResultSortOptions)
	{
		this.searchResultSortOptions = searchResultSortOptions;
	}

}

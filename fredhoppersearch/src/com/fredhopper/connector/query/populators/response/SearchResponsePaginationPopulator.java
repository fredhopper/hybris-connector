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
import de.hybris.platform.commerceservices.search.pagedata.PaginationData;
import de.hybris.platform.converters.Populator;

import java.util.Optional;

import com.fredhopper.connector.query.data.FhSearchQueryData;
import com.fredhopper.connector.query.data.FhSearchResponse;
import com.fredhopper.webservice.client.Results;
import com.fredhopper.webservice.client.Universe;


/**
 * Pagination {@link Populator} from a {@link FhSearchResponse} to Hybris' {@link FacetSearchPageData}
 */
public class SearchResponsePaginationPopulator<I> extends AbstractSearchResponsePopulator
		implements Populator<FhSearchResponse, FacetSearchPageData<FhSearchQueryData, I>>
{
	@Override
	public void populate(final FhSearchResponse source, final FacetSearchPageData<FhSearchQueryData, I> target)
	{
		final PaginationData paginationData = new PaginationData();

		final Optional<Universe> universe = getUniverse(source);
		if (universe.isPresent() && universe.get().getItemsSection() != null
				&& universe.get().getItemsSection().getResults() != null)
		{
			final Results results = universe.get().getItemsSection().getResults();
			paginationData.setCurrentPage(source.getRequest().getCurrentPage());
			paginationData.setNumberOfPages((int) Math.ceil((double) results.getTotalItems() / results.getViewSize()));
			paginationData.setPageSize(results.getViewSize());
			paginationData.setSort(source.getRequest().getSort());
			paginationData.setTotalNumberOfResults(results.getTotalItems());
		}
		else
		{
			paginationData.setCurrentPage(1);
			paginationData.setNumberOfPages(1);
			paginationData.setPageSize(1);
			paginationData.setSort(source.getRequest().getSort());
			paginationData.setTotalNumberOfResults(0);
		}
		target.setPagination(paginationData);
	}

}

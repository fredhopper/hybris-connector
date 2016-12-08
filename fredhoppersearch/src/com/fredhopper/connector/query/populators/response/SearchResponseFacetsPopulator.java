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

import de.hybris.platform.commerceservices.search.facetdata.FacetData;
import de.hybris.platform.commerceservices.search.facetdata.FacetSearchPageData;
import de.hybris.platform.commerceservices.search.facetdata.FacetValueData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections.CollectionUtils;

import com.fredhopper.connector.query.data.FhSearchQueryData;
import com.fredhopper.connector.query.data.FhSearchResponse;
import com.fredhopper.webservice.client.Facetmap;
import com.fredhopper.webservice.client.Filter;
import com.fredhopper.webservice.client.Filtersection;
import com.fredhopper.webservice.client.Results;
import com.fredhopper.webservice.client.Universe;


/**
 * {@link Populator} from a {@link FhSearchResponse} to Hybris' {@link FacetSearchPageData}
 */
public class SearchResponseFacetsPopulator<I> extends AbstractSearchResponsePopulator
		implements Populator<FhSearchResponse, FacetSearchPageData<FhSearchQueryData, I>>
{

	@Override
	public void populate(final FhSearchResponse source, final FacetSearchPageData<FhSearchQueryData, I> target)
			throws ConversionException
	{
		final Optional<Universe> universe = getUniverse(source);
		if (universe.isPresent() && CollectionUtils.isNotEmpty(universe.get().getFacetmap()))
		{
			final Results results = universe.get().getItemsSection().getResults();
			final int totalNumberResults = results != null ? results.getTotalItems() : 1;
			final List<Facetmap> facetMap = universe.get().getFacetmap();
			final List<FacetData<FhSearchQueryData>> facets = new ArrayList<>();
			for (final Facetmap facet : facetMap)
			{
				final List<Filter> filters = facet.getFilter();
				for (final Filter filter : filters)
				{
					final FacetData<FhSearchQueryData> facetData = createFacetData(source, filter, totalNumberResults);
					facets.add(facetData);
				}
			}
			target.setFacets(facets);
		}
		else
		{
			target.setFacets(Collections.emptyList());
		}
	}

	protected FacetData<FhSearchQueryData> createFacetData(final FhSearchResponse source, final Filter filter,
			final int totalNumberResults)
	{
		final FacetData<FhSearchQueryData> facetData = new FacetData<>();
		facetData.setCode(filter.getFacetid());
		final boolean multiselect = filter.getDisplayHint() != null && filter.getDisplayHint().contains("multiselect");
		facetData.setMultiSelect(multiselect);
		facetData.setName(filter.getTitle());
		facetData.setVisible(true);
		final List<FacetValueData<FhSearchQueryData>> facetValues = new ArrayList<>();
		for (final Filtersection filterSection : filter.getFiltersection())
		{
			if (filterSection.getNr() < totalNumberResults)
			{
				final FacetValueData<FhSearchQueryData> facetValue = createFacetValueData(source, filter, filterSection);
				facetValues.add(facetValue);
			}
		}
		facetData.setValues(facetValues);
		return facetData;
	}

	protected FacetValueData<FhSearchQueryData> createFacetValueData(final FhSearchResponse source, final Filter filter,
			final Filtersection filterSection)
	{
		final FacetValueData<FhSearchQueryData> facetValue = new FacetValueData<>();
		facetValue.setCode(filter.getFacetid() + "_" + filterSection.getValue().getValue());
		facetValue.setCount(filterSection.getNr());
		facetValue.setName(filterSection.getLink().getName());

		final FhSearchQueryData queryData = cloneSearchQueryData(source.getRequest());
		queryData.setLocation(filterSection.getLink().getUrlParams());
		facetValue.setQuery(queryData);
		facetValue.setSelected(filterSection.isSelected() != null ? filterSection.isSelected().booleanValue() : false);
		return facetValue;
	}
}

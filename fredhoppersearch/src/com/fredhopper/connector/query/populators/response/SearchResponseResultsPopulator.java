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
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.DocumentData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Required;

import com.fredhopper.connector.query.data.FhSearchQueryData;
import com.fredhopper.connector.query.data.FhSearchResponse;
import com.fredhopper.webservice.client.Item;
import com.fredhopper.webservice.client.Items;
import com.fredhopper.webservice.client.Searchterms;
import com.fredhopper.webservice.client.Universe;


public class SearchResponseResultsPopulator<I> extends AbstractSearchResponsePopulator
		implements Populator<FhSearchResponse, FacetSearchPageData<FhSearchQueryData, I>>
{
	private Converter<DocumentData, I> searchResultConverter;

	@Override
	public void populate(final FhSearchResponse source, final FacetSearchPageData<FhSearchQueryData, I> target)
	{
		final Optional<Universe> universe = getUniverse(source);
		if (universe.isPresent() && universe.get().getItemsSection() != null)
		{
			final Items items = universe.get().getItemsSection().getItems();
			final List<I> results = new ArrayList<>();
			for (final Item item : items.getItem())
			{
				results.add(convertResultDocument(source.getPage().getSearchterms(), item));
			}
			target.setResults(results);
		}
		else
		{
			target.setResults(Collections.emptyList());
		}
	}

	protected I convertResultDocument(final Searchterms searchTerms, final Item item)
	{
		final DocumentData<Searchterms, Item> documentData = new DocumentData<>();
		documentData.setSearchQuery(searchTerms);
		documentData.setDocument(item);
		return getSearchResultConverter().convert(documentData);
	}

	protected Converter<DocumentData, I> getSearchResultConverter()
	{
		return searchResultConverter;
	}

	@Required
	public void setSearchResultConverter(final Converter<DocumentData, I> searchResultConverter)
	{
		this.searchResultConverter = searchResultConverter;
	}
}


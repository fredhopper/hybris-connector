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
package com.fredhopper.connector.query.populators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commerceservices.search.facetdata.FacetSearchPageData;
import de.hybris.platform.commerceservices.search.resultdata.SearchResultValueData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.DocumentData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryData;
import de.hybris.platform.solrfacetsearch.converters.AbstractPopulatingConverter;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.Before;
import org.junit.Test;

import com.fredhopper.connector.query.AbstractQueryTest;
import com.fredhopper.connector.query.data.FhSearchResponse;
import com.fredhopper.connector.query.populators.response.SearchResponseResultsPopulator;
import com.fredhopper.connector.template.query.populators.response.DocumentSearchResultValuePopulator;
import com.fredhopper.webservice.client.Attribute;
import com.fredhopper.webservice.client.Item;
import com.fredhopper.webservice.client.Searchterms;


/**
 *
 */
@UnitTest
public class SearchResponseResultsPopulatorTest extends AbstractQueryTest
{

	SearchResponseResultsPopulator populator;

	@Before
	public void setUp()
	{
		populator = new SearchResponseResultsPopulator();
		final DocumentSearchResultValuePopulator valuePopulator = new DocumentSearchResultValuePopulator();
		final AbstractPopulatingConverter searchResultConverter = new AbstractPopulatingConverter<DocumentData<Searchterms, Item>, SearchResultValueData>()
		{

			@Override
			public void populate(final DocumentData<Searchterms, Item> source, final SearchResultValueData target)
			{
				valuePopulator.populate(source, target);
			}
		};
		searchResultConverter.setTargetClass(SearchResultValueData.class);
		populator.setSearchResultConverter(searchResultConverter);
	}


	/**
	 * Test method for
	 * {@link com.fredhopper.connector.template.query.populators.response.SearchResponseResultsPopulator#populate(com.fredhopper.webservice.client.Page, de.hybris.platform.commerceservices.search.facetdata.FacetSearchPageData)}
	 * .
	 */
	@Test
	public void test()
	{
		final FhSearchResponse source = new FhSearchResponse();
		source.setPage(getPage("fredhoppersearch/test/response-template/rootCatalog.xml"));
		final FacetSearchPageData<SolrSearchQueryData, SearchResultValueData> target = new FacetSearchPageData();
		populator.populate(source, target);

		final List<Item> items = source.getPage().getUniverses().getUniverse().get(1).getItemsSection().getItems().getItem();
		final List<SearchResultValueData> results = target.getResults();

		assertFalse(CollectionUtils.isEmpty(items));
		assertEquals(items.size(), results.size());

		final Item item = items.get(0);
		final String id = item.getId();


		final Optional<SearchResultValueData> optional = results.stream().filter(data -> data.getValues().containsValue(id))
				.findAny();
		assertTrue(optional.isPresent());
		final Map<String, Object> valueMap = optional.get().getValues();

		for (final Attribute attribute : item.getAttribute())
		{
			final Object object = valueMap.get(attribute.getName());
			assertNotNull(object);
		}
	}
}

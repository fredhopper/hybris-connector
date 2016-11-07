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
package com.fredhopper.connector.query.populate;

import static org.junit.Assert.assertNotNull;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commerceservices.search.facetdata.FacetSearchPageData;
import de.hybris.platform.commerceservices.search.resultdata.SearchResultValueData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryData;

import org.junit.Ignore;

import com.fredhopper.connector.query.AbstractQueryTest;
import com.fredhopper.connector.query.data.FhSearchResponse;
import com.fredhopper.connector.query.populators.response.SearchResponseQueryPopulator;
import com.fredhopper.webservice.client.Page;


/**
 *
 */
@UnitTest
public class SearchResponseQueryPopulatorTest extends AbstractQueryTest
{

	SearchResponseQueryPopulator populator;

	/**
	 * Test method for
	 * {@link com.fredhopper.connector.query.populators.response.SearchResponseQueryPopulator#populate(com.fredhopper.webservice.client.Page, de.hybris.platform.commerceservices.search.facetdata.FacetSearchPageData)}
	 * .
	 */
	@Ignore
	public void test()
	{

		final Page page = getPage("fredhoppersearch/test/response-template/rootCatalog.xml");
		final FhSearchResponse source = new FhSearchResponse();
		source.setPage(page);

		populator = new SearchResponseQueryPopulator();
		final FacetSearchPageData<SolrSearchQueryData, SearchResultValueData> target = new FacetSearchPageData();
		populator.populate(source, target);
		assertNotNull(target.getCurrentQuery());
	}

}

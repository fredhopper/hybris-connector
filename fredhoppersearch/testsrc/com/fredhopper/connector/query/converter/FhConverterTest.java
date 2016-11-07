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
package com.fredhopper.connector.query.converter;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.product.data.CategoryData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.search.data.SearchQueryData;
import de.hybris.platform.commercefacades.search.data.SearchStateData;
import de.hybris.platform.commerceservices.search.facetdata.BreadcrumbData;
import de.hybris.platform.commerceservices.search.facetdata.FacetData;
import de.hybris.platform.commerceservices.search.facetdata.FacetValueData;
import de.hybris.platform.commerceservices.search.facetdata.ProductCategorySearchPageData;
import de.hybris.platform.commerceservices.search.pagedata.PaginationData;
import de.hybris.platform.commerceservices.search.pagedata.SortData;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Ignore;

import com.fredhopper.core.connector.query.service.DefaultFasWebserviceFactory;
import com.fredhopper.core.connector.query.service.DefaultFhQueryService;
import com.fredhopper.lang.query.Query;
import com.fredhopper.webservice.client.Breadcrumbs;
import com.fredhopper.webservice.client.FASWebService;
import com.fredhopper.webservice.client.Facetmap;
import com.fredhopper.webservice.client.Filter;
import com.fredhopper.webservice.client.Filtersection;
import com.fredhopper.webservice.client.Item;
import com.fredhopper.webservice.client.Page;
import com.fredhopper.webservice.client.Results;
import com.fredhopper.webservice.client.Searchterms;
import com.fredhopper.webservice.client.SortField;
import com.fredhopper.webservice.client.Universe;
import com.fredhopper.webservice.client.UniverseType;


/**
 *
 */
@UnitTest
public class FhConverterTest
{

	protected FASWebService getFasWebservice()
	{
		final DefaultFasWebserviceFactory fasWebserviceFactory = new DefaultFasWebserviceFactory();

		fasWebserviceFactory.setServicePassword("your_password");
		fasWebserviceFactory.setServiceUsername("example5");
		fasWebserviceFactory.setServiceUrl("http://localhost:1080/fredhopper-ws/services/FASWebService");
		return fasWebserviceFactory.getObject();
	}


	@Ignore
	public void testRetry() throws UnsupportedEncodingException
	{
		final DefaultFhQueryService queryService = new DefaultFhQueryService();
		queryService.setFasWebService(getFasWebservice());
		queryService.setMaxRetries(3);

		final Query query = new Query("fh_location=//catalog01/en_GB");



		final Page page = queryService.execute(query.toString());



		for (final Universe u : page.getUniverses().getUniverse())
		{
			if (UniverseType.SELECTED.equals(u.getType()))
			{
				final Breadcrumbs breadcrumbs = u.getBreadcrumbs();
				final int nrOfItemsInSelection = breadcrumbs.getNrOfItemsInSelection();
				System.out.println("Items in selection: " + nrOfItemsInSelection);

				// There is only one selected universe in a response, so we can break.
				break;
			}
		}

		Universe universe = null;

		for (final Universe u : page.getUniverses().getUniverse())
		{
			if (UniverseType.SELECTED.equals(u.getType()))
			{
				universe = u;
				break;
			}
		}

		final ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData> searchPageData = new ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData>();

		//setCurrentQuery - START
		final SearchStateData searchStateData = new SearchStateData();

		final SearchQueryData searchQueryData = new SearchQueryData();
		searchQueryData.setValue(universe.getLink().getUrlParams());

		searchStateData.setQuery(searchQueryData);

		final String baseUrl = "/search?q="; //default search url or category url  - check SearchStatePopulator
		final String queryUrl = searchStateData.getQuery().getValue();
		searchStateData.setUrl(baseUrl + queryUrl);
		searchPageData.setCurrentQuery(searchStateData);
		//setCurrentQuery - END

		//setPagination - START - check SearchResponsePaginationPopulator
		final PaginationData paginationData = new PaginationData();
		final Results results = universe.getItemsSection().getResults();
		paginationData.setCurrentPage(results.getCurrentSet());
		paginationData.setNumberOfPages((int) Math.ceil((double) results.getTotalItems() / results.getViewSize()));
		paginationData.setPageSize(results.getViewSize());
		//TODO Check is possible to have multipse sorts?!
		if (!results.getRanking().getSortFields().getSortField().isEmpty())
		{
			final SortField sort = results.getRanking().getSortFields().getSortField().get(0);
			paginationData.setSort(sort.getSortAttribute());
		}
		paginationData.setTotalNumberOfResults(results.getTotalItems());

		searchPageData.setPagination(paginationData);
		//setPagination - END


		//setSorts - START
		final List<SortData> sorts = new ArrayList<SortData>();
		//TODO: populate from index config or so...
		searchPageData.setSorts(sorts);
		//setSorts - END

		//setBreadcrumbs  - START - check BreadcrumbPopulator
		final List<BreadcrumbData<SearchStateData>> breadcrumbs = new ArrayList<BreadcrumbData<SearchStateData>>();
		//no need to convert the breadcrumbs - they are created after, in Hybris via the SearchBreadcrumbBuilder, based on the freeTextSearch and categoryCode values from searchPageData
		searchPageData.setBreadcrumbs(breadcrumbs);
		//setBreadcrumbs  - END

		//setFreeTextSearch - START
		final Searchterms searchTerms = page.getSearchterms();
		final String textSearchQuery = searchTerms != null && searchTerms.getTerm() != null ? searchTerms.getTerm().getValue() : "";
		searchPageData.setFreeTextSearch(textSearchQuery);
		//setFreeTextSearch - END

		//setCategoryCode - START
		final String categoryCode = null;
		//TODO: how to find category code?
		searchPageData.setCategoryCode(categoryCode);
		//setCategoryCode - END

		//setFacets - START
		final List<FacetData<SearchStateData>> facets = new ArrayList<FacetData<SearchStateData>>();
		final List<Facetmap> facetMap = universe.getFacetmap();
		for (final Facetmap facet : facetMap)
		{
			final List<Filter> filters = facet.getFilter();
			for (final Filter filter : filters)
			{
				final FacetData<SearchStateData> facetData = new FacetData<SearchStateData>();
				facetData.setCode(filter.getFacetid());
				final boolean multiselect = filter.getDisplayHint() != null && filter.getDisplayHint().contains("multiselect");
				facetData.setMultiSelect(multiselect);
				facetData.setName(filter.getTitle());
				facetData.setVisible(true);
				final List<FacetValueData<SearchStateData>> facetValues = new ArrayList<FacetValueData<SearchStateData>>();
				for (final Filtersection filterSection : filter.getFiltersection())
				{
					final FacetValueData<SearchStateData> facetValue = new FacetValueData<SearchStateData>();
					facetValue.setCode(filter.getFacetid() + "_" + filterSection.getValue().getValue());
					facetValue.setCount(filterSection.getNr());
					facetValue.setName(filterSection.getValue().getValue());

					final SearchStateData facetValueQuery = new SearchStateData();
					final SearchQueryData queryData = new SearchQueryData();
					queryData.setValue(filterSection.getLink().getValue().getValue());
					facetValueQuery.setQuery(queryData);

					searchStateData.setQuery(searchQueryData);
					facetValue.setQuery(facetValueQuery);
					facetValue.setSelected(filterSection.isSelected().booleanValue());

				}
				facetData.setValues(facetValues);
				//TODO: do we have to set those?
				//			facetData.setCategory(category);
				//			facetData.setPriority(priority);
				//			facetData.setTopValues(topValues);
				facets.add(facetData);
			}
		}
		searchPageData.setFacets(facets);
		//setFacets - END

		//setResults - START
		final List<ProductData> productResults = new ArrayList<ProductData>();
		for (final Item item : universe.getItemsSection().getItems().getItem())
		{
			System.out.println(item);
			final ProductData productData = new ProductData();
			//TODO: populate product attributes


			productResults.add(productData);
		}
		searchPageData.setResults(productResults);
		//setResults - END

	}

}

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
package com.fredhopper.connector.query;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commerceservices.search.ProductSearchService;
import de.hybris.platform.commerceservices.search.facetdata.ProductCategorySearchPageData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SearchQueryPageableData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryTermData;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.Collections;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.fredhopper.connector.query.data.FhSearchQueryData;
import com.fredhopper.connector.query.data.FhSearchResponse;
import com.fredhopper.core.connector.query.FhQueryService;
import com.fredhopper.lang.query.Query;
import com.fredhopper.webservice.client.Page;
import com.google.common.base.Preconditions;


/**
 * Default implementation of {@link ProductSearchService}
 */
public class DefaultFhProductSearchService<I>
		implements ProductSearchService<FhSearchQueryData, I, ProductCategorySearchPageData<FhSearchQueryData, I, CategoryModel>>
{
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(DefaultFhProductSearchService.class);

	private FhQueryService fhQueryService;
	private Converter<FhSearchResponse, ProductCategorySearchPageData<FhSearchQueryData, I, CategoryModel>> searchResponseConverter;
	private Converter<SearchQueryPageableData<FhSearchQueryData>, Query> searchQueryPageableConverter;


	@Override
	public ProductCategorySearchPageData<FhSearchQueryData, I, CategoryModel> textSearch(final String text,
			final PageableData pageableData)
	{
		Preconditions.checkArgument(StringUtils.isNotBlank(text));

		final FhSearchQueryData searchQueryData = createSearchQueryData();
		searchQueryData.setFreeTextSearch(text);
		searchQueryData.setFilterTerms(Collections.<SolrSearchQueryTermData> emptyList());

		return doSearch(searchQueryData, pageableData);
	}

	@Override
	public ProductCategorySearchPageData<FhSearchQueryData, I, CategoryModel> categorySearch(final String categoryCode,
			final PageableData pageableData)
	{
		Preconditions.checkArgument(StringUtils.isNotBlank(categoryCode));

		final FhSearchQueryData searchQueryData = createSearchQueryData();
		searchQueryData.setCategoryCode(categoryCode);
		searchQueryData.setFilterTerms(Collections.<SolrSearchQueryTermData> emptyList());

		return doSearch(searchQueryData, pageableData);
	}

	@Override
	public ProductCategorySearchPageData<FhSearchQueryData, I, CategoryModel> searchAgain(final FhSearchQueryData searchQueryData,
			final PageableData pageableData)
	{
		Preconditions.checkArgument(searchQueryData != null);

		return doSearch(searchQueryData, pageableData);
	}

	protected ProductCategorySearchPageData<FhSearchQueryData, I, CategoryModel> doSearch(final FhSearchQueryData searchQueryData,
			final PageableData pageableData)
	{
		if (pageableData != null)
		{
			searchQueryData.setSort(pageableData.getSort());
			searchQueryData.setCurrentPage(pageableData.getCurrentPage());
		}

		final SearchQueryPageableData<FhSearchQueryData> searchQueryPageableData = buildSearchQueryPageableData(searchQueryData,
				pageableData);

		final Query query = searchQueryPageableConverter.convert(searchQueryPageableData);

		final Page page = fhQueryService.execute(query.toString());
		final FhSearchResponse response = new FhSearchResponse();
		searchQueryData.setLocation(query.toQueryString());
		response.setRequest(searchQueryData);
		response.setPage(page);
		return searchResponseConverter.convert(response);
	}

	protected SearchQueryPageableData<FhSearchQueryData> buildSearchQueryPageableData(final FhSearchQueryData searchQueryData,
			final PageableData pageableData)
	{
		final SearchQueryPageableData<FhSearchQueryData> searchQueryPageableData = createSearchQueryPageableData();
		searchQueryPageableData.setSearchQueryData(searchQueryData);
		searchQueryPageableData.setPageableData(pageableData);
		return searchQueryPageableData;
	}

	// Create methods for data object - can be overridden in spring config

	protected SearchQueryPageableData<FhSearchQueryData> createSearchQueryPageableData()
	{
		return new SearchQueryPageableData<FhSearchQueryData>();
	}

	protected FhSearchQueryData createSearchQueryData()
	{
		return new FhSearchQueryData();
	}


	public FhQueryService getFhQueryService()
	{
		return fhQueryService;
	}

	@Required
	public void setFhQueryService(final FhQueryService fhQueryService)
	{
		this.fhQueryService = fhQueryService;
	}

	public Converter<FhSearchResponse, ProductCategorySearchPageData<FhSearchQueryData, I, CategoryModel>> getSearchResponseConverter()
	{
		return searchResponseConverter;
	}

	@Required
	public void setSearchResponseConverter(
			final Converter<FhSearchResponse, ProductCategorySearchPageData<FhSearchQueryData, I, CategoryModel>> searchResponseConverter)
	{
		this.searchResponseConverter = searchResponseConverter;
	}

	public Converter<SearchQueryPageableData<FhSearchQueryData>, Query> getSearchQueryPageableConverter()
	{
		return searchQueryPageableConverter;
	}

	@Required
	public void setSearchQueryPageableConverter(
			final Converter<SearchQueryPageableData<FhSearchQueryData>, Query> searchQueryPageableConverter)
	{
		this.searchQueryPageableConverter = searchQueryPageableConverter;
	}



}

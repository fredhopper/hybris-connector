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

import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SearchQueryPageableData;
import de.hybris.platform.converters.Populator;

import com.fredhopper.connector.query.data.FhSearchQueryData;
import com.fredhopper.core.connector.index.generate.validator.SanitizeIdStrategy;
import com.fredhopper.lang.query.Query;
import com.fredhopper.lang.query.location.Location;
import com.fredhopper.lang.query.location.criteria.Criterion;
import com.fredhopper.lang.query.location.criteria.MultiValuedCriterion;
import com.fredhopper.lang.query.location.criteria.SearchCriterion;
import com.fredhopper.lang.query.location.criteria.ValueSet;
import com.fredhopper.lang.query.location.criteria.ValueSet.AggregationType;


/**
 *
 */
public class SearchQueryPopulator implements Populator<SearchQueryPageableData<FhSearchQueryData>, Query>
{
	private SanitizeIdStrategy sanitizeIdStrategy;

	@Override
	public void populate(final SearchQueryPageableData<FhSearchQueryData> source, final Query target)
	{

		final FhSearchQueryData searchQueryData = source.getSearchQueryData();
		if (searchQueryData.getLocation() != null)
		{
			final Query query = new Query(searchQueryData.getLocation());
			//final Location location = new Location(searchQueryData.getLocation());
			target.setLocation(query.getLocation());
		}
		else if (searchQueryData.getCategoryCode() != null)
		{
			populateCategoryQuery(searchQueryData.getCategoryCode(), target);
		}
		else if (searchQueryData.getFreeTextSearch() != null)
		{
			populateSearchQuery(searchQueryData.getFreeTextSearch(), target);
		}
		else
		{
			throw new IllegalStateException();
		}
	}



	private void populateCategoryQuery(final String categoryCode, final Query query)
	{

		final Location location = query.getLocation();
		final ValueSet selectedCategory = new ValueSet(AggregationType.AND);
		selectedCategory.add(getSanitizeIdStrategy().sanitizeIdWithNumber(categoryCode));
		final MultiValuedCriterion catCriterion = new MultiValuedCriterion("allcategories", selectedCategory, null, false);
		location.addCriterion(catCriterion);

	}

	private void populateSearchQuery(final String searchTerm, final Query query)
	{

		final Location location = query.getLocation();
		final Criterion searchTermCriterion = new SearchCriterion(searchTerm);
		location.addCriterion(searchTermCriterion);

	}



	public SanitizeIdStrategy getSanitizeIdStrategy()
	{
		return sanitizeIdStrategy;
	}



	public void setSanitizeIdStrategy(final SanitizeIdStrategy sanitizeIdStrategy)
	{
		this.sanitizeIdStrategy = sanitizeIdStrategy;
	}



}

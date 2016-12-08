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

import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SearchQueryPageableData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import org.apache.commons.lang.StringUtils;

import com.fredhopper.connector.query.data.FhSearchQueryData;
import com.fredhopper.lang.query.Query;
import com.fredhopper.lang.query.SortDirection;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;


/**
 * Sorting {@link Populator} from a Hybris {@link SearchQueryPageableData} to a Fredhopper {@link Query}
 */
public class SearchSortPopulator implements Populator<SearchQueryPageableData<FhSearchQueryData>, Query>
{

	@Override
	public void populate(final SearchQueryPageableData<FhSearchQueryData> source, final Query target) throws ConversionException
	{
		final PageableData pageableData = source.getPageableData();
		if (pageableData != null && StringUtils.isNotBlank(pageableData.getSort()))
		{
			final Iterable<String> split = Splitter.on('_').split(pageableData.getSort());
			if (Iterables.size(split) > 1)
			{
				final String attributeName = Iterables.get(split, 0);
				final String direction = Iterables.get(split, 1);

				if ("ASC".equalsIgnoreCase(direction))
				{
					target.addSortingBy(attributeName, SortDirection.ASC);
				}
				else
				{
					target.addSortingBy(attributeName, SortDirection.DESC);
				}
			}
		}
	}

}

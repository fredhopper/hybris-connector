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

import java.util.Optional;

import com.fredhopper.connector.query.data.FhSearchQueryData;
import com.fredhopper.connector.query.data.FhSearchResponse;
import com.fredhopper.webservice.client.Universe;
import com.fredhopper.webservice.client.UniverseType;


/**
 * Abstract class containing utility methods for a {@link Populator} from a {@link FhSearchResponse} to a
 * {@link FhSearchQueryData}
 */
public abstract class AbstractSearchResponsePopulator
{
	public Optional<Universe> getUniverse(final FhSearchResponse source)
	{
		if (source.getPage().getUniverses() != null)
		{
			return source.getPage().getUniverses().getUniverse().stream().filter(u -> u.getType().equals(UniverseType.SELECTED))
					.findFirst();
		}
		return Optional.empty();
	}

	public FhSearchQueryData cloneSearchQueryData(final FhSearchQueryData source)
	{
		final FhSearchQueryData target = new FhSearchQueryData();
		target.setFreeTextSearch(source.getFreeTextSearch());
		target.setCategoryCode(source.getCategoryCode());
		target.setSort(source.getSort());
		target.setFilterTerms(source.getFilterTerms());
		target.setCurrentPage(source.getCurrentPage());
		target.setLocation(source.getLocation());
		return target;
	}
}

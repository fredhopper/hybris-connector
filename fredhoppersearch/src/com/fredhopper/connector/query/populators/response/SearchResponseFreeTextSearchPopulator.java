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


import de.hybris.platform.commerceservices.search.facetdata.ProductSearchPageData;
import de.hybris.platform.converters.Populator;

import com.fredhopper.connector.query.data.FhSearchQueryData;
import com.fredhopper.connector.query.data.FhSearchResponse;


/**
 * FreeTextSearch {@link Populator} from a {@link FhSearchResponse} to Hybris' {@link ProductSearchPageData}
 */
public class SearchResponseFreeTextSearchPopulator<I>
		implements Populator<FhSearchResponse, ProductSearchPageData<FhSearchQueryData, I>>
{
	@Override
	public void populate(final FhSearchResponse source, final ProductSearchPageData<FhSearchQueryData, I> target)
	{
		target.setFreeTextSearch(source.getRequest().getFreeTextSearch());
	}

}
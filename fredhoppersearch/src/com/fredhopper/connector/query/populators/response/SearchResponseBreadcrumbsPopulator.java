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

import de.hybris.platform.commerceservices.search.facetdata.BreadcrumbData;
import de.hybris.platform.commerceservices.search.facetdata.FacetSearchPageData;
import de.hybris.platform.converters.Populator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.fredhopper.connector.query.data.FhSearchQueryData;
import com.fredhopper.connector.query.data.FhSearchResponse;
import com.fredhopper.webservice.client.Crumb;
import com.fredhopper.webservice.client.Link;
import com.fredhopper.webservice.client.LinkType;
import com.fredhopper.webservice.client.Universe;


/**
 * Breadcrumbs {@link Populator} from a {@link FhSearchResponse} to Hybris' {@link FacetSearchPageData}
 */
public class SearchResponseBreadcrumbsPopulator<I> extends AbstractSearchResponsePopulator
		implements Populator<FhSearchResponse, FacetSearchPageData<FhSearchQueryData, I>>
{

	@Override
	public void populate(final FhSearchResponse source, final FacetSearchPageData<FhSearchQueryData, I> target)
	{
		final Optional<Universe> universe = getUniverse(source);
		if (universe.isPresent() && universe.get().getBreadcrumbs() != null)
		{
			final List<BreadcrumbData<FhSearchQueryData>> result = new ArrayList<>();
			for (final Crumb crumb : universe.get().getBreadcrumbs().getCrumb())
			{
				if (isValidCrumb(crumb))
				{
					final BreadcrumbData<FhSearchQueryData> breadcrumbData = createBreadcrumbData(source, crumb);
					result.add(breadcrumbData);
				}
			}
			target.setBreadcrumbs(result);
		}
		else
		{
			target.setBreadcrumbs(Collections.emptyList());
		}
	}

	protected boolean isValidCrumb(final Crumb crumb)
	{
		final String attributeType = crumb.getName().getAttributeType();
		final String type = crumb.getName().getType();
		return !(("home").equals(type) || ("selected-universe").equals(type) || ("search").equals(type)
				|| ("allcategories").equals(attributeType));
	}

	protected BreadcrumbData<FhSearchQueryData> createBreadcrumbData(final FhSearchResponse source, final Crumb crumb)
	{
		final BreadcrumbData<FhSearchQueryData> breadcrumbData = new BreadcrumbData<>();
		breadcrumbData.setFacetCode(crumb.getName().getValue());
		breadcrumbData.setFacetName(crumb.getName().getValue());
		breadcrumbData.setFacetValueCode(crumb.getName().getValue());
		breadcrumbData.setFacetValueName(crumb.getName().getValue());

		final Optional<Link> removeLink = crumb.getLink().stream().filter(l -> l.getType().equals(LinkType.REMOVE)).findFirst();
		if (removeLink.isPresent())
		{
			final FhSearchQueryData searchQueryData = cloneSearchQueryData(source.getRequest());
			searchQueryData.setLocation(removeLink.get().getUrlParams());
			breadcrumbData.setRemoveQuery(searchQueryData);
		}
		return breadcrumbData;
	}


}

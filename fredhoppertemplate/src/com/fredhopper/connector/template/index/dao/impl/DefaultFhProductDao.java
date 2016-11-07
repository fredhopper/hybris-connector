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
package com.fredhopper.connector.template.index.dao.impl;


import de.hybris.platform.catalog.enums.ArticleApprovalStatus;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.fredhopper.connector.index.dao.FhProductDao;
import com.fredhopper.connector.index.dao.FindCatalogVersionStrategy;


public class DefaultFhProductDao implements FhProductDao
{
   private static final Logger LOG = Logger.getLogger(DefaultFhProductDao.class);
   private static final String QUERY = "SELECT tbl.pk FROM (	{{ SELECT {p.pk} FROM {CatalogVersion AS cv}, {Catalog AS c}, {ApparelProduct! AS p} WHERE  {cv.catalog} = {c.pk} AND {p.catalogVersion} = {cv.pk} AND {cv.version} = ?version AND {c.id} = ?catalogId AND {p.approvalStatus} = ?approvalStatus AND {p.varianttype} IS NULL }} UNION ALL {{ SELECT {p.pk} FROM {CatalogVersion AS cv}, {Catalog AS c}, {ApparelStyleVariantProduct! AS p} WHERE  {cv.catalog} = {c.pk} AND {p.catalogVersion} = {cv.pk} AND {cv.version} = ?version AND {c.id} = ?catalogId AND {p.approvalStatus} = ?approvalStatus }} ) tbl ";

   private FlexibleSearchService flexibleSearchService;

   private FindCatalogVersionStrategy findCatalogVersionStrategy;

   protected FlexibleSearchService getFlexibleSearchService()
   {
	  return flexibleSearchService;
   }

   @Required
   public void setFlexibleSearchService(final FlexibleSearchService flexibleSearchService)
   {
	  this.flexibleSearchService = flexibleSearchService;
   }


   @Override
   public List<ProductModel> getItems(final int start, final int count)
   {
	  final CatalogVersionModel catalogVersion = findCatalogVersionStrategy.findCatalogVersion();
	  final Map<String, Object> parameters = new HashMap<>();
	  parameters.put("approvalStatus", ArticleApprovalStatus.APPROVED);
	  parameters.put("version", catalogVersion.getVersion());
	  parameters.put("catalogId", catalogVersion.getCatalog().getId());

	  final FlexibleSearchQuery flexibleSearchQuery = new FlexibleSearchQuery(QUERY);
	  flexibleSearchQuery.setStart(start);
	  flexibleSearchQuery.setCount(count);
	  if (LOG.isDebugEnabled())
	  {
		 LOG.debug("Executing query: " + flexibleSearchQuery.getQuery());
	  }
	  flexibleSearchQuery.setResultClassList(Collections.singletonList(ProductModel.class));
	  flexibleSearchQuery.addQueryParameters(parameters);

	  final SearchResult<ProductModel> searchResult = getFlexibleSearchService().search(flexibleSearchQuery);
	  if (LOG.isDebugEnabled())
	  {
		 LOG.debug("Query returned " + searchResult.getCount() + " records");
	  }
	  return searchResult.getResult();
   }

   public FindCatalogVersionStrategy getFindCatalogVersionStrategy()
   {
	  return findCatalogVersionStrategy;
   }

   public void setFindCatalogVersionStrategy(final FindCatalogVersionStrategy findCatalogVersionStrategy)
   {
	  this.findCatalogVersionStrategy = findCatalogVersionStrategy;
   }
}

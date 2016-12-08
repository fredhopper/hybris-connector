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
package com.fredhopper.connector.index.dao.impl;




import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;

import com.fredhopper.connector.index.dao.FindCatalogVersionStrategy;


/**
 * Default strategy for retrieving the product catalog version
 */
public class FindProductCatalogVersionStrategy implements FindCatalogVersionStrategy
{

	private String productCatalogId;

	private String productCatalogVersionId;

	private CatalogVersionService catalogVersionService;

	@Override
	public CatalogVersionModel findCatalogVersion()
	{
		return catalogVersionService.getCatalogVersion(productCatalogId, productCatalogVersionId);
	}

	/**
	 * @return the productCatalogId
	 */
	public String getProductCatalogId()
	{
		return productCatalogId;
	}

	/**
	 * @return the productCatalogVersionId
	 */
	public String getProductCatalogVersionId()
	{
		return productCatalogVersionId;
	}

	/**
	 * @return the catalogVersionService
	 */
	public CatalogVersionService getCatalogVersionService()
	{
		return catalogVersionService;
	}

	/**
	 * @param productCatalogId
	 *           the productCatalogId to set
	 */
	public void setProductCatalogId(final String productCatalogId)
	{
		this.productCatalogId = productCatalogId;
	}

	/**
	 * @param productCatalogVersionId
	 *           the productCatalogVersionId to set
	 */
	public void setProductCatalogVersionId(final String productCatalogVersionId)
	{
		this.productCatalogVersionId = productCatalogVersionId;
	}

	/**
	 * @param catalogVersionService
	 *           the catalogVersionService to set
	 */
	public void setCatalogVersionService(final CatalogVersionService catalogVersionService)
	{
		this.catalogVersionService = catalogVersionService;
	}

}

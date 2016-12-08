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
package com.fredhopper.connector.index.provider;

import de.hybris.platform.basecommerce.enums.StockLevelStatus;
import de.hybris.platform.commerceservices.stock.CommerceStockService;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.store.BaseStoreModel;

import java.util.Collection;
import java.util.Locale;
import java.util.Optional;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

import com.fredhopper.connector.config.data.MetaAttributeData;
import com.google.common.collect.HashBasedTable;


/**
 * Default {@link AttributeProvider} implementation for {@link StockLevelStatus} for {@link ProductModel}
 */
public class ProductInStockProvider extends AbstractAttributeProvider
{
	private String baseSite;

	private CommerceStockService commerceStockService;

	private FlexibleSearchService flexibleSearchService;

	@Override
	public HashBasedTable<Optional<String>, Optional<Locale>, String> getAttributeValue(final Collection<Locale> locales,
			final ProductModel product, final MetaAttributeData metaAttribute)
	{
		final HashBasedTable<Optional<String>, Optional<Locale>, String> table = HashBasedTable.create();

		final BaseStoreModel store = new BaseStoreModel();
		store.setUid(baseSite);
		final BaseStoreModel baseStore = flexibleSearchService.getModelByExample(store);

		final String value;
		if (getCommerceStockService().isStockSystemEnabled(baseStore))
		{
			value = String.valueOf(isInStock(product, baseStore));
		}
		else
		{
			value = String.valueOf(false);
		}
		if (CollectionUtils.isNotEmpty(locales) && isLocalizedAttribute(metaAttribute))
		{

			for (final Locale locale : locales)
			{
				addValues(table, locale, value, metaAttribute);
			}
		}
		else
		{
			addValues(table, null, value, metaAttribute);
		}

		return table;

	}

	protected boolean isInStock(final ProductModel product, final BaseStoreModel baseStore)
	{
		return isInStock(getProductStockLevelStatus(product, baseStore));
	}

	protected boolean isInStock(final StockLevelStatus stockLevelStatus)
	{
		return stockLevelStatus != null && !StockLevelStatus.OUTOFSTOCK.equals(stockLevelStatus);
	}

	protected StockLevelStatus getProductStockLevelStatus(final ProductModel product, final BaseStoreModel baseStore)
	{
		return getCommerceStockService().getStockLevelStatusForProductAndBaseStore(product, baseStore);
	}

	public String getBaseSite()
	{
		return baseSite;
	}

	public void setBaseSite(final String baseSite)
	{
		this.baseSite = baseSite;
	}

	public CommerceStockService getCommerceStockService()
	{
		return commerceStockService;
	}

	@Required
	public void setCommerceStockService(final CommerceStockService commerceStockService)
	{
		this.commerceStockService = commerceStockService;
	}

	public FlexibleSearchService getFlexibleSearchService()
	{
		return flexibleSearchService;
	}

	@Required
	public void setFlexibleSearchService(final FlexibleSearchService flexibleSearchService)
	{
		this.flexibleSearchService = flexibleSearchService;
	}


}

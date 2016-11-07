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

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.jalo.order.price.PriceInformation;
import de.hybris.platform.product.PriceService;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

import com.fredhopper.connector.config.data.MetaAttributeData;
import com.fredhopper.core.connector.index.generate.data.FhAttributeData;
import com.google.common.base.Preconditions;
import com.google.common.collect.HashBasedTable;


/**
 *
 */
public class ProductPriceValueProvider implements AttributeProvider
{
	private PriceService priceService;

	@Override
	public Collection<FhAttributeData> getAttribute(final ProductModel product, final MetaAttributeData metaAttribute,
			final Collection<Locale> locales)
	{
		Preconditions.checkArgument(product != null);
		Preconditions.checkArgument(metaAttribute != null);

		final List<PriceInformation> prices = priceService.getPriceInformationsForProduct(product);
		if (!CollectionUtils.isEmpty(prices))
		{
			final Optional<PriceInformation> priceInformation = prices.stream()
					.filter(price -> metaAttribute.getAttributeId().toUpperCase().endsWith(price.getPriceValue().getCurrencyIso()))
					.findFirst();
			if (priceInformation.isPresent())
			{
				final HashBasedTable<Optional<String>, Optional<Locale>, String> table = HashBasedTable.create();
				addPriceValue(table, priceInformation.get());

				final FhAttributeData attributeData = new FhAttributeData(metaAttribute.getBaseType());
				attributeData.setAttributeId(metaAttribute.getAttributeId());
				attributeData.setItemId(product.getCode());
				attributeData.setValues(table);
				return Arrays.asList(attributeData);
			}
		}
		return Collections.emptyList();
	}

	private void addPriceValue(final HashBasedTable<Optional<String>, Optional<Locale>, String> table,
			final PriceInformation priceInformation)
	{
		final String value = String.format(java.util.Locale.US, "%.2f", new Double(priceInformation.getPriceValue().getValue()));
		table.put(Optional.empty(), Optional.empty(), value);
	}

	public PriceService getPriceService()
	{
		return priceService;
	}

	@Required
	public void setPriceService(final PriceService priceService)
	{
		this.priceService = priceService;
	}

}

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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commerceservices.stock.CommerceStockService;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.store.BaseStoreModel;

import java.util.Collection;
import java.util.Collections;
import java.util.Locale;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

import com.fredhopper.connector.config.data.MetaAttributeData;
import com.fredhopper.connector.index.provider.ProductInStockProvider;
import com.fredhopper.core.connector.index.generate.data.FhAttributeBaseType;
import com.fredhopper.core.connector.index.generate.data.FhAttributeData;
import com.fredhopper.core.connector.index.generate.validator.SanitizeIdStrategy;
import com.google.common.collect.Table;


/**
 *
 */
@UnitTest
public class ProductInStockProviderTest
{
	private ProductInStockProvider productInStockProvider;
	private CommerceStockService commerceStockService;
	private FlexibleSearchService flexibleSearchService;
	private SanitizeIdStrategy sanitizeIdStrategy;

	@Before
	public void setUp() throws Exception
	{
		productInStockProvider = new ProductInStockProvider();
		commerceStockService = mock(CommerceStockService.class);
		flexibleSearchService = mock(FlexibleSearchService.class);
		sanitizeIdStrategy = mock(SanitizeIdStrategy.class);
		productInStockProvider.setCommerceStockService(commerceStockService);
		productInStockProvider.setFlexibleSearchService(flexibleSearchService);
		productInStockProvider.setBaseSite("apparel-uk");
		productInStockProvider.setSanitizeIdStrategy(sanitizeIdStrategy);

	}

	@Test
	@SuppressWarnings("boxing")
	public void testInStock()
	{
		final ProductModel product = new ProductModel();
		product.setCode("productCode");
		when(sanitizeIdStrategy.sanitizeId(product.getCode())).thenReturn(product.getCode());

		final MetaAttributeData metaAttribute = new MetaAttributeData();
		metaAttribute.setBaseType(FhAttributeBaseType.TEXT);
		metaAttribute.setAttributeId("productInStock");

		final BaseStoreModel baseStore = new BaseStoreModel();
		baseStore.setUid("apparel-uk");
		when(flexibleSearchService.getModelByExample(baseStore)).thenReturn(baseStore);
		when(Boolean.valueOf(commerceStockService.isStockSystemEnabled(baseStore))).thenReturn(Boolean.TRUE);
		final Collection<Locale> locales = Collections.emptyList();

		final Collection<FhAttributeData> fhAttributeData = productInStockProvider.getAttribute(product, metaAttribute, locales);

		final Table<Optional<String>, Optional<Locale>, String> values = fhAttributeData.iterator().next().getValues();

		assertNotNull(fhAttributeData);
		assertEquals(1, fhAttributeData.size());
		assertEquals("false", values.get(Optional.empty(), Optional.empty()));
	}

}

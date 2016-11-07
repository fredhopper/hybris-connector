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
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commerceservices.url.UrlResolver;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.session.MockSessionService;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.fredhopper.connector.config.data.MetaAttributeData;
import com.fredhopper.connector.index.provider.ProductUrlProvider;
import com.fredhopper.core.connector.index.generate.data.FhAttributeBaseType;
import com.fredhopper.core.connector.index.generate.data.FhAttributeData;
import com.fredhopper.core.connector.index.generate.validator.SanitizeIdStrategy;
import com.google.common.collect.Table;


/**
 *
 */
@UnitTest
public class ProductUrlProviderTest
{

	ProductUrlProvider productUrlProvider;
	UrlResolver<ProductModel> urlResolver;
	SessionService sessionService;
	I18NService i18nService;
	private SanitizeIdStrategy sanitizeIdStrategy;

	@Before
	public void setUp() throws Exception
	{
		productUrlProvider = new ProductUrlProvider();

		i18nService = mock(I18NService.class);
		doAnswer(new Answer<Void>()
		{
			@Override
			public Void answer(final InvocationOnMock invocation)
			{
				final Locale locale = (Locale) invocation.getArguments()[0];
				sessionService.setAttribute("locale", locale);
				return null;
			}
		}).when(i18nService).setCurrentLocale(any(Locale.class));

		sanitizeIdStrategy = mock(SanitizeIdStrategy.class);
		sessionService = new MockSessionService();
		urlResolver = mock(UrlResolver.class);

		productUrlProvider.setSanitizeIdStrategy(sanitizeIdStrategy);
		productUrlProvider.setSessionService(sessionService);
		productUrlProvider.setUrlResolver(urlResolver);
		productUrlProvider.setI18nService(i18nService);
	}

	@Test
	public void testGetNonLocalizedAttribute()
	{
		final ProductModel product = new ProductModel();
		product.setCode("productCode");
		when(sanitizeIdStrategy.sanitizeId(product.getCode())).thenReturn(product.getCode());

		final MetaAttributeData metaAttribute = new MetaAttributeData();
		metaAttribute.setBaseType(FhAttributeBaseType.TEXT);
		metaAttribute.setAttributeId("url");
		when(urlResolver.resolve(product)).thenReturn("/product/url/p/productCode");

		final Collection<FhAttributeData> attributeDatas = productUrlProvider.getAttribute(product, metaAttribute, null);

		assertNotNull(attributeDatas);
		assertEquals(1, attributeDatas.size());
		final FhAttributeData attributeData = attributeDatas.iterator().next();
		assertEquals("productCode", attributeData.getItemId());
		assertEquals("url", attributeData.getAttributeId());
		assertEquals(FhAttributeBaseType.TEXT, attributeData.getBaseType());

		final Table<Optional<String>, Optional<Locale>, String> values = attributeData.getValues();
		assertNotNull(values);
		assertEquals(1, values.size());
		final String urlValue = values.get(Optional.empty(), Optional.empty());
		assertEquals("/product/url/p/productCode", urlValue);
	}

	@Test
	public void testGetLocalizedAttribute()
	{
		final ProductModel product = new ProductModel();
		product.setCode("productCode");
		when(sanitizeIdStrategy.sanitizeId(product.getCode())).thenReturn(product.getCode());

		final MetaAttributeData metaAttribute = new MetaAttributeData();
		metaAttribute.setBaseType(FhAttributeBaseType.SET);
		metaAttribute.setAttributeId("url");
		when(urlResolver.resolve(product)).thenReturn("/product/url-EN/p/productCode");

		final Object value1 = "/product/url-EN/p/productCode";

		final Collection<Locale> locales = Arrays.asList(Locale.ENGLISH);

		when(sanitizeIdStrategy.sanitizeIdWithNumber(value1.toString())).thenReturn("_product_url_en_p_productcode");

		final Collection<FhAttributeData> attributeDatas = productUrlProvider.getAttribute(product, metaAttribute, locales);

		assertNotNull(attributeDatas);
		assertEquals(1, attributeDatas.size());
		final FhAttributeData attributeData = attributeDatas.iterator().next();
		assertEquals("productCode", attributeData.getItemId());
		assertEquals("url", attributeData.getAttributeId());
		assertEquals(FhAttributeBaseType.SET, attributeData.getBaseType());

		verify(i18nService).setCurrentLocale(Locale.ENGLISH);

		final Table<Optional<String>, Optional<Locale>, String> values = attributeData.getValues();
		assertNotNull(values);
		assertEquals(1, values.size());
		final String urlValueEN = values.get(Optional.of("_product_url_en_p_productcode"), Optional.of(Locale.ENGLISH));
		assertEquals("/product/url-EN/p/productCode", urlValueEN);
	}

}

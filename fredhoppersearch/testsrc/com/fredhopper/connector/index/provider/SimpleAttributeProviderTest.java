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
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.MockSessionService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.type.TypeService;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.fredhopper.connector.config.data.MetaAttributeData;
import com.fredhopper.connector.index.provider.SimpleAttributeProvider;
import com.fredhopper.core.connector.index.generate.data.FhAttributeBaseType;
import com.fredhopper.core.connector.index.generate.data.FhAttributeData;
import com.fredhopper.core.connector.index.generate.validator.SanitizeIdStrategy;
import com.google.common.collect.Table;


/**
 *
 */
@UnitTest
public class SimpleAttributeProviderTest
{

	SimpleAttributeProvider simpleAttributeProvider;
	I18NService i18nService;
	ModelService modelService;
	SessionService sessionService;
	TypeService typeService;
	SanitizeIdStrategy sanitizeIdStrategy;

	/**
	 *
	 */
	@Before
	public void setUp() throws Exception
	{
		simpleAttributeProvider = new SimpleAttributeProvider();

		sessionService = new MockSessionService();
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

		modelService = mock(ModelService.class);
		typeService = mock(TypeService.class);
		sanitizeIdStrategy = mock(SanitizeIdStrategy.class);
		doAnswer(new Answer<String>()
		{
			@Override
			public String answer(final InvocationOnMock invocation)
			{
				return (String) invocation.getArguments()[0];
			}
		}).when(sanitizeIdStrategy).sanitizeId(any(String.class));
		doAnswer(new Answer<String>()
		{
			@Override
			public String answer(final InvocationOnMock invocation)
			{
				return (String) invocation.getArguments()[0];
			}
		}).when(sanitizeIdStrategy).sanitizeIdWithNumber(any(String.class));

		simpleAttributeProvider.setI18nService(i18nService);
		simpleAttributeProvider.setModelService(modelService);
		simpleAttributeProvider.setSessionService(sessionService);
		simpleAttributeProvider.setTypeService(typeService);
		simpleAttributeProvider.setSanitizeIdStrategy(sanitizeIdStrategy);

	}

	@Test
	public void testGetNonLocalizedAttribute()
	{
		final ProductModel product = new ProductModel();
		product.setCode("productCode");
		final ComposedTypeModel composedType = new ComposedTypeModel();

		final MetaAttributeData metaAttribute = new MetaAttributeData();
		metaAttribute.setBaseType(FhAttributeBaseType.FLOAT);
		metaAttribute.setAttributeId("price");
		metaAttribute.setQualifier("price");

		when(typeService.getComposedTypeForClass(product.getClass())).thenReturn(composedType);
		when(Boolean.valueOf(typeService.hasAttribute(composedType, "price"))).thenReturn(Boolean.TRUE);

		when(modelService.getAttributeValue(product, "price")).thenReturn(BigDecimal.valueOf(12));

		final Collection<FhAttributeData> attributeDatas = simpleAttributeProvider.getAttribute(product, metaAttribute, null);
		assertNotNull(attributeDatas);
		assertEquals(1, attributeDatas.size());
		final FhAttributeData attributeData = attributeDatas.iterator().next();
		assertEquals("productCode", attributeData.getItemId());
		assertEquals("price", attributeData.getAttributeId());
		assertEquals(FhAttributeBaseType.FLOAT, attributeData.getBaseType());

		final Table<Optional<String>, Optional<Locale>, String> values = attributeData.getValues();
		assertNotNull(values);
		assertEquals(1, values.size());
		final String priceValue = values.get(Optional.empty(), Optional.empty());
		assertEquals("12", priceValue);
	}

	/**
	 * Test method for
	 * {@link com.fredhopper.connector.index.provider.SimpleAttributeProvider#getAttribute(de.hybris.platform.core.model.product.ProductModel, com.fredhopper.model.export.data.MetaAttributeModel)}
	 * .
	 */
	@Test
	public void testLocalizedAssetAttribute()
	{
		final ProductModel product = new ProductModel();
		product.setCode("productCode");
		final ComposedTypeModel composedType = new ComposedTypeModel();

		final MetaAttributeData metaAttribute = new MetaAttributeData();
		metaAttribute.setBaseType(FhAttributeBaseType.ASSET);
		metaAttribute.setAttributeId("description");
		metaAttribute.setQualifier("description");

		when(typeService.getComposedTypeForClass(product.getClass())).thenReturn(composedType);
		when(Boolean.valueOf(typeService.hasAttribute(composedType, "description"))).thenReturn(Boolean.TRUE);

		final Collection<Locale> locales = Arrays.asList(Locale.US);
		when(modelService.getAttributeValue(product, "description")).thenReturn("HUUUGE");

		final Collection<FhAttributeData> attributeDatas = simpleAttributeProvider.getAttribute(product, metaAttribute, locales);
		verify(i18nService, atLeastOnce()).setCurrentLocale(Locale.US);

		assertNotNull(attributeDatas);
		assertEquals(1, attributeDatas.size());
		final FhAttributeData attributeData = attributeDatas.iterator().next();
		assertEquals("productCode", attributeData.getItemId());
		assertEquals("description", attributeData.getAttributeId());
		assertEquals(FhAttributeBaseType.ASSET, attributeData.getBaseType());

		final Table<Optional<String>, Optional<Locale>, String> values = attributeData.getValues();
		assertNotNull(values);
		assertEquals(1, values.size());

		final String description = values.get(Optional.empty(), Optional.of(Locale.US));
		assertEquals("HUUUGE", description);

	}

	@Test
	public void testGetMultiValueAttribute()
	{
		final ProductModel product = new ProductModel();
		product.setCode("productCode");
		final ComposedTypeModel composedType = new ComposedTypeModel();

		final MetaAttributeData metaAttribute = new MetaAttributeData();
		metaAttribute.setBaseType(FhAttributeBaseType.SET);
		metaAttribute.setAttributeId("sizes");
		metaAttribute.setQualifier("sizes");

		when(typeService.getComposedTypeForClass(product.getClass())).thenReturn(composedType);
		when(Boolean.valueOf(typeService.hasAttribute(composedType, "sizes"))).thenReturn(Boolean.TRUE);

		final Collection<String> sizeList = Arrays.asList("S", "M", "L");
		when(modelService.getAttributeValue(product, "sizes")).thenReturn(sizeList);

		final Collection<FhAttributeData> attributeDatas = simpleAttributeProvider.getAttribute(product, metaAttribute, null);
		assertNotNull(attributeDatas);
		assertEquals(1, attributeDatas.size());
		final FhAttributeData attributeData = attributeDatas.iterator().next();
		assertEquals("productCode", attributeData.getItemId());
		assertEquals("sizes", attributeData.getAttributeId());
		assertEquals(FhAttributeBaseType.SET, attributeData.getBaseType());

		final Table<Optional<String>, Optional<Locale>, String> values = attributeData.getValues();
		assertNotNull(values);
		assertEquals(3, values.size());
		final String sizeS = values.get(Optional.of("S"), Optional.empty());
		assertEquals("S", sizeS);
		final String sizeM = values.get(Optional.of("M"), Optional.empty());
		assertEquals("M", sizeM);
		final String sizeL = values.get(Optional.of("L"), Optional.empty());
		assertEquals("L", sizeL);
	}

	@Test
	public void testGetLocalizedMultiValueAttribute()
	{
		final ProductModel product = new ProductModel();
		product.setCode("productCode");
		final ComposedTypeModel composedType = new ComposedTypeModel();

		final MetaAttributeData metaAttribute = new MetaAttributeData();
		metaAttribute.setBaseType(FhAttributeBaseType.SET);
		metaAttribute.setAttributeId("sizeNames");
		metaAttribute.setQualifier("sizeNames");

		when(typeService.getComposedTypeForClass(product.getClass())).thenReturn(composedType);
		when(Boolean.valueOf(typeService.hasAttribute(composedType, "sizeNames"))).thenReturn(Boolean.TRUE);

		final Collection<Locale> locales = Arrays.asList(Locale.US);
		final Collection<String> sizeListUS = Arrays.asList("LARGE", "XLARGE", "XXLARGE");
		when(modelService.getAttributeValue(product, "sizeNames")).thenReturn(sizeListUS);

		final Collection<FhAttributeData> attributeDatas = simpleAttributeProvider.getAttribute(product, metaAttribute, locales);
		verify(i18nService, atLeastOnce()).setCurrentLocale(Locale.US);

		assertNotNull(attributeDatas);
		assertEquals(1, attributeDatas.size());
		final FhAttributeData attributeData = attributeDatas.iterator().next();
		assertEquals("productCode", attributeData.getItemId());
		assertEquals("sizeNames", attributeData.getAttributeId());
		assertEquals(FhAttributeBaseType.SET, attributeData.getBaseType());

		final Table<Optional<String>, Optional<Locale>, String> values = attributeData.getValues();
		assertNotNull(values);
		assertEquals(3, values.size());

		final String small = values.get(Optional.of("LARGE"), Optional.of(Locale.US));
		assertEquals("LARGE", small);

		final String medium = values.get(Optional.of("XLARGE"), Optional.of(Locale.US));
		assertEquals("XLARGE", medium);

		final String large = values.get(Optional.of("XXLARGE"), Optional.of(Locale.US));
		assertEquals("XXLARGE", large);

	}

}

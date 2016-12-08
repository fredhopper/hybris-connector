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
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.MockSessionService;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.fredhopper.connector.config.data.MetaAttributeData;
import com.fredhopper.connector.index.provider.CategoryCodeProvider;
import com.fredhopper.connector.index.provider.FhCategorySource;
import com.fredhopper.core.connector.index.generate.data.FhAttributeBaseType;
import com.fredhopper.core.connector.index.generate.data.FhAttributeData;
import com.fredhopper.core.connector.index.generate.validator.SanitizeIdStrategy;


/**
 *
 */
@UnitTest
public class CategoryCodeProviderTest
{

	private CategoryCodeProvider categoryCodeProvider;
	protected I18NService i18nService;
	private FhCategorySource fhCategorySource;
	private ModelService modelService;
	private SanitizeIdStrategy sanitizeIdStrategy;
	private SessionService sessionService;

	@Before
	public void setUp() throws Exception
	{
		categoryCodeProvider = new CategoryCodeProvider();
		fhCategorySource = mock(FhCategorySource.class);
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
		sessionService = new MockSessionService();

		categoryCodeProvider.setCategorySource(fhCategorySource);
		categoryCodeProvider.setI18nService(i18nService);
		categoryCodeProvider.setModelService(modelService);
		categoryCodeProvider.setSessionService(sessionService);

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
		categoryCodeProvider.setSanitizeIdStrategy(sanitizeIdStrategy);
	}

	@Test
	public void testGetNonLocalizedCategory()
	{
		final ProductModel product = new ProductModel();
		product.setCode("productCode");

		final MetaAttributeData metaAttribute = new MetaAttributeData();
		metaAttribute.setBaseType(FhAttributeBaseType.SET);
		metaAttribute.setAttributeId("category");

		final CategoryModel category1 = mock(CategoryModel.class);
		when(category1.getCode()).thenReturn("cat1");
		when(category1.getPk()).thenReturn(PK.parse("12345"));
		final CategoryModel category2 = mock(CategoryModel.class);
		when(category2.getCode()).thenReturn("cat2");
		when(category2.getPk()).thenReturn(PK.parse("12345678"));

		final Collection<CategoryModel> categoryCollection = Arrays.asList(category1, category2);

		when(sanitizeIdStrategy.sanitizeIdWithNumber(category1.toString())).thenReturn("cat_test1");
		when(sanitizeIdStrategy.sanitizeIdWithNumber(category2.toString())).thenReturn("cat_test2");

		when(fhCategorySource.findCategoriesForProduct(product)).thenReturn(categoryCollection);
		when(modelService.getAttributeValue(category1, "code")).thenReturn(category1);
		when(modelService.getAttributeValue(category2, "code")).thenReturn(category2);


		final Collection<FhAttributeData> fhAttributeData = categoryCodeProvider.getAttribute(product, metaAttribute, null);
		assertNotNull(fhAttributeData);
		assertEquals(1, fhAttributeData.size());

	}

	@Test
	public void testGetLocalizedCategory()
	{
		final ProductModel product = new ProductModel();
		product.setCode("productCode");

		final MetaAttributeData metaAttribute = new MetaAttributeData();
		metaAttribute.setBaseType(FhAttributeBaseType.SET);
		metaAttribute.setAttributeId("category");

		final CategoryModel category1 = new CategoryModel();
		category1.setCode("cat1");
		final CategoryModel category2 = new CategoryModel();
		category2.setCode("cat2");
		final CategoryModel category3 = new CategoryModel();
		category3.setCode("cat3");
		final Collection<Locale> locales = Arrays.asList(Locale.ENGLISH, Locale.JAPANESE);

		final Collection<CategoryModel> categoryCollection = Arrays.asList(category1, category2, category3);

		when(fhCategorySource.findCategoriesForProduct(product)).thenReturn(categoryCollection);
		when(modelService.getAttributeValue(category1, "code")).thenReturn(category1);
		when(modelService.getAttributeValue(category2, "code")).thenReturn(category2);
		when(modelService.getAttributeValue(category3, "code")).thenReturn(category3);

		when(sanitizeIdStrategy.sanitizeIdWithNumber(category1.toString())).thenReturn("cat_test1");
		when(sanitizeIdStrategy.sanitizeIdWithNumber(category2.toString())).thenReturn("cat_test2");
		when(sanitizeIdStrategy.sanitizeIdWithNumber(category3.toString())).thenReturn("cat_test3");

		final Collection<FhAttributeData> fhAttributeData = categoryCodeProvider.getAttribute(product, metaAttribute, locales);

		assertNotNull(fhAttributeData);
		assertEquals(1, fhAttributeData.size());
	}
}

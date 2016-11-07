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
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.classification.ClassificationService;
import de.hybris.platform.classification.features.Feature;
import de.hybris.platform.classification.features.FeatureValue;
import de.hybris.platform.classification.features.LocalizedFeature;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.product.ProductModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.fredhopper.connector.config.data.MetaAttributeData;
import com.fredhopper.connector.index.provider.ClassificationAttributeProvider;
import com.fredhopper.core.connector.index.generate.data.FhAttributeBaseType;
import com.fredhopper.core.connector.index.generate.data.FhAttributeData;
import com.fredhopper.core.connector.index.generate.validator.SanitizeIdStrategy;
import com.google.common.collect.Table;



/**
 *
 */
@UnitTest
public class ClassificationAttributeProviderTest
{

	ClassificationAttributeProvider classificationAttributeProvider;

	@Mock
	ClassificationService classificationService;

	private ProductModel product;

	private ClassAttributeAssignmentModel classAttributeAssignment;

	private MetaAttributeData metaAttribute;

	private SanitizeIdStrategy sanitizeIdStrategy;

	@Before
	public void setUp()
	{
		classificationAttributeProvider = new ClassificationAttributeProvider();
		classificationService = mock(ClassificationService.class);
		classificationAttributeProvider.setClassificationService(classificationService);

		sanitizeIdStrategy = mock(SanitizeIdStrategy.class);
		classificationAttributeProvider.setSanitizeIdStrategy(sanitizeIdStrategy);

		product = mock(ProductModel.class);
		classAttributeAssignment = mock(ClassAttributeAssignmentModel.class);
		classificationAttributeProvider.setClassAttributeAssignment(classAttributeAssignment);

		metaAttribute = mock(MetaAttributeData.class);
	}

	@Test
	public void testAttributeTypeInt()
	{
		final FeatureValue featureValue = mock(FeatureValue.class);
		when(featureValue.getValue()).thenReturn(Integer.valueOf(8));
		final List<FeatureValue> valueList = new ArrayList<FeatureValue>();
		valueList.add(featureValue);

		final Feature feature = mock(Feature.class);
		when(feature.getCode()).thenReturn("AttributeAssignmentCode");
		when(feature.getName()).thenReturn("LocalisedFeatuerName");
		when(feature.getValue()).thenReturn(featureValue);
		when(feature.getValues()).thenReturn(valueList);

		given(classificationService.getFeature(product, classAttributeAssignment)).willReturn(feature);

		when(metaAttribute.getBaseType()).thenReturn(FhAttributeBaseType.INT);

		final FhAttributeData attributeData = classificationAttributeProvider.getAttribute(product, metaAttribute, null).iterator()
				.next();

		assertEquals(metaAttribute.getAttributeId(), attributeData.getAttributeId());
		assertEquals(FhAttributeBaseType.INT, attributeData.getBaseType());
		assertEquals(product.getCode(), attributeData.getItemId());

		//Verify values

		final Table<Optional<String>, Optional<Locale>, String> values = attributeData.getValues();

		assertEquals(1, values.size());
		assertEquals("8", values.get(Optional.empty(), Optional.empty()));

		//As many locales as expected
	}


	@Test
	public void testAttributeTypeList()
	{
		final FeatureValue featureValue1 = mock(FeatureValue.class);
		when(featureValue1.getValue()).thenReturn("blue @valueblue45");
		final FeatureValue featureValue2 = mock(FeatureValue.class);
		when(featureValue2.getValue()).thenReturn("green");
		final FeatureValue featureValue3 = mock(FeatureValue.class);
		when(featureValue3.getValue()).thenReturn("yellow");
		final List<FeatureValue> valueList = new ArrayList<FeatureValue>();
		valueList.add(featureValue1);
		valueList.add(featureValue2);
		valueList.add(featureValue3);

		final Feature feature = mock(Feature.class);
		when(feature.getCode()).thenReturn("AttributeAssignmentCode");
		when(feature.getName()).thenReturn("LocalisedFeatuerName");
		when(feature.getValue()).thenReturn(featureValue1);
		when(feature.getValues()).thenReturn(valueList);

		given(classificationService.getFeature(product, classAttributeAssignment)).willReturn(feature);

		when(sanitizeIdStrategy.sanitizeIdWithNumber("blue @valueblue45")).thenReturn("bluevalueblue");
		when(sanitizeIdStrategy.sanitizeIdWithNumber("green")).thenReturn("green");
		when(sanitizeIdStrategy.sanitizeIdWithNumber("yellow")).thenReturn("yellow");

		when(metaAttribute.getBaseType()).thenReturn(FhAttributeBaseType.LIST);

		final FhAttributeData attributeData = classificationAttributeProvider.getAttribute(product, metaAttribute, null).iterator()
				.next();

		final Table<Optional<String>, Optional<Locale>, String> values = attributeData.getValues();

		assertEquals(metaAttribute.getAttributeId(), attributeData.getAttributeId());
		assertEquals(FhAttributeBaseType.LIST, attributeData.getBaseType());
		assertEquals(product.getCode(), attributeData.getItemId());
		assertEquals(3, values.size());
		assertEquals("blue @valueblue45", values.get(Optional.of("bluevalueblue"), Optional.empty()));

	}

	@Test
	public void testAttributeTypeListLocalised()
	{
		final FeatureValue featureValue1 = mock(FeatureValue.class);
		when(featureValue1.getValue()).thenReturn("blue @valueblue45");
		when(featureValue1.getProductFeaturePk()).thenReturn(PK.parse("1234"));
		final FeatureValue featureValue2 = mock(FeatureValue.class);
		when(featureValue2.getValue()).thenReturn("green");
		when(featureValue2.getProductFeaturePk()).thenReturn(PK.parse("3456"));
		final FeatureValue featureValue3 = mock(FeatureValue.class);
		when(featureValue3.getValue()).thenReturn("yellow");
		when(featureValue3.getProductFeaturePk()).thenReturn(PK.parse("4567"));
		final List<FeatureValue> valueList = new ArrayList<FeatureValue>();
		valueList.add(featureValue1);
		valueList.add(featureValue2);
		valueList.add(featureValue3);

		final FeatureValue featureValue4 = mock(FeatureValue.class);
		when(featureValue4.getValue()).thenReturn("Husky 456");
		final FeatureValue featureValue5 = mock(FeatureValue.class);
		when(featureValue5.getValue()).thenReturn("Dashund");
		final FeatureValue featureValue6 = mock(FeatureValue.class);
		when(featureValue6.getValue()).thenReturn("Doberman");
		final List<FeatureValue> valueList1 = new ArrayList<FeatureValue>();
		valueList1.add(featureValue4);
		valueList1.add(featureValue5);
		valueList1.add(featureValue6);

		final Map<Locale, List<FeatureValue>> valueMap = new HashMap<Locale, List<FeatureValue>>();
		valueMap.put(Locale.ENGLISH, valueList);
		valueMap.put(Locale.JAPANESE, valueList1);

		final LocalizedFeature feature = mock(LocalizedFeature.class);
		when(feature.getValuesForAllLocales()).thenReturn(valueMap);
		when(feature.getCode()).thenReturn("AttributeAssignmentCode");
		when(feature.getName()).thenReturn("LocalisedFeatuerName");
		when(feature.getValues()).thenReturn(valueList);

		given(classificationService.getFeature(product, classAttributeAssignment)).willReturn(feature);

		when(metaAttribute.getBaseType()).thenReturn(FhAttributeBaseType.LIST);

		final Collection<Locale> locales = Arrays.asList(Locale.ENGLISH, Locale.JAPANESE);

		when(sanitizeIdStrategy.sanitizeIdWithNumber("blue @valueblue45")).thenReturn("bluevalueblue");
		when(sanitizeIdStrategy.sanitizeIdWithNumber("green")).thenReturn("green");
		when(sanitizeIdStrategy.sanitizeIdWithNumber("yellow")).thenReturn("yellow");
		when(sanitizeIdStrategy.sanitizeIdWithNumber("Dashund")).thenReturn("dashund");
		when(sanitizeIdStrategy.sanitizeIdWithNumber("Doberman")).thenReturn("doberman");
		when(sanitizeIdStrategy.sanitizeIdWithNumber("Husky 456")).thenReturn("husky");


		final FhAttributeData attributeData = classificationAttributeProvider.getAttribute(product, metaAttribute, locales)
				.iterator().next();

		final Table<Optional<String>, Optional<Locale>, String> values = attributeData.getValues();

		assertEquals(metaAttribute.getAttributeId(), attributeData.getAttributeId());
		assertEquals(FhAttributeBaseType.LIST, attributeData.getBaseType());
		assertEquals(product.getCode(), attributeData.getItemId());
		assertEquals(6, values.size());
		assertEquals("blue @valueblue45", values.get(Optional.of("bluevalueblue"), Optional.of(Locale.ENGLISH)));
		assertEquals("green", values.get(Optional.of("green"), Optional.of(Locale.ENGLISH)));
		assertEquals("yellow", values.get(Optional.of("yellow"), Optional.of(Locale.ENGLISH)));
		assertEquals("Husky 456", values.get(Optional.of("husky"), Optional.of(Locale.JAPANESE)));
		assertEquals("Dashund", values.get(Optional.of("dashund"), Optional.of(Locale.JAPANESE)));
		assertEquals("Doberman", values.get(Optional.of("doberman"), Optional.of(Locale.JAPANESE)));

	}

}

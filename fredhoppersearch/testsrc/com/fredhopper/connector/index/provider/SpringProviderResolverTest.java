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

import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import de.hybris.bootstrap.annotations.UnitTest;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fredhopper.connector.config.data.IndexConfig;
import com.fredhopper.connector.config.data.MetaAttributeData;
import com.fredhopper.connector.index.provider.AttributeProvider;
import com.fredhopper.connector.index.provider.ClassificationAttributeProvider;
import com.fredhopper.connector.index.provider.SimpleAttributeProvider;
import com.fredhopper.connector.index.provider.SpringProviderResolver;




@UnitTest
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations =
{ "classpath:/fredhoppersearch/fredhoppersearch-resolver-test.xml" })
public class SpringProviderResolverTest
{
	@Resource
	private SpringProviderResolver springProviderResolver;

	@Resource
	private HashSet<Locale> localesEnDe;

	@Resource
	private ClassificationAttributeProvider classificationAttributeProvider;

	@Resource
	private SimpleAttributeProvider simpleAttributeProvider;


	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);


		//springProviderResolver = new SpringProviderResolver();
		//springProviderResolver.set





	}

	@Test
	public void test()
	{
		final IndexConfig config = new IndexConfig();
		config.setLocales(localesEnDe);


		final List<MetaAttributeData> baseMetaAttributes = new ArrayList<>();
		final MetaAttributeData att1 = new MetaAttributeData();
		att1.setAttributeId("att1");
		baseMetaAttributes.add(att1);

		config.setBaseMetaAttributes(baseMetaAttributes);


		final List<MetaAttributeData> variantMetaAttributes = new ArrayList<>();
		final MetaAttributeData att2 = new MetaAttributeData();
		att2.setAttributeId("att2");
		att2.setProvider("classificationAttributeProvider");
		variantMetaAttributes.add(att1);
		variantMetaAttributes.add(att2);

		config.setVariantMetaAttributes(variantMetaAttributes);



		final Map<MetaAttributeData, AttributeProvider> baseProviders = springProviderResolver.resolveBaseProviders(config);

		assertThat(baseProviders.values(), containsInAnyOrder(simpleAttributeProvider));
		assertEquals(simpleAttributeProvider, baseProviders.get(att1));


		final Map<MetaAttributeData, AttributeProvider> variantProviders = springProviderResolver.resolveVariantProviders(config);

		assertThat(variantProviders.values(), containsInAnyOrder(simpleAttributeProvider, classificationAttributeProvider));
		assertEquals(simpleAttributeProvider, variantProviders.get(att1));
		assertEquals(classificationAttributeProvider, variantProviders.get(att2));
	}




}

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

package com.fredhopper.connector.index.populator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.category.model.CategoryModel;

import java.util.HashSet;
import java.util.Locale;
import java.util.Optional;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fredhopper.connector.config.data.IndexConfig;
import com.fredhopper.connector.index.converter.ItemToConvert;
import com.fredhopper.connector.index.populator.CategoryPopulator;
import com.fredhopper.connector.index.populator.ParentCategoryResolver;
import com.fredhopper.core.connector.index.generate.data.FhCategoryData;
import com.fredhopper.core.connector.index.generate.validator.SanitizeIdStrategy;



@UnitTest
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations =
{ "classpath:/fredhoppersearch/fredhoppersearch-test.xml" })
public class CategoryPopulatorTest
{

	private CategoryPopulator categoryPopulator;

	@Resource
	private HashSet<Locale> localesEnDe;
	@Mock
	private ItemToConvert<CategoryModel> source1;
	@Mock
	private ItemToConvert<CategoryModel> source2;
	@Mock
	private ParentCategoryResolver parentResolver;

	@Mock
	private SanitizeIdStrategy sanitizeIdStrategy;


	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);

		final IndexConfig config = mock(IndexConfig.class);

		when(config.getLocales()).thenReturn(localesEnDe);
		when(source1.getIndexConfig()).thenReturn(config);
		when(source2.getIndexConfig()).thenReturn(config);


		final CategoryModel category1 = mock(CategoryModel.class);
		mockCategory(category1, 1);
		when(source1.getItem()).thenReturn(category1);
		when(parentResolver.resolve(category1)).thenReturn(Optional.empty());

		final CategoryModel category2 = mock(CategoryModel.class);
		mockCategory(category2, 2);
		when(source2.getItem()).thenReturn(category2);
		when(parentResolver.resolve(category2)).thenReturn(Optional.of(category1));

		when(sanitizeIdStrategy.sanitizeId("cat1")).thenReturn("cat1");
		when(sanitizeIdStrategy.sanitizeId("cat2")).thenReturn("cat2");

		categoryPopulator = new CategoryPopulator();
		categoryPopulator.setParentCategoryResolver(parentResolver);

		categoryPopulator.setSanitizeIdStrategy(sanitizeIdStrategy);

	}



	private void mockCategory(final CategoryModel category, final int id)
	{
		when(category.getCode()).thenReturn("cat" + id);

		for (final Locale loc : localesEnDe)
		{
			when(category.getName(loc)).thenReturn("name" + id + loc.toString());
		}


	}

	@Test
	public void testRootCategory()
	{
		final FhCategoryData categoryData = new FhCategoryData();
		categoryPopulator.populate(source1, categoryData);
		assertEquals("cat1", categoryData.getCategoryId());
		assertNull(categoryData.getParentId());

		for (final Locale loc : localesEnDe)
		{
			assertEquals("name1" + loc.toString(), categoryData.getNames().get(loc));
		}
	}

	@Test
	public void test()
	{
		final FhCategoryData categoryData = new FhCategoryData();
		categoryPopulator.populate(source2, categoryData);
		assertEquals("cat2", categoryData.getCategoryId());
		assertEquals("cat1", categoryData.getParentId());

		for (final Locale loc : localesEnDe)
		{
			assertEquals("name2" + loc.toString(), categoryData.getNames().get(loc));
		}
	}



}

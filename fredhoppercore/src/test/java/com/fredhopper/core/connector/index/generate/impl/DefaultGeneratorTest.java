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
package com.fredhopper.core.connector.index.generate.impl;

import java.io.File;
import java.io.IOException;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fredhopper.core.connector.index.generate.Generator;
import com.fredhopper.core.connector.index.generate.collector.MockCategoryDataCollector;
import com.fredhopper.core.connector.index.generate.collector.MockMetaAttributeCollector;
import com.fredhopper.core.connector.index.generate.collector.MockProductDataCollector;
import com.fredhopper.core.connector.index.generate.context.IndexingContext;
import com.fredhopper.core.connector.index.report.Statistics;


/**
 * JUnit Tests for the FredhopperIndexService
 */
@ContextConfiguration(locations =
{ "classpath:/fredhoppercore-spring.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public class DefaultGeneratorTest
{

	@Resource
	public Generator generator;

	@Rule
	public TemporaryFolder tmpFolder = new TemporaryFolder();

	/**
	 * This is a sample test method.
	 *
	 * @throws IOException
	 */
	@Test
	public void testExporter() throws IOException
	{

		final IndexingContext context = new IndexingContext(new Statistics(), tmpFolder.getRoot());
		final File result = generator.generate(new MockMetaAttributeCollector(), new MockCategoryDataCollector(),
				new MockProductDataCollector(), context);

		Assert.assertNotNull(result);
		Assert.assertTrue(result.length() > 0);

		Assert.assertEquals(1, tmpFolder.getRoot().listFiles().length);


	}
}

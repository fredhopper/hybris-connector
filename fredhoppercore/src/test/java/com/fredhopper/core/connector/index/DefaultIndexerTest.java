/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2016 SAP SE
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * Hybris ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with SAP Hybris.
 */
package com.fredhopper.core.connector.index;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.core.io.FileSystemResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fredhopper.core.connector.config.InstanceConfig;
import com.fredhopper.core.connector.config.InstanceConfigService;
import com.fredhopper.core.connector.index.generate.Generator;
import com.fredhopper.core.connector.index.generate.collector.CategoryDataCollector;
import com.fredhopper.core.connector.index.generate.collector.CollectorFactory;
import com.fredhopper.core.connector.index.generate.collector.MetaAttributeCollector;
import com.fredhopper.core.connector.index.generate.collector.ProductDataCollector;
import com.fredhopper.core.connector.index.impl.DefaultIndexer;
import com.fredhopper.core.connector.index.upload.PublishingService;
import com.fredhopper.core.connector.index.upload.impl.DefaultPublishingService;


/**
 *
 */
@ContextConfiguration(locations =
{ "classpath:/test-fredhoppercore-spring.xml", "classpath:/fredhoppercore-spring.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public class DefaultIndexerTest
{

	@Resource
	private InstanceConfigService instanceConfigService;
	@Rule
	public final TemporaryFolder tmpFolder = new TemporaryFolder();

	private DefaultIndexer defaultFhIndexer;
	private Generator generator;
	private File newFile;
	private File newFolder;

	public final static String DEFAULT_INSTANCE_CONFIG = "default";
	private CategoryDataCollector mockCategoryCollector;
	private ProductDataCollector mockProductCollector;
	private MetaAttributeCollector mockMetaAttributeCollector;
	private PublishingService mockPublishingService;

	/**
	 *
	 */
	@Before
	public void setUp() throws Exception
	{


		mockPublishingService = mock(DefaultPublishingService.class);
		when(Boolean.valueOf(mockPublishingService.publishZip(any(), any()))).thenReturn(Boolean.TRUE);

		newFolder = tmpFolder.newFolder();
		final FileSystemResource dataDirResource = new FileSystemResource(newFolder);


		final CollectorFactory collectorFactory = mock(CollectorFactory.class);
		mockCategoryCollector = mock(CategoryDataCollector.class);
		when(collectorFactory.getCategoryCollector(DEFAULT_INSTANCE_CONFIG)).thenReturn(mockCategoryCollector);
		mockProductCollector = mock(ProductDataCollector.class);
		when(collectorFactory.getProductCollector(DEFAULT_INSTANCE_CONFIG)).thenReturn(mockProductCollector);
		mockMetaAttributeCollector = mock(MetaAttributeCollector.class);
		when(collectorFactory.getMetaAttributeCollector(DEFAULT_INSTANCE_CONFIG)).thenReturn(mockMetaAttributeCollector);

		generator = mock(Generator.class);
		newFile = tmpFolder.newFile();
		when(generator.generate(eq(mockMetaAttributeCollector), eq(mockCategoryCollector), eq(mockProductCollector), any()))
				.thenReturn(newFile);


		defaultFhIndexer = new DefaultIndexer();
		defaultFhIndexer.setDataDirectoryResource(dataDirResource);
		defaultFhIndexer.setPublishingService(mockPublishingService);
		defaultFhIndexer.setCollectorFactory(collectorFactory);
		defaultFhIndexer.setGenerator(generator);
		defaultFhIndexer.setInstanceConfigService(instanceConfigService);

	}

	/**
	 * Test method for {@link com.fredhopper.core.connector.index.impl.DefaultIndexer#index(java.lang.String)}.
	 */
	@Test
	public void testIndex() throws IOException
	{
		final InstanceConfig instageConfig = instanceConfigService.getInstageConfig(DEFAULT_INSTANCE_CONFIG + "InstanceConfig");
		defaultFhIndexer.index("default");

		Mockito.verify(generator).generate(eq(mockMetaAttributeCollector), eq(mockCategoryCollector), eq(mockProductCollector),
				any());

		Mockito.verify(mockPublishingService).publishZip(eq(instageConfig), eq(newFile));

		Assert.assertEquals(1, newFolder.list().length);
		Assert.assertTrue(newFile.exists());
	}

}

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
package com.fredhopper.core.connector.index.impl;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.core.io.Resource;

import com.fredhopper.core.connector.config.InstanceConfig;
import com.fredhopper.core.connector.config.InstanceConfigService;
import com.fredhopper.core.connector.index.FileUtils;
import com.fredhopper.core.connector.index.Indexer;
import com.fredhopper.core.connector.index.IndexingHook;
import com.fredhopper.core.connector.index.generate.Generator;
import com.fredhopper.core.connector.index.generate.collector.CategoryDataCollector;
import com.fredhopper.core.connector.index.generate.collector.CollectorFactory;
import com.fredhopper.core.connector.index.generate.collector.MetaAttributeCollector;
import com.fredhopper.core.connector.index.generate.collector.ProductDataCollector;
import com.fredhopper.core.connector.index.generate.context.IndexingContext;
import com.fredhopper.core.connector.index.generate.context.IndexingResult;
import com.fredhopper.core.connector.index.report.Statistics;
import com.fredhopper.core.connector.index.upload.PublishingService;


public class DefaultIndexer implements Indexer
{

	private Generator generator;
	private Resource dataDirectoryResource;
	private CollectorFactory collectorFactory;
	private PublishingService publishingService;
	private InstanceConfigService instanceConfigService;
	private List<IndexingHook> prePublishingHooks;
	private List<IndexingHook> postPublishingHooks;

	private static final Logger LOG = Logger.getLogger(DefaultIndexer.class);

	@Override
	public void index(final String indexConfig) throws IOException
	{
		index(indexConfig, true);
	}

	@Override
	public void index(final String indexConfig, final boolean upload) throws IOException
	{
		this.index(indexConfig, upload, -1);

	}

	@Override
	public void index(final String indexConfig, final boolean upload, final int maxNumberViolations) throws IOException
	{

		final MetaAttributeCollector metaAttributes = getCollectorFactory().getMetaAttributeCollector(indexConfig);
		final CategoryDataCollector categories = getCollectorFactory().getCategoryCollector(indexConfig);
		final ProductDataCollector products = getCollectorFactory().getProductCollector(indexConfig);
		final InstanceConfig instanceConfig = getInstanceConfigService().getConfig(indexConfig + "InstanceConfig");

		final File parentDir = createFileDirectory(getDataDirectoryResource());
		final IndexingContext context = new IndexingContext(new Statistics(), parentDir);
		final File datazip = getGenerator().generate(metaAttributes, categories, products, context);
		context.setDataZip(Optional.of(datazip));
		executeHooks(context, prePublishingHooks);
		if (canUpload(maxNumberViolations, context))
		{
			if (upload)
			{
				LOG.info("Publication Start");
				getPublishingService().publishZip(instanceConfig, datazip);
				context.setResult(IndexingResult.PUBLISHED);
			}
			else
			{
				LOG.info("Skipped Publication");
				context.setResult(IndexingResult.NOT_PUBLISHED);
			}
		}
		else
		{
			LOG.info("Aborted Publication");
			context.setResult(IndexingResult.VALIDATION_FAILURE);
		}
		executeHooks(context, postPublishingHooks);
	}

	protected void executeHooks(final IndexingContext context, final List<IndexingHook> hooks)
	{
		if (hooks != null)
		{
			for (final IndexingHook hook : hooks)
			{
				hook.execute(context);
			}
		}
	}

	protected boolean canUpload(final int maxNumberViolations, final IndexingContext context)
	{
		return maxNumberViolations == -1 || context.getStatistics().getViolations().size() < maxNumberViolations;
	}

	private File createFileDirectory(final Resource dataDirectoryResource) throws IOException
	{

		return FileUtils.createRandomDirectory(dataDirectoryResource.getFile());

	}

	public Generator getGenerator()
	{
		return generator;
	}

	@Required
	public void setGenerator(final Generator generator)
	{
		this.generator = generator;
	}

	public Resource getDataDirectoryResource()
	{
		return dataDirectoryResource;
	}

	@Required
	public void setDataDirectoryResource(final Resource dataDirectoryResource)
	{
		this.dataDirectoryResource = dataDirectoryResource;
	}

	public CollectorFactory getCollectorFactory()
	{
		return collectorFactory;
	}

	@Required
	public void setCollectorFactory(final CollectorFactory collectorFactory)
	{
		this.collectorFactory = collectorFactory;
	}

	public PublishingService getPublishingService()
	{
		return publishingService;
	}

	@Required
	public void setPublishingService(final PublishingService publishingService)
	{
		this.publishingService = publishingService;
	}

	public InstanceConfigService getInstanceConfigService()
	{
		return instanceConfigService;
	}

	@Required
	public void setInstanceConfigService(final InstanceConfigService instanceConfigService)
	{
		this.instanceConfigService = instanceConfigService;
	}


	public List<IndexingHook> getPrePublishingHooks()
	{
		return prePublishingHooks;
	}


	public void setPrePublishingHooks(final List<IndexingHook> prePublishingHooks)
	{
		this.prePublishingHooks = prePublishingHooks;
	}

	public List<IndexingHook> getPostPublishingHooks()
	{
		return postPublishingHooks;
	}

	public void setPostPublishingHooks(final List<IndexingHook> postPublishingHooks)
	{
		this.postPublishingHooks = postPublishingHooks;
	}



}

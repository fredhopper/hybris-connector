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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipOutputStream;

import org.apache.log4j.Logger;

import com.fredhopper.core.connector.index.FileUtils;
import com.fredhopper.core.connector.index.generate.Generator;
import com.fredhopper.core.connector.index.generate.collector.CategoryDataCollector;
import com.fredhopper.core.connector.index.generate.collector.MetaAttributeCollector;
import com.fredhopper.core.connector.index.generate.collector.ProductDataCollector;
import com.fredhopper.core.connector.index.generate.context.IndexingContext;
import com.fredhopper.core.connector.index.generate.engine.CategoryExporter;
import com.fredhopper.core.connector.index.generate.engine.MetaAttributeExporter;
import com.fredhopper.core.connector.index.generate.engine.ProductExporter;


public class DefaultGenerator implements Generator
{


	private CategoryExporter categoryExporter;
	private MetaAttributeExporter metaAttributeExporter;
	private ProductExporter productExporter;

	private static final Logger LOG = Logger.getLogger(DefaultGenerator.class);

	@Override
	public File generate(final MetaAttributeCollector metaAttributeCollector, final CategoryDataCollector categoryDataCollector,
			final ProductDataCollector productDataCollector, final IndexingContext context) throws IOException
	{
		LOG.info("file creation - Start");
		categoryExporter.process(categoryDataCollector, context);
		metaAttributeExporter.process(metaAttributeCollector, context);
		productExporter.process(productDataCollector, context);

		final File[] filesProduced = context.getParentDir().listFiles();
		final File result = createDataZip(context.getParentDir(), filesProduced);
		cleanup(filesProduced);
		LOG.info("file creation - End");
		return result;

	}

	private File createDataZip(final File parentDir, final File[] files) throws FileNotFoundException, IOException
	{
		final File result = new File(parentDir.getAbsolutePath() + File.separator + "data.zip");
		final FileOutputStream fos = new FileOutputStream(result);
		final ZipOutputStream zos = new ZipOutputStream(fos);
		try
		{
			for (final File file : files)
			{
				FileUtils.addToZipFile(file, zos);
			}
		}
		finally
		{
			zos.close();
			fos.close();
		}
		return result;
	}

	private void cleanup(final File[] files)
	{
		for (final File file : files)
		{
			file.delete();
		}
	}

	public CategoryExporter getCategoryExporter()
	{
		return categoryExporter;
	}

	public void setCategoryExporter(final CategoryExporter categoryExporter)
	{
		this.categoryExporter = categoryExporter;
	}

	public MetaAttributeExporter getMetaAttributeExporter()
	{
		return metaAttributeExporter;
	}

	public void setMetaAttributeExporter(final MetaAttributeExporter metaAttributeExporter)
	{
		this.metaAttributeExporter = metaAttributeExporter;
	}

	public ProductExporter getProductExporter()
	{
		return productExporter;
	}

	public void setProductExporter(final ProductExporter productExporter)
	{
		this.productExporter = productExporter;
	}

}

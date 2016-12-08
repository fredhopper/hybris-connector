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
package com.fredhopper.connector.index.collector;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.log4j.Logger;

import com.fredhopper.connector.config.data.IndexConfig;
import com.fredhopper.connector.index.converter.ItemToConvert;
import com.fredhopper.connector.index.dao.FhProductDao;
import com.fredhopper.core.connector.index.generate.data.FhProductData;


/**
 * This class collects Products and converts them into FhProductData DTOs.
 */
public class ProductIterator implements Iterator<FhProductData>
{
	private static final Logger LOG = Logger.getLogger(ProductIterator.class);

	private final FhProductDao dao;
	private final Converter<ItemToConvert<ProductModel>, FhProductData> converter;
	private final IndexConfig config;

	private final int batchSize;
	private final int offset;

	private final Queue<FhProductData> buffer;
	private int index;

	public ProductIterator(final FhProductDao dao, final Converter<ItemToConvert<ProductModel>, FhProductData> converter,
			final IndexConfig config, final int batchSize)
	{
		this(dao, converter, config, batchSize, 0, Integer.MAX_VALUE);
	}

	public ProductIterator(final FhProductDao dao, final Converter<ItemToConvert<ProductModel>, FhProductData> converter,
			final IndexConfig config, final int batchSize, final int start, final int offset)
	{
		super();
		this.dao = dao;
		this.converter = converter;
		this.config = config;
		this.batchSize = batchSize;
		this.offset = offset;
		this.buffer = new ConcurrentLinkedQueue<>();
		this.index = start;
		fillBuffer();
	}

	@Override
	public boolean hasNext()
	{
		return !buffer.isEmpty();
	}

	@Override
	public FhProductData next()
	{
		final FhProductData obj = buffer.poll();
		if (obj == null)
		{
			throw new NoSuchElementException();
		}
		if (buffer.isEmpty())
		{
			fillBuffer();
		}
		return obj;
	}

	private void fillBuffer()
	{
		if (index < 0)
		{
			return;
		}
		final List<FhProductData> batchItems = retrieveNextBatch(index, index + batchSize < offset ? batchSize : offset - index);
		if (batchItems.isEmpty())
		{
			index = -1;
		}
		else
		{
			index = index + batchSize;
		}
		buffer.addAll(batchItems);
	}

	private List<FhProductData> retrieveNextBatch(final int index, final int batchSize)
	{
		final List<FhProductData> targets = new ArrayList<>();
		final List<ProductModel> sources = dao.getItems(index, batchSize);
		for (final ProductModel source : sources)
		{
			try
			{
				final FhProductData target = converter.convert(new ItemToConvert<>(source, config));
				targets.add(target);
			}
			catch (final Exception e)
			{
				final String message = "source code " + source + " ---- Collector ERROR ----" + e.getMessage();
				LOG.error(message, e);
			}
		}
		return targets;
	}

}

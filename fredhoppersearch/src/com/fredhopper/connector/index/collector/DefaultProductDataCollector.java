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

import java.util.Iterator;

import com.fredhopper.connector.config.data.IndexConfig;
import com.fredhopper.connector.index.converter.ItemToConvert;
import com.fredhopper.connector.index.dao.FhProductDao;
import com.fredhopper.core.connector.index.generate.collector.ProductDataCollector;
import com.fredhopper.core.connector.index.generate.data.FhProductData;


/**
 * This class supplies an Iterable that contains FhProductData data collected.
 *
 */
public class DefaultProductDataCollector extends AbstractConfigurableCollector implements ProductDataCollector
{
	private FhProductDao fhProductDao;
	private Converter<ItemToConvert<ProductModel>, FhProductData> converter;
	private int batchSize = DEFAULT_BATCH_SIZE;

	public static final int DEFAULT_BATCH_SIZE = 100;

	@Override
	public Iterator<FhProductData> iterator()
	{
		final IndexConfig config = getIndexConfig();
		return new ProductIterator(fhProductDao, converter, config, batchSize);
	}

	public FhProductDao getFhProductDao()
	{
		return fhProductDao;
	}

	public void setFhProductDao(final FhProductDao fhProductDao)
	{
		this.fhProductDao = fhProductDao;
	}

	public Converter<ItemToConvert<ProductModel>, FhProductData> getConverter()
	{
		return converter;
	}

	public void setConverter(final Converter<ItemToConvert<ProductModel>, FhProductData> converter)
	{
		this.converter = converter;
	}

	public int getBatchSize()
	{
		return batchSize;
	}

	public void setBatchSize(final int batchSize)
	{
		this.batchSize = batchSize;
	}
}

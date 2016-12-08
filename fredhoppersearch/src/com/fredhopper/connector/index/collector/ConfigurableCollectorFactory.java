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

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.fredhopper.core.connector.index.generate.collector.CategoryDataCollector;
import com.fredhopper.core.connector.index.generate.collector.CollectorFactory;
import com.fredhopper.core.connector.index.generate.collector.MetaAttributeCollector;
import com.fredhopper.core.connector.index.generate.collector.ProductDataCollector;


/**
 * Factory class for Collectors
 */
public class ConfigurableCollectorFactory implements CollectorFactory, ApplicationContextAware
{

	private ApplicationContext applicationContext;

	@Override
	public CategoryDataCollector getCategoryCollector(final String indexConfig)
	{
		return (CategoryDataCollector) applicationContext.getBean(indexConfig + "fhCategoryCollector");
	}

	@Override
	public MetaAttributeCollector getMetaAttributeCollector(final String indexConfig)
	{
		return (MetaAttributeCollector) applicationContext.getBean(indexConfig + "fhMetaAttributeCollector");
	}

	@Override
	public ProductDataCollector getProductCollector(final String indexConfig)
	{
		return (ProductDataCollector) applicationContext.getBean(indexConfig + "fhProductCollector");
	}

	@Override
	public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException
	{
		this.applicationContext = applicationContext;

	}

}

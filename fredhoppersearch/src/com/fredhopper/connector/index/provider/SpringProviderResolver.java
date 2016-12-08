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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.fredhopper.connector.config.data.IndexConfig;
import com.fredhopper.connector.config.data.MetaAttributeData;


/**
 * Spring implementation of {@link ProviderResolver}
 */
public class SpringProviderResolver implements ProviderResolver, ApplicationContextAware
{
	private ApplicationContext applicationContext;
	private AttributeProvider defaultAttributeProvider;

	@Override
	public Map<MetaAttributeData, AttributeProvider> resolveVariantProviders(final IndexConfig indexConfig)
	{

		final List<MetaAttributeData> attributes = indexConfig.getVariantMetaAttributes();

		final Map<MetaAttributeData, AttributeProvider> providers = new HashMap<>();
		for (final MetaAttributeData attribute : attributes)
		{
			providers.put(attribute, resolveProvider(attribute));
		}
		return providers;
	}

	@Override
	public Map<MetaAttributeData, AttributeProvider> resolveBaseProviders(final IndexConfig indexConfig)
	{

		final List<MetaAttributeData> attributes = indexConfig.getBaseMetaAttributes();

		final Map<MetaAttributeData, AttributeProvider> providers = new HashMap<>();
		for (final MetaAttributeData attribute : attributes)
		{
			providers.put(attribute, resolveProvider(attribute));
		}
		return providers;
	}



	protected AttributeProvider resolveProvider(final MetaAttributeData attribute)
	{
		if (!StringUtils.isEmpty(attribute.getProvider()))
		{
			return (AttributeProvider) applicationContext.getBean(attribute.getProvider());

		}
		else
		{
			return defaultAttributeProvider;
		}

	}

	@Override
	public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException
	{
		this.applicationContext = applicationContext;

	}

	public AttributeProvider getDefaultAttributeProvider()
	{
		return defaultAttributeProvider;
	}

	public void setDefaultAttributeProvider(final AttributeProvider defaultAttributeProvider)
	{
		this.defaultAttributeProvider = defaultAttributeProvider;
	}

}

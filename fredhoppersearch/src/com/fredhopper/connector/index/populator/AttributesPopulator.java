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



import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.fredhopper.connector.config.data.MetaAttributeData;
import com.fredhopper.connector.index.converter.ItemToConvert;
import com.fredhopper.connector.index.provider.AttributeProvider;
import com.fredhopper.connector.index.provider.ProviderResolver;
import com.fredhopper.core.connector.index.generate.data.FhAttributeData;
import com.fredhopper.core.connector.index.generate.data.FhAttributeHolderData;


/**
 *
 */
public class AttributesPopulator<PRODUCT extends ProductModel> implements Populator<ItemToConvert<PRODUCT>, FhAttributeHolderData>
{

	private ProviderResolver providerResolver;
	private boolean variant;

	/**
	 *
	 */
	public AttributesPopulator()
	{
		super();
	}

	@Override
	public void populate(final ItemToConvert<PRODUCT> source, final FhAttributeHolderData target) throws ConversionException
	{
		final PRODUCT product = source.getItem();
		final Set<FhAttributeData> attributes = new HashSet<>();

		final Map<MetaAttributeData, AttributeProvider> providers = variant
				? providerResolver.resolveVariantProviders(source.getIndexConfig())
				: providerResolver.resolveBaseProviders(source.getIndexConfig());
		for (final MetaAttributeData metaAttribute : providers.keySet())
		{
			final AttributeProvider provider = providers.get(metaAttribute);
			final Collection<FhAttributeData> attributesData = provider.getAttribute(product, metaAttribute,
					source.getIndexConfig().getLocales());
			attributes.addAll(attributesData);
		}
		target.setAttributes(attributes);
	}

	public ProviderResolver getProviderResolver()
	{
		return providerResolver;
	}

	public void setProviderResolver(final ProviderResolver providerResolver)
	{
		this.providerResolver = providerResolver;
	}

	public boolean isVariant()
	{
		return variant;
	}

	public void setVariant(final boolean variant)
	{
		this.variant = variant;
	}

}
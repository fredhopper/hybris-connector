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

import de.hybris.platform.converters.Converters;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.variants.model.VariantProductModel;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fredhopper.connector.config.data.IndexConfig;
import com.fredhopper.connector.index.converter.ItemToConvert;
import com.fredhopper.core.connector.index.generate.data.FhProductData;
import com.fredhopper.core.connector.index.generate.data.FhVariantData;
import com.fredhopper.core.connector.index.generate.validator.SanitizeIdStrategy;
import com.google.common.base.Preconditions;


/**
 *
 */
public class ProductVariantsPopulator implements Populator<ItemToConvert<ProductModel>, FhProductData>
{

	private Converter<ItemToConvert<VariantProductModel>, FhVariantData> variantConverter;

	private SanitizeIdStrategy sanitizeIdStrategy;

	@Override
	public void populate(final ItemToConvert<ProductModel> source, final FhProductData target) throws ConversionException
	{
		Preconditions.checkArgument(source != null);
		Preconditions.checkArgument(target != null);

		final IndexConfig indexConfig = source.getIndexConfig();
		final ProductModel product = source.getItem();

		final Set<ItemToConvert<VariantProductModel>> variants = new HashSet<>();
		final Collection<VariantProductModel> sourceVariants = selectVariants(product);
		if (!sourceVariants.isEmpty())
		{
			for (final VariantProductModel variant : sourceVariants)
			{
				variants.add(new ItemToConvert<>(variant, indexConfig));
			}
			final List<FhVariantData> variantsTarget = Converters.convertAll(variants, variantConverter);
			target.setVariants(variantsTarget);
		}
		else
		{
			final FhVariantData defaultVariant = new FhVariantData();
			defaultVariant.setProductId(getSanitizeIdStrategy().sanitizeId(source.getItem().getCode()));
			defaultVariant.setVariantId("v_" + getSanitizeIdStrategy().sanitizeId(source.getItem().getCode()));
			defaultVariant.setLocales(source.getIndexConfig().getLocales());
			target.setVariants(Arrays.asList(defaultVariant));
		}



	}

	protected Collection<VariantProductModel> selectVariants(final ProductModel product)
	{
		return product.getVariants();
	}


	public Converter<ItemToConvert<VariantProductModel>, FhVariantData> getVariantConverter()
	{
		return variantConverter;
	}

	public void setVariantConverter(final Converter<ItemToConvert<VariantProductModel>, FhVariantData> variantConverter)
	{
		this.variantConverter = variantConverter;
	}

	public SanitizeIdStrategy getSanitizeIdStrategy()
	{
		return sanitizeIdStrategy;
	}

	public void setSanitizeIdStrategy(final SanitizeIdStrategy sanitizeIdStrategy)
	{
		this.sanitizeIdStrategy = sanitizeIdStrategy;
	}
}

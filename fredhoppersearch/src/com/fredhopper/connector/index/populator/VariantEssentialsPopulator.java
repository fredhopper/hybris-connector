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
import de.hybris.platform.variants.model.VariantProductModel;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

import com.fredhopper.connector.index.converter.ItemToConvert;
import com.fredhopper.core.connector.index.generate.data.FhVariantData;
import com.fredhopper.core.connector.index.generate.validator.SanitizeIdStrategy;
import com.google.common.base.Preconditions;


/**
 * Populator responsible for converting product variants to FhVariantData
 */
public class VariantEssentialsPopulator implements Populator<ItemToConvert<VariantProductModel>, FhVariantData>
{

	private SanitizeIdStrategy sanitizeIdStrategy;

	@Override
	public void populate(final ItemToConvert<VariantProductModel> source, final FhVariantData target) throws ConversionException
	{
		Preconditions.checkArgument(source != null);
		Preconditions.checkArgument(target != null);
		Preconditions.checkArgument(CollectionUtils.isNotEmpty(source.getIndexConfig().getLocales()));

		final VariantProductModel variant = source.getItem();
		target.setVariantId(getSanitizeIdStrategy().sanitizeId(variant.getCode()));

		final ProductModel baseProduct = variant.getBaseProduct();

		target.setProductId(getSanitizeIdStrategy().sanitizeId(baseProduct.getCode()));
		target.setLocales(source.getIndexConfig().getLocales());

	}

	public SanitizeIdStrategy getSanitizeIdStrategy()
	{
		return sanitizeIdStrategy;
	}

	@Required
	public void setSanitizeIdStrategy(final SanitizeIdStrategy sanitizeIdStrategy)
	{
		this.sanitizeIdStrategy = sanitizeIdStrategy;
	}

}

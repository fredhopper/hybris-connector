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

import de.hybris.platform.core.model.product.ProductModel;

import java.util.Collection;
import java.util.Locale;

import com.fredhopper.connector.config.data.MetaAttributeData;
import com.fredhopper.core.connector.index.generate.data.FhAttributeData;


/**
 * Collects and converts a given product's attribute values to FhAttributeData based on the supplied meta-attribute
 * definition
 */
@FunctionalInterface
public interface AttributeProvider
{
	public Collection<FhAttributeData> getAttribute(final ProductModel product, final MetaAttributeData metaAttribute,
			final Collection<Locale> locales);
}

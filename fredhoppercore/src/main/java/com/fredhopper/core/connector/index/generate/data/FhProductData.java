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
package com.fredhopper.core.connector.index.generate.data;

import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;


public class FhProductData extends FhAttributeHolderData
{
	private static final long serialVersionUID = 1L;

	private String productId;
	private HashMap<Locale, Set<String>> categories;
	private Collection<FhVariantData> variants;

	public String getProductId()
	{
		return productId;
	}

	public void setProductId(final String productId)
	{
		this.productId = productId;
	}

	public Map<Locale, Set<String>> getCategories()
	{
		return categories;
	}

	public void setCategories(final Map<Locale, Set<String>> categories)
	{
		this.categories = new HashMap<>(categories);
	}

	public Collection<FhVariantData> getVariants()
	{
		return variants;
	}

	public void setVariants(final Collection<FhVariantData> variants)
	{
		this.variants = variants;
	}


}

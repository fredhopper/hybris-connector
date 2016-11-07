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

package com.fredhopper.connector.config.data;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;


public class IndexConfig implements java.io.Serializable
{

	private String code;
	private HashSet<Locale> locales;
	private ArrayList<MetaAttributeData> baseMetaAttributes;
	private ArrayList<MetaAttributeData> variantMetaAttributes;


	public void setCode(final String code)
	{
		this.code = code;
	}

	public String getCode()
	{
		return code;
	}

	public void setLocales(final Set<Locale> locales)
	{
		this.locales = new HashSet<>(locales);
	}

	public Set<Locale> getLocales()
	{
		return locales;
	}

	public void setBaseMetaAttributes(final List<MetaAttributeData> baseMetaAttributes)
	{
		this.baseMetaAttributes = new ArrayList<>(baseMetaAttributes);
	}

	public List<MetaAttributeData> getBaseMetaAttributes()
	{
		return baseMetaAttributes;
	}

	public void setVariantMetaAttributes(final List<MetaAttributeData> variantMetaAttributes)
	{
		this.variantMetaAttributes = new ArrayList<>(variantMetaAttributes);
	}

	public List<MetaAttributeData> getVariantMetaAttributes()
	{
		return variantMetaAttributes;
	}



}
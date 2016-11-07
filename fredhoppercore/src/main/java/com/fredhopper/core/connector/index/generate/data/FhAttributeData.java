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

import java.io.Serializable;
import java.util.Locale;
import java.util.Optional;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;


public class FhAttributeData implements Serializable
{

	private static final long serialVersionUID = 1L;

	private String itemId;
	private String attributeId;
	private FhAttributeBaseType baseType;

	private HashBasedTable<Optional<String>, Optional<Locale>, String> values;

	public FhAttributeData(final FhAttributeBaseType baseType)
	{
		this.setBaseType(baseType);
	}

	public String getAttributeId()
	{
		return attributeId;
	}

	public void setAttributeId(final String attributeId)
	{
		this.attributeId = attributeId;
	}

	public String getItemId()
	{
		return itemId;
	}

	public void setItemId(final String itemId)
	{
		this.itemId = itemId;
	}

	public FhAttributeBaseType getBaseType()
	{
		return baseType;
	}

	private void setBaseType(final FhAttributeBaseType baseType)
	{
		this.baseType = baseType;
	}

	public final Table<Optional<String>, Optional<Locale>, String> getValues()
	{
		return values;
	}

	public final void setValues(final Table<Optional<String>, Optional<Locale>, String> values)
	{
		this.values = HashBasedTable.create(values);
	}

}

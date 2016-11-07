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
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class FhCategoryData implements Serializable
{
	private static final long serialVersionUID = 1L;

	private String categoryId;
	private String parentId;
	private HashMap<Locale, String> names;

	public String getCategoryId()
	{
		return categoryId;
	}

	public void setCategoryId(final String categoryId)
	{
		this.categoryId = categoryId;
	}

	public String getParentId()
	{
		return parentId;
	}

	public void setParentId(final String parentId)
	{
		this.parentId = parentId;
	}

	public Map<Locale, String> getNames()
	{
		return names;
	}

	public void setNames(final Map<Locale, String> names)
	{
		this.names = new HashMap<>(names);
	}
}

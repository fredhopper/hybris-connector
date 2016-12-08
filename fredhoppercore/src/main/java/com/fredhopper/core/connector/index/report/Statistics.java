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
/**
 *
 */
package com.fredhopper.core.connector.index.report;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;

import com.fredhopper.core.connector.index.generate.data.FhAttributeData;
import com.fredhopper.core.connector.index.generate.data.FhCategoryData;
import com.fredhopper.core.connector.index.generate.data.FhMetaAttributeData;
import com.fredhopper.core.connector.index.generate.data.FhProductData;
import com.fredhopper.core.connector.index.generate.data.FhVariantData;


public class Statistics
{

	private final List<Violation> violations;
	ConcurrentHashMap<String, LongAdder> exported;

	public Statistics()
	{
		this.violations = new ArrayList<>();
		exported = new ConcurrentHashMap<>();
		exported.put(FhProductData.class.getName(), new LongAdder());
		exported.put(FhVariantData.class.getName(), new LongAdder());
		exported.put(FhCategoryData.class.getName(), new LongAdder());
		exported.put(FhAttributeData.class.getName(), new LongAdder());
		exported.put(FhMetaAttributeData.class.getName(), new LongAdder());
	}


	public void increaseExported(final String classname)
	{
		exported.computeIfAbsent(classname, k -> new LongAdder()).increment();
	}

	public void addViolation(final List<Violation> violations)
	{
		this.violations.addAll(violations);
	}

	public List<Violation> getViolations()
	{
		return violations;
	}

	public long getExported(final String classname)
	{
		return exported.get(classname).longValue();
	}
}
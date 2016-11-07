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
package com.fredhopper.core.connector.index.generate.collector;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import com.fredhopper.core.connector.index.generate.data.FhAttributeBaseType;
import com.fredhopper.core.connector.index.generate.data.FhMetaAttributeData;


/**
 *
 */
public class MockMetaAttributeCollector implements MetaAttributeCollector
{

	private final Set<FhMetaAttributeData> data;
	private final Iterator<FhMetaAttributeData> iterator;

	/**
	 *
	 */
	public MockMetaAttributeCollector()
	{

		final Set<FhMetaAttributeData> attributes = new HashSet<>();

		final Locale locale_en = new Locale("en");
		final Locale locale_de = new Locale("de");

		final FhMetaAttributeData attr0 = new FhMetaAttributeData();
		attr0.setAttributeId("processor_speed");
		attr0.setBaseType(FhAttributeBaseType.FLOAT);
		Map<Locale, String> names = new HashMap<>();
		names.put(locale_en, "Processor speed");
		names.put(locale_de, "Prozessorgeschwindigkeit");
		attr0.setNames(names);
		attributes.add(attr0);

		final FhMetaAttributeData attr1 = new FhMetaAttributeData();
		attr1.setAttributeId("color");
		attr1.setBaseType(FhAttributeBaseType.SET);
		names = new HashMap<>();
		names.put(locale_en, "Colour");
		names.put(locale_de, "Farbe");
		attr1.setNames(names);
		attributes.add(attr1);

		final FhMetaAttributeData attr2 = new FhMetaAttributeData();
		attr2.setAttributeId("name");
		attr2.setBaseType(FhAttributeBaseType.ASSET);
		names = new HashMap<>();
		names.put(locale_en, "Description");
		names.put(locale_de, "Beschreibung");
		attr2.setNames(names);
		attributes.add(attr2);

		this.data = attributes;
		this.iterator = data.iterator();
	}


	@Override
	public Iterator<FhMetaAttributeData> iterator()
	{
		return data.iterator();
	}



}

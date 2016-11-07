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

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.fredhopper.core.connector.index.generate.data.FhAttributeBaseType;
import com.fredhopper.core.connector.index.generate.data.FhAttributeData;
import com.fredhopper.core.connector.index.generate.data.FhProductData;
import com.fredhopper.core.connector.index.generate.data.FhVariantData;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;


/**
 *
 */
public class MockProductDataCollector implements ProductDataCollector
{

	private final Set<FhProductData> data;
	private final Iterator<FhProductData> iterator;

	/**
	 *
	 */
	public MockProductDataCollector()
	{
		final Locale locale_en = new Locale("en");
		final Locale locale_de = new Locale("de");

		final FhProductData prd1 = new FhProductData();
		prd1.setProductId("prd1");
		Map<Locale, Set<String>> categories = new HashMap<>();
		categories.put(locale_en, new HashSet<String>(Arrays.asList(new String[]
		{ "000102" })));
		categories.put(locale_de, new HashSet<String>(Arrays.asList(new String[]
		{ "000102" })));
		prd1.setCategories(categories);

		final FhAttributeData color_blue = new FhAttributeData(FhAttributeBaseType.SET);
		color_blue.setAttributeId("color");
		color_blue.setItemId("prd1");
		final Table<Optional<String>, Optional<Locale>, String> blue_value = HashBasedTable.create();
		blue_value.put(Optional.of("blue"), Optional.of(locale_en), "Blue");
		blue_value.put(Optional.of("blue"), Optional.of(locale_de), "Blau");
		color_blue.setValues(blue_value);

		prd1.setAttributes(new HashSet<FhAttributeData>(Arrays.asList(new FhAttributeData[]
		{ color_blue })));

		final FhVariantData var1 = new FhVariantData();
		var1.setVariantId("var1");
		var1.setProductId("prd1");
		var1.setLocales(new HashSet<Locale>(Arrays.asList(new Locale[]
		{ locale_en, locale_de })));

		final FhAttributeData speed1 = new FhAttributeData(FhAttributeBaseType.FLOAT);
		speed1.setAttributeId("processor_speed");
		speed1.setItemId("var1");
		final Table<Optional<String>, Optional<Locale>, String> speed_value1 = HashBasedTable.create();
		speed_value1.put(Optional.empty(), Optional.empty(), "1.33");
		speed1.setValues(speed_value1);
		var1.setAttributes(new HashSet<FhAttributeData>(Arrays.asList(new FhAttributeData[]
		{ speed1 })));

		final FhVariantData var2 = new FhVariantData();
		var2.setVariantId("var2");
		var2.setProductId("prd1");
		var2.setLocales(new HashSet<Locale>(Arrays.asList(new Locale[]
		{ locale_en, locale_de })));

		final FhAttributeData speed2 = new FhAttributeData(FhAttributeBaseType.FLOAT);
		speed2.setAttributeId("processor_speed");
		speed2.setItemId("var2");
		final Table<Optional<String>, Optional<Locale>, String> speed_value2 = HashBasedTable.create();
		speed_value2.put(Optional.empty(), Optional.empty(), "1.66");
		speed2.setValues(speed_value2);
		var2.setAttributes(new HashSet<FhAttributeData>(Arrays.asList(new FhAttributeData[]
		{ speed2 })));

		final FhVariantData var3 = new FhVariantData();
		var3.setVariantId("var3");
		var3.setProductId("prd1");
		var3.setLocales(new HashSet<Locale>(Arrays.asList(new Locale[]
		{ locale_en, locale_de })));

		final FhAttributeData speed3 = new FhAttributeData(FhAttributeBaseType.FLOAT);
		speed3.setAttributeId("processor_speed");
		speed3.setItemId("var3");
		final Table<Optional<String>, Optional<Locale>, String> speed_value3 = HashBasedTable.create();
		speed_value3.put(Optional.empty(), Optional.empty(), "2.4");
		speed3.setValues(speed_value3);
		var3.setAttributes(new HashSet<FhAttributeData>(Arrays.asList(new FhAttributeData[]
		{ speed3 })));

		prd1.setVariants(new HashSet<FhVariantData>(Arrays.asList(new FhVariantData[]
		{ var1, var2, var3 })));

		final FhProductData prd2 = new FhProductData();
		prd2.setProductId("prd2");
		prd2.setCategories(categories);

		final FhAttributeData color_green = new FhAttributeData(FhAttributeBaseType.SET);
		color_green.setAttributeId("color");
		color_green.setItemId("prd2");
		final Table<Optional<String>, Optional<Locale>, String> green_value = HashBasedTable.create();
		green_value.put(Optional.of("green"), Optional.of(locale_en), "Green");
		green_value.put(Optional.of("green"), Optional.of(locale_de), "Grün");
		color_green.setValues(green_value);

		prd2.setAttributes(new HashSet<FhAttributeData>(Arrays.asList(new FhAttributeData[]
		{ color_green })));

		categories = new HashMap<>();
		categories.put(locale_en, new HashSet<String>(Arrays.asList(new String[]
		{ "000103" })));
		categories.put(locale_de, new HashSet<String>(Arrays.asList(new String[]
		{ "000103" })));

		final FhProductData prd3 = new FhProductData();
		prd3.setProductId("prd3");
		prd3.setCategories(categories);

		final FhProductData prd4 = new FhProductData();
		prd4.setProductId("prd4");
		prd4.setCategories(categories);

		this.data = new HashSet<FhProductData>(Arrays.asList(new FhProductData[]
		{ prd1, prd2, prd3, prd4 }));

		this.iterator = data.iterator();

	}



	@Override
	public Iterator<FhProductData> iterator()
	{
		return iterator;
	}

}

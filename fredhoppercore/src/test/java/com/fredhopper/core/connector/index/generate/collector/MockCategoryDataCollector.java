/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2016 SAP SE
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * Hybris ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with SAP Hybris.
 */
package com.fredhopper.core.connector.index.generate.collector;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import com.fredhopper.core.connector.index.generate.data.FhCategoryData;


/**
 *
 */
public class MockCategoryDataCollector implements CategoryDataCollector
{

	private final Set<FhCategoryData> data;


	/**
	 *
	 */
	public MockCategoryDataCollector()
	{
		final Locale locale_en = new Locale("en");
		final Locale locale_de = new Locale("de");

		final FhCategoryData cat0 = new FhCategoryData();
		cat0.setCategoryId("000100");
		Map<Locale, String> names = new HashMap<>();
		names.put(locale_en, "Category 100");
		names.put(locale_de, "Kategorie 100");
		cat0.setNames(names);
		cat0.setParentId("000100");

		final FhCategoryData cat1 = new FhCategoryData();
		cat1.setCategoryId("000101");
		names = new HashMap<>();
		names.put(locale_en, "Category 101");
		names.put(locale_de, "Kategorie 101");
		cat1.setNames(names);
		cat1.setParentId("000100");

		final FhCategoryData cat2 = new FhCategoryData();
		cat2.setCategoryId("000102");
		names = new HashMap<>();
		names.put(locale_en, "Category 102");
		names.put(locale_de, "Kategorie 102");
		cat2.setNames(names);
		cat2.setParentId("000101");

		final FhCategoryData cat3 = new FhCategoryData();
		cat3.setCategoryId("000103");
		names = new HashMap<>();
		names.put(locale_en, "Category 103");
		names.put(locale_de, "Kategorie 103");
		cat3.setNames(names);
		cat3.setParentId("000101");


		this.data = new HashSet(Arrays.asList(new FhCategoryData[]
		{ cat0, cat1, cat2, cat3 }));

	}


	@Override
	public Iterator<FhCategoryData> iterator()
	{
		// YTODO Auto-generated method stub
		return data.iterator();
	}




}

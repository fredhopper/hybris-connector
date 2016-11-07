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
package com.fredhopper.core.connector.index.generate.writer;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.MapUtils;

import com.fredhopper.core.connector.index.generate.data.FhProductData;
import com.google.common.base.Preconditions;


public class ProductCsvWriter extends AbstractCsvWriter<FhProductData>
{

	public ProductCsvWriter(final File parentDir, final String filename, final List<String> columns) throws IOException
	{
		super(parentDir, filename, columns);
	}

	@Override
	public void print(final FhProductData source) throws IOException
	{
		Preconditions.checkArgument(source != null);
		Preconditions.checkArgument(!MapUtils.isEmpty(source.getCategories()));

		final Map<Locale, Set<String>> categories = source.getCategories();
		//FIXME What is this retrieval of locales from the KeySet of the categories?
		for (final Locale locale : categories.keySet())
		{
			final Set<String> categoriesSet = source.getCategories().get(locale);
			printLine(source.getProductId(), locale.toString(), buildCategoriesString(categoriesSet));
		}
	}


	protected String buildCategoriesString(final Set<String> categoryIds)
	{

		final StringBuilder builder = new StringBuilder();
		for (final String categoryId : categoryIds)
		{
			builder.append(categoryId).append(' ');
		}
		return builder.toString().trim();
	}

}

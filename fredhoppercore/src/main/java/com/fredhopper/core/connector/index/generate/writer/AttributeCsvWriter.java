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
import java.util.Optional;

import com.fredhopper.core.connector.index.generate.data.FhAttributeData;
import com.google.common.base.Preconditions;
import com.google.common.collect.Table;


public class AttributeCsvWriter extends AbstractCsvWriter<FhAttributeData>
{

	public AttributeCsvWriter(final File parentDir, final String filename, final List<String> columns) throws IOException
	{
		super(parentDir, filename, columns);
	}

	private static final Optional<String> NO_VALUEID = Optional.empty();
	private static final Optional<Locale> NO_LOCALE = Optional.empty();

	@Override
	public void print(final FhAttributeData source) throws IOException
	{

		Preconditions.checkArgument(source != null);
		Preconditions.checkArgument(source.getValues() != null && !source.getValues().isEmpty());

		switch (source.getBaseType())
		{
			case ASSET:
				printAssetAttributeValues(source);
				break;
			case FLOAT:
			case INT:
			case TEXT:
				printSingleValueAttribute(source);
				break;
			case LIST:
			case LIST64:
			case SET:
			case SET64:
				printAttributeValueCollection(source);
				break;
			default:
				throw new UnsupportedOperationException(
						"The FhAttributeBaseType \"" + source.getBaseType().getCode() + "\" is not supported.");
		}
	}

	protected void printAssetAttributeValues(final FhAttributeData source) throws IOException
	{

		final Map<Optional<Locale>, String> valueMap = source.getValues().row(NO_VALUEID);
		for (final Optional<Locale> locale : valueMap.keySet())
		{
			printLine(source.getItemId(), locale.get().toString(), source.getAttributeId(), EMPTY_VALUE, valueMap.get(locale));
		}
	}

	protected void printAttributeValueCollection(final FhAttributeData source) throws IOException
	{

		final Table<Optional<String>, Optional<Locale>, String> values = source.getValues();
		for (final Optional<String> valueId : values.rowKeySet())
		{
			final String attributeValueId = valueId.isPresent() ? valueId.get() : EMPTY_VALUE;
			final Map<Optional<Locale>, String> valueMap = values.row(valueId);
			for (final Optional<Locale> locale : valueMap.keySet())
			{
				printLine(source.getItemId(), locale.get().toString(), source.getAttributeId(), attributeValueId,
						valueMap.get(locale));
			}
		}
	}

	protected void printSingleValueAttribute(final FhAttributeData source) throws IOException
	{
		final String value = source.getValues().get(NO_VALUEID, NO_LOCALE);
		printLine(source.getItemId(), EMPTY_VALUE, source.getAttributeId(), EMPTY_VALUE, value);
	}



}

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

import com.fredhopper.core.connector.index.generate.data.FhMetaAttributeData;
import com.google.common.base.Preconditions;


public class MetaAttributeCsvWriter extends AbstractCsvWriter<FhMetaAttributeData>
{

	public MetaAttributeCsvWriter(final File parentDir, final String filename, final List<String> columns) throws IOException
	{
		super(parentDir, filename, columns);
	}

	@Override
	public void print(final FhMetaAttributeData source) throws IOException
	{
		Preconditions.checkArgument(source != null);
		final Map<Locale, String> names = source.getNames();
		for (final Locale locale : names.keySet())
		{
			printLine(source.getAttributeId(), source.getBaseType().getCode(), locale.toString(), source.getNames().get(locale));
		}
	}

}

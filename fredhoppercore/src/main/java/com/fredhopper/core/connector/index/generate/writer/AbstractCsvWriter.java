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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;


public abstract class AbstractCsvWriter<S>
{

	protected Writer writer;

	public static final String COLUMN_SEPARATOR = "\t\t";
	public static final String ROW_SEPARATOR = "\r\n";
	public static final String CHARACTER_ENCODING = "UTF-8";
	public static final String EMPTY_VALUE = "";

	/**
	 * @param parentDir
	 * @param filename
	 * @throws IOException
	 */
	public AbstractCsvWriter(final File parentDir, final String filename, final List<String> columns) throws IOException
	{
		final String fullFileName = parentDir.getAbsolutePath() + File.separator + filename;

		final FileWriter fileWriter = new FileWriter(fullFileName, true);
		writer = new BufferedWriter(fileWriter);

		printHeader(fullFileName, columns);
	}

	public void printLine(final String... values) throws IOException
	{

		for (int i = 0; i < values.length; i++)
		{
			writer.write(values[i]);
			if (i < values.length - 1)
			{
				writer.write(COLUMN_SEPARATOR);
			}
		}
		writer.write(ROW_SEPARATOR);
		writer.flush();
	}

	public void printLine(final List<String> values) throws IOException
	{

		for (int i = 0; i < values.size(); i++)
		{
			writer.write(values.get(i));
			if (i < values.size() - 1)
			{
				writer.write(COLUMN_SEPARATOR);
			}
		}
		writer.write(ROW_SEPARATOR);
		writer.flush();
	}

	public abstract void print(final S source) throws IOException;

	public void printHeader(final String fullFileName, final List<String> columns) throws IOException
	{
		final File file = new File(fullFileName);
		if (!file.exists() || file.length() == 0)
		{
			printLine(columns);
		}
	}

}

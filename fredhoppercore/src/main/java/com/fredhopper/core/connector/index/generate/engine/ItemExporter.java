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
package com.fredhopper.core.connector.index.generate.engine;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.fredhopper.core.connector.index.generate.context.IndexingContext;
import com.fredhopper.core.connector.index.generate.validator.Validator;
import com.fredhopper.core.connector.index.generate.writer.AbstractCsvWriter;
import com.fredhopper.core.connector.index.report.Violation;
import com.google.common.base.Preconditions;


public abstract class ItemExporter<I>
{
	private String fileName;
	private Set<Validator<I>> validators;
	private List<String> header;
	private static final Logger LOG = Logger.getLogger(ItemExporter.class);

	public void process(final Iterable<I> collection, final IndexingContext context) throws IOException
	{

		Preconditions.checkArgument(collection != null);
		final AbstractCsvWriter<I> writer = createWriter(context.getParentDir(), fileName, header);
		final Iterator<I> iterator = collection.iterator();
		while (iterator.hasNext())
		{
			final List<Violation> itemErrors = exportItem(writer, iterator.next(), context);
			context.getStatistics().addViolation(itemErrors);
		}
		LOG.info("file " + fileName + "  - End");
	}


	protected List<Violation> exportItem(final AbstractCsvWriter<I> writer, final I item, final IndexingContext context)
			throws IOException
	{
		final List<Violation> itemErrors = validate(item);
		if (itemErrors.isEmpty())
		{
			writer.print(item);
			context.getStatistics().increaseExported(item.getClass().getName());
		}
		return itemErrors;
	}


	protected abstract AbstractCsvWriter<I> createWriter(final File parentDir, String fileName, List<String> header)
			throws IOException;

	protected List<Violation> validate(final I source)
	{
		final List<Violation> violations = new ArrayList<>();
		for (final Validator<I> validator : validators)
		{
			violations.addAll(validator.validate(source));
		}
		return violations;
	}

	public String getFileName()
	{
		return fileName;
	}

	public void setFileName(final String fileName)
	{
		this.fileName = fileName;
	}

	public List<String> getHeader()
	{
		return header;
	}

	public void setHeader(final List<String> header)
	{
		this.header = header;
	}

	public Set<Validator<I>> getValidators()
	{
		return validators;
	}

	public void setValidators(final Set<Validator<I>> validators)
	{
		this.validators = validators;
	}
}

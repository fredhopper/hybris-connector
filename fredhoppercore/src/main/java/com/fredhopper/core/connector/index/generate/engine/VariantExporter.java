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
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

import com.fredhopper.core.connector.index.generate.context.IndexingContext;
import com.fredhopper.core.connector.index.generate.data.FhVariantData;
import com.fredhopper.core.connector.index.generate.writer.AbstractCsvWriter;
import com.fredhopper.core.connector.index.generate.writer.VariantCsvWriter;
import com.fredhopper.core.connector.index.report.Violation;


/**
 *
 */
public class VariantExporter extends ItemExporter<FhVariantData>
{
	private AttributeExporter attributeExporter;

	@Override
	protected List<Violation> exportItem(final AbstractCsvWriter<FhVariantData> writer, final FhVariantData item,
			final IndexingContext context) throws IOException
	{
		final List<Violation> violations = super.exportItem(writer, item, context);
		if (violations.isEmpty())
		{
			if (CollectionUtils.isNotEmpty(item.getAttributes()))
			{
				attributeExporter.process(item.getAttributes(), context);
			}
		}
		return violations;
	}


	public AttributeExporter getAttributeExporter()
	{
		return attributeExporter;
	}

	@Required
	public void setAttributeExporter(final AttributeExporter attributeExporter)
	{
		this.attributeExporter = attributeExporter;
	}



	@Override
	protected AbstractCsvWriter<FhVariantData> createWriter(final File parentDir, final String fileName, final List<String> header)
			throws IOException
	{
		return new VariantCsvWriter(parentDir, fileName, header);
	}

}

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

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

import com.fredhopper.core.connector.index.generate.context.IndexingContext;
import com.fredhopper.core.connector.index.generate.data.FhProductData;
import com.fredhopper.core.connector.index.generate.writer.AbstractCsvWriter;
import com.fredhopper.core.connector.index.generate.writer.ProductCsvWriter;
import com.fredhopper.core.connector.index.report.Violation;


/**
 *
 */
public class ProductExporter extends ItemExporter<FhProductData>
{

	private AttributeExporter attributeExporter;
	private VariantExporter variantExporter;


	@Override
	protected List<Violation> exportItem(final AbstractCsvWriter<FhProductData> writer, final FhProductData item,
			final IndexingContext context) throws IOException
	{
		final List<Violation> itemErrors = super.exportItem(writer, item, context);
		if (itemErrors.isEmpty())
		{
			if (CollectionUtils.isNotEmpty(item.getAttributes()))
			{
				attributeExporter.process(item.getAttributes(), context);
			}
			if (CollectionUtils.isNotEmpty(item.getVariants()))
			{
				variantExporter.process(item.getVariants(), context);
			}
		}

		return itemErrors;
	}

	@Override
	protected AbstractCsvWriter<FhProductData> createWriter(final File parentDir, final String fileName, final List<String> header)
			throws IOException
	{
		return new ProductCsvWriter(parentDir, fileName, header);
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

	public VariantExporter getVariantExporter()
	{
		return variantExporter;
	}

	@Required
	public void setVariantExporter(final VariantExporter variantExporter)
	{
		this.variantExporter = variantExporter;
	}

}

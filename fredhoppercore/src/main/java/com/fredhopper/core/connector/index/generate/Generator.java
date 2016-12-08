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
package com.fredhopper.core.connector.index.generate;

import java.io.File;
import java.io.IOException;

import com.fredhopper.core.connector.index.generate.collector.CategoryDataCollector;
import com.fredhopper.core.connector.index.generate.collector.MetaAttributeCollector;
import com.fredhopper.core.connector.index.generate.collector.ProductDataCollector;
import com.fredhopper.core.connector.index.generate.context.IndexingContext;


/**
 * Responsible for generating the output files using the Collector parameters
 *
 * @return location of the compressed data.zip
 */
@FunctionalInterface
public interface Generator
{
	public File generate(final MetaAttributeCollector metaAttributes, final CategoryDataCollector categories,
			final ProductDataCollector products, final IndexingContext context) throws IOException;
}

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
package com.fredhopper.connector.index.collector;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.fredhopper.connector.config.data.IndexConfig;
import com.fredhopper.core.connector.index.generate.collector.MetaAttributeCollector;
import com.fredhopper.core.connector.index.generate.data.FhMetaAttributeData;


/**
 * Default implementation of {@link MetaAttributeCollector}
 */
public class DefaultMetaAttributeCollector extends AbstractConfigurableCollector implements MetaAttributeCollector
{

	@Override
	public Iterator<FhMetaAttributeData> iterator()
	{

		final IndexConfig config = getIndexConfig();

		final Set<FhMetaAttributeData> attributes = new HashSet<>();
		for (final FhMetaAttributeData fhMetaAttributeData : config.getBaseMetaAttributes())
		{
			attributes.add(fhMetaAttributeData);
		}
		for (final FhMetaAttributeData fhMetaAttributeData : config.getVariantMetaAttributes())
		{
			attributes.add(fhMetaAttributeData);
		}

		return attributes.iterator();
	}


}

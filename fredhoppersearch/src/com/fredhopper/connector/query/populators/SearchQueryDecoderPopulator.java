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
package com.fredhopper.connector.query.populators;

import de.hybris.platform.commercefacades.search.data.SearchQueryData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.apache.log4j.Logger;

import com.fredhopper.connector.query.data.FhSearchQueryData;
import com.fredhopper.lang.query.Query;


/**
 */
public class SearchQueryDecoderPopulator implements Populator<SearchQueryData, FhSearchQueryData>
{
	private static final Logger LOG = Logger.getLogger(SearchQueryDecoderPopulator.class.getName());

	@Override
	public void populate(final SearchQueryData source, final FhSearchQueryData target)
	{
		if (source != null && source.getValue() != null && !source.getValue().isEmpty())
		{

			try
			{
				final String originalLocation = URLDecoder.decode(source.getValue(), "UTF-8");
				if (originalLocation.contains("fh_location"))
				{
					target.setLocation(originalLocation);
					final Query query = new Query(originalLocation);
					target.setFreeTextSearch(query.getSearchPhrase());
				}
				else
				{
					target.setFreeTextSearch(originalLocation);
				}
			}
			catch (final UnsupportedEncodingException e)
			{
				LOG.error("Error deconding location " + source.getValue(), e);
				throw new ConversionException("Error final deconding location " + source.getValue());
			}
		}

	}
}

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
package com.fredhopper.core.connector.index.generate.validator;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import com.fredhopper.core.connector.index.generate.data.FhAttributeBaseType;
import com.fredhopper.core.connector.index.generate.data.FhAttributeData;
import com.fredhopper.core.connector.index.report.Violation;
import com.google.common.collect.Table;


public class List64AttributeValidator extends ListAttributeValidator
{

	private static final int MAX_SIZE = 64;

	@Override
	public boolean supports(final FhAttributeData attribute)
	{
		return attribute.getBaseType().equals(FhAttributeBaseType.LIST64) ? true : false;
	}

	@Override
	protected boolean isValidSize(final FhAttributeData attribute, final List<Violation> violations)
	{
		final Table<Optional<String>, Optional<Locale>, String> values = attribute.getValues();
		if (values.isEmpty())
		{
			rejectValue(attribute, violations,
					"The \"list64\" attribute \"" + attribute.getAttributeId() + "\" has no values assigned.");
			return false;
		}
		if (values.rowKeySet().size() > MAX_SIZE)
		{
			rejectValue(attribute, violations,
					"The \"list64\" attribute \"" + attribute.getAttributeId() + "\" values total must not exceed 64.");
			return false;
		}
		return true;
	}
}

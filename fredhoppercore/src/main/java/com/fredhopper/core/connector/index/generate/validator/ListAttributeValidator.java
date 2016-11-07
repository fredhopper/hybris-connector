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
import java.util.Map;
import java.util.Optional;

import org.springframework.util.CollectionUtils;

import com.fredhopper.core.connector.index.generate.data.FhAttributeBaseType;
import com.fredhopper.core.connector.index.generate.data.FhAttributeData;
import com.fredhopper.core.connector.index.report.Violation;
import com.google.common.collect.Table;


public class ListAttributeValidator extends AbstractAttributeValidator
{

	private static final String VALUE_ID_PATTERN = "[a-z_]+";

	@Override
	public boolean supports(final FhAttributeData attribute)
	{
		return attribute.getBaseType().equals(FhAttributeBaseType.LIST) ? true : false;
	}

	@Override
	protected boolean isValidSize(final FhAttributeData attribute, final List<Violation> violations)
	{
		final Table<Optional<String>, Optional<Locale>, String> values = attribute.getValues();
		if (values.isEmpty())
		{
			rejectValue(attribute, violations, "The \"list\" attribute \"" + attribute.getAttributeId() + "\" has no values assigned.");
			return false;
		}
		return true;
	}

	@Override
	protected boolean hasValidValueId(final FhAttributeData attribute, final List<Violation> violations)
	{
		final Table<Optional<String>, Optional<Locale>, String> values = attribute.getValues();
		for (final Optional<String> optional : values.rowKeySet())
		{
			final String key = optional == null || !optional.isPresent() ? "_absent_" : optional.get();
			if (!key.matches(VALUE_ID_PATTERN))
			{
				rejectValue(attribute, violations,
						"The \"list\" attribute valueId key \"" + key + "\" does not match the appropriate pattern.");
				return false;
			}
		}
		return true;
	}

	@Override
	protected void validateValue(final FhAttributeData attribute, final List<Violation> violations)
	{
		final Table<Optional<String>, Optional<Locale>, String> values = attribute.getValues();
		for (final Optional<String> valueId : values.rowKeySet())
		{
			final Map<Optional<Locale>, String> valueMap = values.row(valueId);
			if (CollectionUtils.isEmpty(valueMap) || valueMap.keySet().size() != 1 || valueMap.containsKey(Optional.empty())
					|| valueMap.containsKey(null))
			{
				rejectValue(attribute, violations, "The \"list\" attribute value unique Locale key must be set.");
				return;
			}
			if (valueMap.containsValue(null) || valueMap.containsValue(""))
			{
				rejectValue(attribute, violations, "The \"list\" attribute value must not be blank.");
				return;
			}
		}
	}
}

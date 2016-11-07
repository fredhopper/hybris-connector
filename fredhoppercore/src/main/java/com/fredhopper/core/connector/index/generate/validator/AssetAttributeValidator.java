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
import java.util.Map.Entry;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import com.fredhopper.core.connector.index.generate.data.FhAttributeBaseType;
import com.fredhopper.core.connector.index.generate.data.FhAttributeData;
import com.fredhopper.core.connector.index.report.Violation;
import com.google.common.collect.Table;


public class AssetAttributeValidator extends AbstractAttributeValidator
{

	@Override
	public boolean supports(final FhAttributeData attribute)
	{
		return attribute.getBaseType().equals(FhAttributeBaseType.ASSET) ? true : false;
	}

	@Override
	protected boolean isValidSize(final FhAttributeData attribute, final List<Violation> violations)
	{
		final Table<Optional<String>, Optional<Locale>, String> values = attribute.getValues();
		if (values.isEmpty() || values.rowKeySet().size() != 1)
		{
			rejectValue(attribute, violations, "The \"asset\" attribute should cover one localized value.");
			return false;
		}
		return true;
	}

	@Override
	protected boolean hasValidValueId(final FhAttributeData attribute, final List<Violation> violations)
	{
		final Table<Optional<String>, Optional<Locale>, String> values = attribute.getValues();
		if (!values.containsRow(Optional.empty()))
		{
			rejectValue(attribute, violations, "The \"asset\" attribute value does not have an identifier.");
			return false;
		}
		return true;
	}

	@Override
	protected void validateValue(final FhAttributeData attribute, final List<Violation> violations)
	{
		final Table<Optional<String>, Optional<Locale>, String> values = attribute.getValues();
		final Map<Optional<Locale>, String> valueMap = values.row(Optional.empty());
		if (CollectionUtils.isEmpty(valueMap) || valueMap.containsKey(Optional.empty()) || valueMap.containsKey(null))
		{
			rejectValue(attribute, violations, "The \"asset\" value's Locale key must be set.");
			return;
		}
		for (final Entry<Optional<Locale>, String> entry : valueMap.entrySet())
		{
			if (StringUtils.isBlank(entry.getValue()))
			{
				rejectValue(attribute, violations, "The \"asset\" value must not be blank.");
			}
		}
	}

}

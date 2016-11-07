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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.fredhopper.core.connector.index.generate.data.FhMetaAttributeData;
import com.fredhopper.core.connector.index.report.Violation;
import com.google.common.base.Preconditions;


public class MetaAttributeDataValidator implements Validator<FhMetaAttributeData>
{

	private static final String ID_PATTERN = "[a-z0-9_]+";

	@Override
	public List<Violation> validate(final FhMetaAttributeData attribute)
	{

		Preconditions.checkArgument(attribute != null);
		final List<Violation> violations = new ArrayList<>();
		if (isValidAttributeId(attribute, violations) && hasAttributeBaseType(attribute, violations))
		{
			validateNames(attribute, violations);
		}
		return violations;
	}

	protected boolean isValidAttributeId(final FhMetaAttributeData attribute, final List<Violation> violations)
	{
		if (StringUtils.isBlank(attribute.getAttributeId()) || !attribute.getAttributeId().matches(ID_PATTERN))
		{
			rejectValue(attribute, violations,
					"The attribute identifier \"" + attribute.getAttributeId() + "\" does not match the appropriate pattern.");
			return false;
		}
		return true;
	}

	protected boolean hasAttributeBaseType(final FhMetaAttributeData attribute, final List<Violation> violations)
	{
		if (attribute.getBaseType() == null)
		{
			rejectValue(attribute, violations, "The attribute \"" + attribute.getAttributeId() + "\" has no FhAttributeBaseType set.");
			return false;
		}
		return true;
	}

	private void validateNames(final FhMetaAttributeData attribute, final List<Violation> violations)
	{
		final Map<Locale, String> names = attribute.getNames();
		if (names.keySet().isEmpty() || names.keySet().contains(null))
		{
			rejectValue(attribute, violations, "The attribute \"" + attribute.getAttributeId() + "\"'s name Locale key must be set.");
		}
		for (final String name : names.values())
		{
			if (StringUtils.isBlank(name))
			{
				rejectValue(attribute, violations, "The attribute \"" + attribute.getAttributeId() + "\"'s name must not be blank.");
			}
		}
	}

	protected void rejectValue(final FhMetaAttributeData attribute, final List<Violation> violations, final String message)
	{
		violations.add(new Violation(FhMetaAttributeData.class.toString(), attribute.getAttributeId(), message));
	}

}

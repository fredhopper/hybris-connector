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

import org.apache.commons.lang3.StringUtils;

import com.fredhopper.core.connector.index.generate.data.FhAttributeData;
import com.fredhopper.core.connector.index.report.Violation;
import com.google.common.base.Preconditions;


public abstract class AbstractAttributeValidator implements Validator<FhAttributeData>
{

	private static final String ID_PATTERN = "[a-z0-9_]+";

	@Override
	public List<Violation> validate(final FhAttributeData attribute)
	{

		Preconditions.checkArgument(attribute != null);
		final List<Violation> violations = new ArrayList<>();

		if (supports(attribute) && isValidAttributeId(attribute, violations) && isValidSize(attribute, violations)
				&& hasValidValueId(attribute, violations))
		{
			validateValue(attribute, violations);
		}
		return violations;

	}

	protected boolean isValidAttributeId(final FhAttributeData attribute, final List<Violation> violations)
	{
		if (StringUtils.isBlank(attribute.getAttributeId()) || !attribute.getAttributeId().matches(ID_PATTERN))
		{
			rejectValue(attribute, violations,
					"The identifier \"" + attribute.getAttributeId() + "\" does not match the appropriate pattern.");
			return false;
		}
		return true;
	}

	protected void rejectValue(final FhAttributeData attribute, final List<Violation> violations, final String message)
	{
		violations.add(
				new Violation(FhAttributeData.class.toString(), attribute.getItemId() + "_" + attribute.getAttributeId(), message));
	}

	public abstract boolean supports(final FhAttributeData attribute);

	protected abstract boolean isValidSize(final FhAttributeData attribute, final List<Violation> violations);

	protected abstract boolean hasValidValueId(final FhAttributeData attribute, final List<Violation> violations);

	protected abstract void validateValue(final FhAttributeData attribute, final List<Violation> violations);

}

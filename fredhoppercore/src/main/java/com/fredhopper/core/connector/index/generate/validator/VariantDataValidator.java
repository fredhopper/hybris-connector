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
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import com.fredhopper.core.connector.index.generate.data.FhVariantData;
import com.fredhopper.core.connector.index.report.Violation;
import com.google.common.base.Preconditions;


public class VariantDataValidator implements Validator<FhVariantData>
{

	private static final String ID_PATTERN = "[a-z0-9_]+";

	/**
	 * @param variant
	 */
	@Override
	public List<Violation> validate(final FhVariantData variant)
	{

		Preconditions.checkArgument(variant != null);
		final List<Violation> violations = new ArrayList<>();


		if (isValidItemId(variant, variant.getVariantId(), violations) && isValidItemId(variant, variant.getProductId(), violations))
		{
			checkLocales(variant, violations);
		}
		return violations;
	}

	protected boolean isValidItemId(final FhVariantData variant, final String identifier, final List<Violation> violations)
	{
		if (StringUtils.isBlank(identifier) || !identifier.matches(ID_PATTERN))
		{
			violations.add(new Violation(FhVariantData.class.toString(), variant.getProductId(),
					"The identifier \"" + identifier + "\" does not match the appropriate pattern."));
			return false;
		}
		return true;
	}

	protected void checkLocales(final FhVariantData variant, final List<Violation> violations)
	{
		final Set<Locale> locales = variant.getLocales();
		if (CollectionUtils.isEmpty(locales))
		{
			violations.add(new Violation(FhVariantData.class.toString(), variant.getProductId(),
					"The variant \"" + variant.getVariantId() + "\" is not assigned to any Locale."));
		}
	}

}

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
import org.springframework.util.CollectionUtils;

import com.fredhopper.core.connector.index.generate.data.FhCategoryData;
import com.fredhopper.core.connector.index.report.Violation;
import com.google.common.base.Preconditions;


public class CategoryDataValidator implements Validator<FhCategoryData>
{

	private static final String ID_PATTERN = "[a-z0-9_]+";

	/**
	 * @param category
	 */
	@Override
	public List<Violation> validate(final FhCategoryData category)
	{

		Preconditions.checkArgument(category != null);
		final List<Violation> violations = new ArrayList<>();


		if (isValidCategoryId(category, category.getCategoryId(), violations)
				&& isValidCategoryId(category, category.getParentId(), violations))
		{
			checkNames(category, violations);
		}
		return violations;
	}

	protected boolean isValidCategoryId(final FhCategoryData category, final String identifier, final List<Violation> violations)
	{
		if (StringUtils.isBlank(identifier) || !identifier.matches(ID_PATTERN))
		{
			violations.add(new Violation(FhCategoryData.class.toString(), category.getCategoryId(),
					"The identifier \"" + identifier + "\" does not match the appropriate pattern."));
			return false;
		}
		return true;
	}

	protected void checkNames(final FhCategoryData category, final List<Violation> violations)
	{
		final Map<Locale, String> names = category.getNames();
		if (CollectionUtils.isEmpty(names.keySet()) || names.keySet().contains(null))
		{
			violations.add(
					new Violation(FhCategoryData.class.toString(), category.getCategoryId(), "The category's Locale key must be set."));
		}
		for (final String name : names.values())
		{
			if (StringUtils.isBlank(name))
			{
				violations.add(
						new Violation(FhCategoryData.class.toString(), category.getCategoryId(), "The category name must not be blank."));
			}
		}
	}

}

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

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import com.fredhopper.core.connector.index.generate.data.FhProductData;
import com.fredhopper.core.connector.index.report.Violation;
import com.google.common.base.Preconditions;


public class ProductDataValidator implements Validator<FhProductData>
{

	private static final String ID_PATTERN = "[a-z0-9_]+";

	/**
	 * @param product
	 */
	@Override
	public List<Violation> validate(final FhProductData product)
	{
		Preconditions.checkArgument(product != null);
		final List<Violation> violations = new ArrayList<>();

		if (isValidProductId(product, violations))
		{
			if (CollectionUtils.isEmpty(product.getCategories()))
			{
				violations.add(new Violation(FhProductData.class.toString(), product.getProductId(),
						"The product is not assigned to any categories."));
			}
			else
			{
				hasValidCategoryIds(product, violations);
			}
		}
		return violations;
	}

	protected boolean isValidProductId(final FhProductData product, final List<Violation> violations)
	{
		if (StringUtils.isBlank(product.getProductId()) || !product.getProductId().matches(ID_PATTERN))
		{
			violations.add(new Violation(FhProductData.class.toString(), product.getProductId(),
					"The product Id \"" + product.getProductId() + "\" does not match the appropriate pattern."));
			return false;
		}
		return true;
	}

	protected boolean hasValidCategoryIds(final FhProductData product, final List<Violation> violations)
	{
		for (final Locale locale : product.getCategories().keySet())
		{
			for (final String categoryId : product.getCategories().get(locale))
			{
				if (StringUtils.isBlank(categoryId) || !categoryId.matches(ID_PATTERN))
				{
					violations.add(new Violation(FhProductData.class.toString(), product.getProductId(),
							"The category Id \"" + categoryId + "\" does not match the appropriate pattern."));
					return false;
				}
			}
		}
		return true;
	}

}

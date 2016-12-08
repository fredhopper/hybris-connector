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

public class SanitizeIdStrategy
{
	private String replacement;

	public String sanitizeId(final String identifier)
	{
		return identifier.replaceAll("[^a-z0-9_]", replacement);
	}

	public String sanitizeIdWithNumber(final String identifier)
	{
		final StringBuilder sb = new StringBuilder();
		for (int i = 0; i < identifier.length(); i++)
		{
			final char value = identifier.charAt(i);
			if (Character.isDigit(value))
			{
				sb.append(numberToString(value));
			}
			else
			{
				sb.append(String.valueOf(value).toLowerCase());
			}
		}
		return sb.toString().replaceAll("[^a-z_]", "_");
	}

	protected String numberToString(final char value)
	{
		final int caseValue = Character.getNumericValue(value);
		switch (caseValue)
		{
			case 0:
				return "zero";
			case 1:
				return "one";
			case 2:
				return "two";
			case 3:
				return "three";
			case 4:
				return "four";
			case 5:
				return "five";
			case 6:
				return "six";
			case 7:
				return "seven";
			case 8:
				return "eight";
			case 9:
				return "nine";
			default:
				return null;
		}
	}

	public String getReplacement()
	{
		return replacement;
	}

	public void setReplacement(final String replacement)
	{
		this.replacement = replacement;
	}
}


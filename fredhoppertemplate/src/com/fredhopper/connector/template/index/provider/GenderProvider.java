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
package com.fredhopper.connector.template.index.provider;

import de.hybris.platform.core.enums.Gender;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.yacceleratorcore.model.ApparelProductModel;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import com.fredhopper.connector.config.data.MetaAttributeData;
import com.fredhopper.connector.index.provider.AbstractAttributeProvider;
import com.google.common.collect.HashBasedTable;


/**
 *
 */
public class GenderProvider extends AbstractAttributeProvider
{
	@Override
	protected HashBasedTable<Optional<String>, Optional<Locale>, String> getAttributeValue(final Collection<Locale> locales,
			final ProductModel product, final MetaAttributeData metaAttribute)
	{
		final HashBasedTable<Optional<String>, Optional<Locale>, String> table = HashBasedTable.create();
		if (product instanceof ApparelProductModel)
		{
			final List<Gender> genders = ((ApparelProductModel) product).getGenders();

			if (genders != null && !genders.isEmpty())
			{
				for (final Gender gender : genders)
				{
					addValues(table, null, gender.getCode(), metaAttribute);
				}
			}
		}
		return table;
	}

}

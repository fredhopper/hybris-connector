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
package com.fredhopper.connector.index.dao;

import de.hybris.platform.core.model.product.ProductModel;

import java.util.List;


@FunctionalInterface
public interface FhProductDao
{
	/*
	 * Find items matching the parameters.
	 *
	 * @param start Specifies the number of rows to skip, before starting to return rows from the query expression.
	 *
	 * @param count Gives the number of rows that should be fetched from the database when more rows are needed.
	 */
	List<ProductModel> getItems(final int start, final int count);
}

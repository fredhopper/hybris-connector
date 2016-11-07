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
package com.fredhopper.connector.index.filter;



import de.hybris.platform.category.model.CategoryModel;

import java.util.Collection;
import java.util.List;


@FunctionalInterface
public interface CategoryFilterStrategy
{

	/**
	 * Filter out classification categories and categories which aren't members of root tree
	 *
	 * @return list of categoryModels without classificationClassModels
	 */
	public List<CategoryModel> filterCategories(final Collection<CategoryModel> categories, final Collection<CategoryModel> roots);

}
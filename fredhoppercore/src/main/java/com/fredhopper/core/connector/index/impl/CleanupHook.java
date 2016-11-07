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
package com.fredhopper.core.connector.index.impl;

import com.fredhopper.core.connector.index.IndexingHook;
import com.fredhopper.core.connector.index.generate.context.IndexingContext;


/**
 *
 */
public class CleanupHook implements IndexingHook
{

	@Override
	public void execute(final IndexingContext contex)
	{
		if (contex.getDataZip().isPresent())
		{
			contex.getDataZip().get().delete();
		}
		contex.getParentDir().delete();
	}

}

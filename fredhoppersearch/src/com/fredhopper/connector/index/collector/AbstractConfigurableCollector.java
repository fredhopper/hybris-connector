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
package com.fredhopper.connector.index.collector;



import com.fredhopper.connector.config.IndexConfigService;
import com.fredhopper.connector.config.data.IndexConfig;


public class AbstractConfigurableCollector
{

	protected String indexCode;
	protected IndexConfigService indexConfigService;

	/**
	 *
	 */
	public AbstractConfigurableCollector()
	{
		super();
	}

	public IndexConfigService getIndexConfigService()
	{
		return indexConfigService;
	}

	public void setIndexConfigService(final IndexConfigService indexConfigService)
	{
		this.indexConfigService = indexConfigService;
	}

	public String getIndexCode()
	{
		return indexCode;
	}

	public void setIndexCode(final String indexCode)
	{
		this.indexCode = indexCode;
	}

	protected IndexConfig getIndexConfig()
	{
		return indexConfigService.getIndexConfig(indexCode);
	}

}
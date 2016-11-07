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
package com.fredhopper.connector.query.data;

import com.fredhopper.webservice.client.Page;


/**
 *
 */
public class FhSearchResponse
{
	private FhSearchQueryData request;
	private Page page;

	public Page getPage()
	{
		return page;
	}

	public void setPage(final Page page)
	{
		this.page = page;
	}

	public FhSearchQueryData getRequest()
	{
		return request;
	}

	public void setRequest(final FhSearchQueryData request)
	{
		this.request = request;
	}
}

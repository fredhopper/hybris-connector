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
package com.fredhopper.core.connector.query.service;

import javax.xml.ws.WebServiceException;

import org.apache.log4j.Logger;

import com.fredhopper.core.connector.query.FhQueryService;
import com.fredhopper.webservice.client.FASWebService;
import com.fredhopper.webservice.client.Page;


public class DefaultFhQueryService implements FhQueryService
{

	private static final Logger LOG = Logger.getLogger(DefaultFhQueryService.class.getName());
	private int maxRetries;
	private FASWebService fasWebService;


	@Override
	public Page execute(final String query)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("Query : " + query);
		}

		int retries = 0;
		while (retries <= maxRetries)
		{
			try
			{
				return getFasWebService().getAll(query);
			}
			catch (final WebServiceException e)
			{
				LOG.error("Fredhopper WebService Exception: " + e.getMessage(), e);
			}
			try
			{
				Thread.sleep(100);
			}
			catch (final InterruptedException ie)
			{
				LOG.error("sleep interrupted.. " + ie.getMessage(), ie);
				Thread.currentThread().interrupt();
			}
			retries++;
		}

		LOG.error("Fredhopper query failed: " + query);

		throw new WebServiceException("Max number of retries reached without succesful response");

	}


	public int getMaxRetries()
	{
		return maxRetries;
	}

	public void setMaxRetries(final int maxRetries)
	{
		this.maxRetries = maxRetries;
	}

	public void setFasWebService(final FASWebService fasWebService)
	{
		this.fasWebService = fasWebService;
	}


	public FASWebService getFasWebService()
	{
		return fasWebService;
	}
}

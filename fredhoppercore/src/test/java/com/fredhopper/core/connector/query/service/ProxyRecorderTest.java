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

import org.junit.Ignore;
import org.junit.Rule;
import org.mockserver.client.proxy.ProxyClient;
import org.mockserver.junit.ProxyRule;

import com.fredhopper.lang.query.Query;
import com.fredhopper.webservice.client.FASWebService;
import com.fredhopper.webservice.client.Page;


/**
 * This class is a utility class, which can be used to record request and responses to Fredhopper.
 *
 */
public class ProxyRecorderTest
{

	@Rule
	public ProxyRule proxyRule = new ProxyRule(new Integer(1080), this);

	//private ProxyClient proxyClient;


	protected FASWebService getFasWebservice()
	{
		final DefaultFasWebserviceFactory fasWebserviceFactory = new DefaultFasWebserviceFactory();

		fasWebserviceFactory.setServicePassword("your_password");
		fasWebserviceFactory.setServiceUsername("example5");
		fasWebserviceFactory.setServiceUrl("http://localhost:1080/fredhopper-ws/services/FASWebService");
		return fasWebserviceFactory.getObject();
	}

	@Ignore
	public void testUrl() throws Exception
	{
		final ProxyClient proxyClient = new ProxyClient("localhost", 1080);
		proxyClient.dumpToLogAsJava();
		final DefaultFhQueryService queryService = new DefaultFhQueryService();
		queryService.setMaxRetries(3);
		queryService.setFasWebService(getFasWebservice());


		final Query query = new Query("fh_location=//catalog01/en_GB");

		final Page page = queryService.execute(query.toString());
		proxyClient.dumpToLogAsJava();
		System.out.println(page);
	}


}

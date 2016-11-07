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

import java.net.URL;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.FactoryBean;

import com.fredhopper.webservice.client.FASWebService;
import com.fredhopper.webservice.client.FASWebServiceService;


public class DefaultFasWebserviceFactory implements FactoryBean<FASWebService>
{

	private static final Logger LOG = Logger.getLogger(DefaultFasWebserviceFactory.class.getName());

	private static final String FH_NAMESPACE = "http://ns.fredhopper.com/XML/output/6.1.0";
	private static final String FH_SERVICE_NAME = "FASWebServiceService";
	public static final QName FH_SERVICE_QNAME = new QName(FH_NAMESPACE, FH_SERVICE_NAME);

	private String serviceUrl;
	private String serviceUsername;
	private String servicePassword;


	@Override
	public FASWebService getObject()
	{
		return createFasWebService();

	}

	@Override
	public Class<FASWebService> getObjectType()
	{
		return FASWebService.class;
	}


	private FASWebService createFasWebService()
	{
		final URL wsdlURL = getFredhopperWsdlUrl();
		final FASWebServiceService serviceService = new FASWebServiceService(wsdlURL, FH_SERVICE_QNAME);
		final FASWebService fasService = serviceService.getFASWebService();

		final Map<String, Object> rc = ((BindingProvider) fasService).getRequestContext();
		rc.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, serviceUrl);
		rc.put(BindingProvider.USERNAME_PROPERTY, serviceUsername);
		rc.put(BindingProvider.PASSWORD_PROPERTY, servicePassword);
		LOG.info("Created FASWebService for url:" + serviceUrl + " serviceUsername:" + serviceUsername);
		return fasService;
	}

	@Override
	public boolean isSingleton()
	{
		return true;
	}


	protected URL getFredhopperWsdlUrl()
	{
		return this.getClass().getResource("/FASWebService.wsdl");
	}

	public String getServiceUrl()
	{
		return serviceUrl;
	}

	public void setServiceUrl(final String serviceUrl)
	{
		this.serviceUrl = serviceUrl;
	}

	public String getServiceUsername()
	{
		return serviceUsername;
	}

	public void setServiceUsername(final String serviceUsername)
	{
		this.serviceUsername = serviceUsername;
	}

	public String getServicePassword()
	{
		return servicePassword;
	}

	public void setServicePassword(final String servicePassword)
	{
		this.servicePassword = servicePassword;
	}


}

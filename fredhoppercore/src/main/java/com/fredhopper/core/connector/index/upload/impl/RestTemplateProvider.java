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
package com.fredhopper.core.connector.index.upload.impl;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import com.google.common.base.Preconditions;


/**
 * Factory class for setting up a pre-configured RestTemplate
 */
public class RestTemplateProvider
{

	public RestTemplate createTemplate(final String host, final Integer port, final String username, final String password)
	{
		Preconditions.checkArgument(StringUtils.isNotBlank(host));
		Preconditions.checkArgument(port != null);
		Preconditions.checkArgument(StringUtils.isNotBlank(username));
		Preconditions.checkArgument(StringUtils.isNotBlank(password));

		final AuthScope authscope = new AuthScope(host, port.intValue());
		final Credentials credentials = new UsernamePasswordCredentials(username, password);
		final BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
		credentialsProvider.setCredentials(authscope, credentials);

		final HttpClientBuilder clientBuilder = HttpClientBuilder.create();
		clientBuilder.setDefaultCredentialsProvider(credentialsProvider);
		final CloseableHttpClient httpClient = clientBuilder.build();

		final HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
		requestFactory.setHttpClient(httpClient);

		return new RestTemplate(requestFactory);
	}
}

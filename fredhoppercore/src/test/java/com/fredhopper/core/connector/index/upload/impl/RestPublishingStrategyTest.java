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

import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withCreatedEntity;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import com.fredhopper.core.connector.config.InstanceConfig;
import com.fredhopper.core.connector.index.generate.exception.ResponseStatusException;


/**
 * JUnit Tests for the RestPublishingStrategy
 */
public class RestPublishingStrategyTest
{

	private RestPublishingStrategy strategy;
	private InstanceConfig config;
	private MockRestServiceServer mockServer;

	@Before
	public void setUp()
	{

		config = new InstanceConfig("https", "my.eu1.fredhopperservices.com", Integer.valueOf(443), "fas:test1", "user", "pwd");
		final RestTemplateProvider restTemplateProvider = new RestTemplateProvider();
		final RestTemplate template = restTemplateProvider.createTemplate(config.getHost(), config.getPort(), config.getUsername(),
				config.getPassword());
		mockServer = MockRestServiceServer.createServer(template);
		final RestTemplateProvider restTemplateProviderMock = Mockito.mock(RestTemplateProvider.class);
		Mockito.when(restTemplateProviderMock.createTemplate(config.getHost(), config.getPort(), config.getUsername(),
				config.getPassword())).thenReturn(template);
		strategy = new RestPublishingStrategy();
		strategy.setRestTemplateProvider(restTemplateProviderMock);
	}

	@Test
	public void testUploadDataSet() throws IOException, URISyntaxException, ResponseStatusException
	{

		final String data_id = "data-id=00987123";
		mockServer
				.expect(requestTo(
						"https://my.eu1.fredhopperservices.com:443/fas:test1/data/input/data.zip?checksum=ecd92758bc155e9bcd73bff5cb847687"))
				.andExpect(method(HttpMethod.PUT)).andRespond(withCreatedEntity(
						new URI("https://my.eu1.fredhopperservices.com:443/fas:test1/data/input/2001-01-01_21-12-21/")).body(data_id));

		final File file = new File(getClass().getResource("/data-test.zip").getFile());
		final String response = strategy.uploadDataSet(config, file);
		Assert.assertEquals(data_id, response);
	}

	@Test
	public void testTriggerDataLoad() throws IOException, URISyntaxException, ResponseStatusException
	{

		final URI location = new URI("https://my.eu1.fredhopperservices.com:443/fas:test1/trigger/load-data/2001-01-01_21-12-21");
		mockServer.expect(requestTo("https://my.eu1.fredhopperservices.com:443/fas:test1/trigger/load-data"))
				.andExpect(method(HttpMethod.PUT)).andRespond(withCreatedEntity(location));

		final URI response = strategy.triggerDataLoad(config, "data-id=00987123");
		Assert.assertEquals(location, response);
	}

	@Test
	public void testCheckStatus() throws IOException, URISyntaxException, ResponseStatusException
	{

		final URI location = new URI("https://my.eu1.fredhopperservices.com:443/fas:test1/trigger/load-data/2001-01-01_21-12-21");
		mockServer
				.expect(requestTo("https://my.eu1.fredhopperservices.com:443/fas:test1/trigger/load-data/2001-01-01_21-12-21/status"))
				.andExpect(method(HttpMethod.GET)).andRespond(withSuccess("SUCCESS", MediaType.TEXT_PLAIN));

		final String response = strategy.checkStatus(config, location);
		Assert.assertTrue(response.contains("SUCCESS"));
	}
}

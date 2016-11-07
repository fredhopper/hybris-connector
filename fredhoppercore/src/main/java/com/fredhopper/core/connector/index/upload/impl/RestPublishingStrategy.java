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

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fredhopper.core.connector.config.InstanceConfig;
import com.fredhopper.core.connector.index.generate.exception.ResponseStatusException;
import com.fredhopper.core.connector.index.upload.PublishingStrategy;
import com.google.common.base.Preconditions;
import com.google.common.io.Files;


/**
 *
 */
public class RestPublishingStrategy implements PublishingStrategy
{

	private static final String UPLOAD_PATH = "/data/input/data.zip";
	private static final String TRIGGER_PATH = "/trigger/load-data";
	private static final String STATUS_PATH = "/status";

	private static final Logger LOG = Logger.getLogger(RestPublishingStrategy.class);
	private RestTemplateProvider restTemplateProvider;

	@Override
	public String uploadDataSet(final InstanceConfig config, final File file) throws ResponseStatusException
	{

		Preconditions.checkArgument(config != null);
		Preconditions.checkArgument(file != null);

		final RestTemplate restTemplate = restTemplateProvider.createTemplate(config.getHost(), config.getPort(),
				config.getUsername(), config.getPassword());
		final String checkSum = createCheckSum(file);

		final List<NameValuePair> params = Arrays.asList(new NameValuePair[]
		{ new BasicNameValuePair("checksum", checkSum) });
		final URI url = createUri(config.getScheme(), config.getHost(), config.getPort(), config.getServername(), UPLOAD_PATH,
				params);

		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.valueOf("application/zip"));
		final HttpEntity<Resource> httpEntity = new HttpEntity<>(new FileSystemResource(file), headers);

		final ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, httpEntity, String.class);

		final HttpStatus status = response.getStatusCode();
		if (status.equals(HttpStatus.CREATED))
		{
			return response.getBody().trim();
		}
		else
		{
			throw new ResponseStatusException("HttpStatus " + status.toString() + " response received. File upload failed.");
		}
	}

	@Override
	public URI triggerDataLoad(final InstanceConfig config, final String data_id) throws ResponseStatusException
	{

		Preconditions.checkArgument(config != null);
		Preconditions.checkArgument(StringUtils.isNotBlank(data_id));

		final RestTemplate restTemplate = restTemplateProvider.createTemplate(config.getHost(), config.getPort(),
				config.getUsername(), config.getPassword());

		final URI url = createUri(config.getScheme(), config.getHost(), config.getPort(), config.getServername(), TRIGGER_PATH,
				Collections.emptyList());

		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.TEXT_PLAIN);
		final HttpEntity<String> httpEntity = new HttpEntity<>(data_id, headers);

		final ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, httpEntity, String.class);
		final HttpStatus status = response.getStatusCode();
		if (status.equals(HttpStatus.CREATED))
		{
			return response.getHeaders().getLocation();
		}
		else
		{
			throw new ResponseStatusException(
					"HttpStatus " + status.toString() + " response received. Load trigger creation failed.");
		}
	}

	@Override
	public String checkStatus(final InstanceConfig config, final URI trigger_url) throws ResponseStatusException
	{

		Preconditions.checkArgument(config != null);
		Preconditions.checkArgument(trigger_url != null);

		final RestTemplate restTemplate = restTemplateProvider.createTemplate(config.getHost(), config.getPort(),
				config.getUsername(), config.getPassword());
		final URI url = trigger_url.resolve(trigger_url.getPath() + STATUS_PATH);

		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.TEXT_PLAIN);
		final HttpEntity<String> httpEntity = new HttpEntity<>(headers);

		final ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
		final HttpStatus status = response.getStatusCode();
		if (status.equals(HttpStatus.OK))
		{
			return response.getBody();
		}
		else
		{
			throw new ResponseStatusException(
					"HttpStatus " + status.toString() + " response received. Load trigger monitoring failed.");
		}
	}

	protected String createCheckSum(final File file)
	{

		Preconditions.checkArgument(file != null);

		try
		{
			return DigestUtils.md5Hex(Files.toByteArray(file));
		}
		catch (final IOException ex)
		{
			LOG.error("Failed to create checksum.", ex);
			throw new UncheckedIOException(ex);
		}
	}

	protected URI createUri(final String scheme, final String host, final Integer port, final String servername, final String path,
			final List<NameValuePair> params)
	{

		Preconditions.checkArgument(StringUtils.isNotBlank(scheme));
		Preconditions.checkArgument(StringUtils.isNotBlank(host));
		Preconditions.checkArgument(port != null);
		Preconditions.checkArgument(StringUtils.isNotBlank(servername));
		Preconditions.checkArgument(StringUtils.isNotBlank(path));

		final URIBuilder uriBuilder = new URIBuilder();
		uriBuilder.setScheme(scheme);
		uriBuilder.setHost(host);
		uriBuilder.setPort(port.intValue());
		uriBuilder.setPath("/".concat(servername).concat(path));
		if (CollectionUtils.isNotEmpty(params))
		{
			uriBuilder.setParameters(params);
		}
		try
		{
			return uriBuilder.build();
		}
		catch (final URISyntaxException ex)
		{
			throw new IllegalArgumentException(ex);
		}
	}

	public RestTemplateProvider getRestTemplateProvider()
	{
		return restTemplateProvider;
	}

	public void setRestTemplateProvider(final RestTemplateProvider restTemplateProvider)
	{
		this.restTemplateProvider = restTemplateProvider;
	}

}

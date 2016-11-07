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
import java.net.URI;

import org.apache.log4j.Logger;

import com.fredhopper.core.connector.config.InstanceConfig;
import com.fredhopper.core.connector.index.generate.exception.ResponseStatusException;
import com.fredhopper.core.connector.index.upload.PublishingService;
import com.fredhopper.core.connector.index.upload.PublishingStrategy;


public class DefaultPublishingService implements PublishingService
{

	private PublishingStrategy publishingStrategy;

	private static final Logger LOG = Logger.getLogger(DefaultPublishingService.class);

	@Override
	public boolean publishZip(final InstanceConfig config, final File file)
	{

		try
		{
			LOG.info("Uploading zip file to Fredhopper");
			final String data_id = publishingStrategy.uploadDataSet(config, file);
			LOG.info("Triggering an index update '" + data_id + "'");
			final URI trigger_url = publishingStrategy.triggerDataLoad(config, data_id);
			LOG.info("Checking index update status...");
			String status = publishingStrategy.checkStatus(config, trigger_url);
			while (!status.contains("SUCCESS") && !status.contains("FAILURE"))
			{
				try
				{
					Thread.sleep(20000);
					status = publishingStrategy.checkStatus(config, trigger_url);
				}
				catch (final InterruptedException ex)
				{
					LOG.error("The status check thread was interrupted while sleeping.", ex);
					Thread.currentThread().interrupt();
				}
			}
			if (status.contains("SUCCESS"))
			{
				LOG.info("Index updated successfully.");
				return true;
			}
			else
			{
				LOG.error(status);
			}
		}
		catch (final ResponseStatusException ex)
		{
			LOG.error(ex.getMessage());
		}
		return false;
	}

	public PublishingStrategy getPublishingStrategy()
	{
		return publishingStrategy;
	}

	public void setPublishingStrategy(final PublishingStrategy publishingStrategy)
	{
		this.publishingStrategy = publishingStrategy;
	}

}

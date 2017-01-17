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
package com.fredhopper.core.connector.index.upload;

import java.io.File;
import java.net.URI;

import com.fredhopper.core.connector.config.InstanceConfig;
import com.fredhopper.core.connector.index.generate.exception.ResponseStatusException;


public interface PublishingStrategy
{

	/**
	 * Upload a new data.zip, and assign a dataId
	 *
	 * @param config
	 *           DTO containing connection parameters
	 * @param file
	 *           the data.zip file to upload
	 * @return the assigned identifier
	 * @throws ResponseStatusException
	 */
	String uploadDataSet(final InstanceConfig config, final File file) throws ResponseStatusException;

	/**
	 * Create new data-load trigger.
	 *
	 * @param config
	 *           DTO containing connection parameters
	 * @param dataId
	 *           the identifier of the uploaded data file
	 * @return the URI where the triggered job can be monitored
	 * @throws ResponseStatusException
	 */
	URI triggerDataLoad(final InstanceConfig config, final String dataId) throws ResponseStatusException;

	/**
	 * Get the status of a triggered data-load operation.
	 *
	 * @param config
	 *           DTO containing connection parameters
	 * @param location
	 *           the URI returned when the data-load job was triggered
	 * @return the status
	 * @throws ResponseStatusException
	 */
	String checkStatus(final InstanceConfig config, final URI location) throws ResponseStatusException;

}
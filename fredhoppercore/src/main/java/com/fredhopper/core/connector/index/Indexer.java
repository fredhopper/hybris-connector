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
package com.fredhopper.core.connector.index;

import java.io.IOException;


public interface Indexer
{
	/**
	 * Generate and send data to Fredhopper. Violations will not stops the upload.
	 *
	 * @param indexConfig
	 *           Configuration
	 */
	public void index(final String indexConfig) throws IOException;

	/**
	 * Generate and send data to Fredhopper. Violations will not stops the upload.
	 *
	 * @param indexConfig
	 *           Configuration
	 * @param upload
	 *           if true, uploads the generated data to Fredhopper for indexing.
	 */
	public void index(final String indexConfig, final boolean upload) throws IOException;

	/**
	 * Generate and send data to Fredhopper. Violations will not stops the upload.
	 *
	 * @param indexConfig
	 *           Configuration
	 * @param upload
	 *           if true, uploads the generated data to Fredhopper for indexing.
	 * @param maxNumberViolatios
	 *           if -1 the upload is always executed, otherwise the upload is not be executed if during the file
	 *           generation number of Violations exceeds the max amount allowed.
	 */
	public void index(final String indexConfig, final boolean upload, final int maxNumberViolatios) throws IOException;
}

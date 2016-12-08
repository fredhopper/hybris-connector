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
package com.fredhopper.core.connector.index.generate.context;

import java.io.File;
import java.util.Optional;

import com.fredhopper.core.connector.index.report.Statistics;


public class IndexingContext
{
	private final Statistics statistics;
	private final File parentDir;
	private Optional<File> dataZip;
	private IndexingResult result;

	/**
	 *
	 */
	public IndexingContext(final Statistics statistics, final File parentDir)
	{
		super();
		this.statistics = statistics;
		this.parentDir = parentDir;
		this.dataZip = Optional.empty();
	}

	public Statistics getStatistics()
	{
		return statistics;
	}

	public File getParentDir()
	{
		return parentDir;
	}

	public Optional<File> getDataZip()
	{
		return dataZip;
	}

	public void setDataZip(final Optional<File> dataZip)
	{
		this.dataZip = dataZip;
	}

	public IndexingResult getResult()
	{
		return result;
	}

	public void setResult(final IndexingResult result)
	{
		this.result = result;
	}



}

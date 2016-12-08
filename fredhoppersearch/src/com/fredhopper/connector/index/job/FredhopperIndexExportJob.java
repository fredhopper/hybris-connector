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
package com.fredhopper.connector.index.job;

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.fredhopper.core.connector.index.Indexer;
import com.fredhopper.model.export.cron.FredhopperIndexExportCronJobModel;


/**
 * Job responsible for gathering, converting and exporting data to a Fredhopper instance
 */
public class FredhopperIndexExportJob extends AbstractJobPerformable<FredhopperIndexExportCronJobModel>
{
	private static final Logger LOG = Logger.getLogger(FredhopperIndexExportJob.class);

	private Indexer fhIndexer;

	@Override
	public PerformResult perform(final FredhopperIndexExportCronJobModel cronJob)
	{
		final String indexCode = cronJob.getIndexConfig().getCode();
		try
		{
			fhIndexer.index(indexCode, cronJob.isUpload());
		}
		catch (final IOException ex)
		{
			LOG.error("IOException caught while executing index with code \"" + indexCode + "\"", ex);
			return new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
		}
		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
	}

	public Indexer getFhIndexer()
	{
		return fhIndexer;
	}

	public void setFhIndexer(final Indexer fhIndexer)
	{
		this.fhIndexer = fhIndexer;
	}

}
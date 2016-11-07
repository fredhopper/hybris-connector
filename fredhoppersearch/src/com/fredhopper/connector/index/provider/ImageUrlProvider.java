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
package com.fredhopper.connector.index.provider;

import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaFormatModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.media.MediaContainerService;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.variants.model.VariantProductModel;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.apache.log4j.Logger;

import com.fredhopper.connector.config.data.MetaAttributeData;
import com.google.common.collect.HashBasedTable;


/**
 *
 */
public class ImageUrlProvider extends AbstractAttributeProvider
{
	private static final Logger LOG = Logger.getLogger(ImageUrlProvider.class);

	private String mediaFormat;
	private MediaService mediaService;
	private MediaContainerService mediaContainerService;

	@Override
	protected HashBasedTable<Optional<String>, Optional<Locale>, String> getAttributeValue(final Collection<Locale> locales,
			final ProductModel product, final MetaAttributeData metaAttribute)
	{
		final HashBasedTable<Optional<String>, Optional<Locale>, String> table = HashBasedTable.create();

		final MediaFormatModel mediaFormatModel = getMediaService().getFormat(getMediaFormat());
		if (mediaFormatModel != null)
		{
			final MediaModel media = findMedia(product, mediaFormatModel);
			if (media != null)
			{
				addValues(table, null, media.getURL(), metaAttribute);
			}

		}

		return table;
	}

	protected MediaModel findMedia(final ProductModel product, final MediaFormatModel mediaFormat)
	{
		if (product != null && mediaFormat != null)
		{
			final List<MediaContainerModel> galleryImages = product.getGalleryImages();
			if (galleryImages != null && !galleryImages.isEmpty())
			{
				// Search each media container in the gallery for an image of the right format
				for (final MediaContainerModel container : galleryImages)
				{
					try
					{
						final MediaModel media = getMediaContainerService().getMediaForFormat(container, mediaFormat);
						if (media != null)
						{
							return media;
						}
					}
					catch (final ModelNotFoundException ex)
					{
						LOG.warn("MediaFormat \"" + mediaFormat.getName() + "\" not found for product \"" + product.getCode() + "\"",
								ex);
					}
				}
			}

			// Failed to find media in product
			if (product instanceof VariantProductModel)
			{
				// Look in the base product
				return findMedia(((VariantProductModel) product).getBaseProduct(), mediaFormat);
			}
		}
		return null;
	}



	/**
	 * @return the mediaFormat
	 */
	public String getMediaFormat()
	{
		return mediaFormat;
	}

	/**
	 * @param mediaFormat
	 *           the mediaFormat to set
	 */
	public void setMediaFormat(final String mediaFormat)
	{
		this.mediaFormat = mediaFormat;
	}

	/**
	 * @return the mediaService
	 */
	public MediaService getMediaService()
	{
		return mediaService;
	}

	/**
	 * @param mediaService
	 *           the mediaService to set
	 */
	public void setMediaService(final MediaService mediaService)
	{
		this.mediaService = mediaService;
	}

	/**
	 * @return the mediaContainerService
	 */
	public MediaContainerService getMediaContainerService()
	{
		return mediaContainerService;
	}

	/**
	 * @param mediaContainerService
	 *           the mediaContainerService to set
	 */
	public void setMediaContainerService(final MediaContainerService mediaContainerService)
	{
		this.mediaContainerService = mediaContainerService;
	}

}

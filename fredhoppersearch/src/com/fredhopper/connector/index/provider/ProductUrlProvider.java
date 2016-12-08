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

import de.hybris.platform.commerceservices.url.UrlResolver;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.Collection;
import java.util.Locale;
import java.util.Optional;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

import com.fredhopper.connector.config.data.MetaAttributeData;
import com.google.common.collect.HashBasedTable;


/**
 * Default {@link AttributeProvider} implementation for the URL path for {@link ProductModel}
 */
public class ProductUrlProvider extends AbstractAttributeProvider
{
	private I18NService i18nService;
	private SessionService sessionService;
	private UrlResolver<ProductModel> urlResolver;



	@Override
	protected HashBasedTable<Optional<String>, Optional<Locale>, String> getAttributeValue(final Collection<Locale> locales,
			final ProductModel product, final MetaAttributeData metaAttribute)
	{
		final HashBasedTable<Optional<String>, Optional<Locale>, String> table = HashBasedTable.create();
		if (CollectionUtils.isNotEmpty(locales) && isLocalizedAttribute(metaAttribute))
		{
			for (final Locale locale : locales)
			{
				final Object value = getSessionService().executeInLocalView(new SessionExecutionBody()
				{
					@Override
					public Object execute()
					{
						getI18nService().setCurrentLocale(locale);
						return getUrlResolver().resolve(product);
					}
				});
				addValues(table, locale, value, metaAttribute);
			}
		}
		else
		{
			final String value = getUrlResolver().resolve(product);
			addValues(table, null, value, metaAttribute);
		}

		return table;
	}


	public SessionService getSessionService()
	{
		return sessionService;
	}

	@Required
	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}

	public UrlResolver<ProductModel> getUrlResolver()
	{
		return urlResolver;
	}

	@Required
	public void setUrlResolver(final UrlResolver<ProductModel> urlResolver)
	{
		this.urlResolver = urlResolver;
	}

	public I18NService getI18nService()
	{
		return i18nService;
	}

	@Required
	public void setI18nService(final I18NService i18nService)
	{
		this.i18nService = i18nService;
	}

}

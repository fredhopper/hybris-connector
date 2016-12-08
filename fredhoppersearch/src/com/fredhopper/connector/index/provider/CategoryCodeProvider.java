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

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.Collection;
import java.util.Locale;
import java.util.Optional;

import org.apache.commons.collections.CollectionUtils;

import com.fredhopper.connector.config.data.MetaAttributeData;
import com.google.common.collect.HashBasedTable;


/**
 * Default {@link AttributeProvider} implementation for {@link CategoryModel}.code
 */
public class CategoryCodeProvider extends AbstractAttributeProvider
{
	private FhCategorySource categorySource;
	private I18NService i18nService;
	private ModelService modelService;
	private SessionService sessionService;

	@Override
	protected HashBasedTable<Optional<String>, Optional<Locale>, String> getAttributeValue(final Collection<Locale> locales,
			final ProductModel product, final MetaAttributeData metaAttribute)
	{
		final HashBasedTable<Optional<String>, Optional<Locale>, String> table = HashBasedTable.create();

		final Collection<CategoryModel> categories = getCategorySource().findCategoriesForProduct(product);
		if (CollectionUtils.isNotEmpty(locales) && isLocalizedAttribute(metaAttribute))
		{
			for (final Locale locale : locales)
			{
				getSessionService().executeInLocalView(new SessionExecutionBody()
				{
					@Override
					public void executeWithoutResult()
					{
						getI18nService().setCurrentLocale(locale);
						for (final CategoryModel category : categories)
						{
							addValues(table, locale, getPropertyValue(category), metaAttribute);
						}
					}
				});
			}
		}
		else
		{
			for (final CategoryModel category : categories)
			{
				addValues(table, null, getPropertyValue(category), metaAttribute);
			}
		}
		return table;
	}

	protected Object getPropertyValue(final Object model)
	{
		return getPropertyValue(model, "code");
	}

	protected Object getPropertyValue(final Object model, final String propertyName)
	{
		return modelService.getAttributeValue(model, propertyName);
	}

	public FhCategorySource getCategorySource()
	{
		return categorySource;
	}

	public void setCategorySource(final FhCategorySource categorySource)
	{
		this.categorySource = categorySource;
	}


	public ModelService getModelService()
	{
		return modelService;
	}

	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	public SessionService getSessionService()
	{
		return sessionService;
	}

	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}

	public I18NService getI18nService()
	{
		return i18nService;
	}

	public void setI18nService(final I18NService i18nService)
	{
		this.i18nService = i18nService;
	}

}

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

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.product.VariantsService;
import de.hybris.platform.servicelayer.exceptions.AttributeNotSupportedException;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.variants.model.VariantAttributeDescriptorModel;
import de.hybris.platform.variants.model.VariantProductModel;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.fredhopper.connector.config.data.MetaAttributeData;
import com.google.common.collect.HashBasedTable;


/**
 * Default implementation of {@link AttributeProvider}
 */
public class SimpleAttributeProvider extends AbstractAttributeProvider
{
	private I18NService i18nService;
	private ModelService modelService;
	private SessionService sessionService;
	private TypeService typeService;
	private VariantsService variantsService;

	private static final Logger LOG = Logger.getLogger(SimpleAttributeProvider.class);

	@Override
	protected HashBasedTable<Optional<String>, Optional<Locale>, String> getAttributeValue(final Collection<Locale> locales,
			final ProductModel product, final MetaAttributeData metaAttribute)
	{
		final HashBasedTable<Optional<String>, Optional<Locale>, String> table = HashBasedTable.create();
		final String qualifier = metaAttribute.getQualifier();

		final ComposedTypeModel composedType = typeService.getComposedTypeForClass(product.getClass());

		if (typeService.hasAttribute(composedType, qualifier))
		{
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
							return getAttributeValue(product, qualifier);

						}
					});
					addValues(table, locale, value, metaAttribute);
				}
			}
			else
			{
				final Object value = modelService.getAttributeValue(product, qualifier);
				addValues(table, null, value, metaAttribute);

			}
		}
		return table;
	}

	protected Object getAttributeValue(final ProductModel product, final String qualifier)
	{
		Object result = null;
		try
		{
			result = getModelService().getAttributeValue(product, qualifier);
			if (result == null && product instanceof VariantProductModel)
			{
				final ProductModel baseProduct = ((VariantProductModel) product).getBaseProduct();
				result = getModelService().getAttributeValue(baseProduct, qualifier);
			}
		}
		catch (final AttributeNotSupportedException ex)
		{
			if (product instanceof VariantProductModel)
			{
				final ProductModel baseProduct = ((VariantProductModel) product).getBaseProduct();
				final List<VariantAttributeDescriptorModel> descriptors = getVariantsService()
						.getVariantAttributesForVariantType(baseProduct.getVariantType());
				if (CollectionUtils.isNotEmpty(descriptors))
				{
					final Optional<VariantAttributeDescriptorModel> optional = descriptors.stream()
							.filter(descriptor -> descriptor.getQualifier().equals(qualifier)).findAny();
					if (optional.isPresent())
					{
						result = getVariantsService().getVariantAttributeValue((VariantProductModel) product, qualifier);
					}
				}
			}
			else
			{
				LOG.error(ex.getMessage());
			}
		}
		return result;
	}


	public ModelService getModelService()
	{
		return modelService;
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
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

	public TypeService getTypeService()
	{
		return typeService;
	}

	@Required
	public void setTypeService(final TypeService typeService)
	{
		this.typeService = typeService;
	}

	public VariantsService getVariantsService()
	{
		return variantsService;
	}

	@Required
	public void setVariantsService(final VariantsService variantsService)
	{
		this.variantsService = variantsService;
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

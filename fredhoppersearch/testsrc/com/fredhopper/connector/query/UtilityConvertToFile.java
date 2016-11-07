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
package com.fredhopper.connector.query;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.product.data.CategoryData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.search.data.SearchStateData;
import de.hybris.platform.commerceservices.search.facetdata.ProductCategorySearchPageData;

import java.io.FileOutputStream;
import java.io.OutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.junit.Ignore;

import com.fredhopper.core.connector.query.service.DefaultFasWebserviceFactory;
import com.fredhopper.core.connector.query.service.DefaultFhQueryService;
import com.fredhopper.lang.query.Query;
import com.fredhopper.webservice.client.Breadcrumbs;
import com.fredhopper.webservice.client.FASWebService;
import com.fredhopper.webservice.client.Page;
import com.fredhopper.webservice.client.Universe;
import com.fredhopper.webservice.client.UniverseType;


/**
 *
 */
@UnitTest
public class UtilityConvertToFile
{

	private final String queryString = "fh_location=//catalog01/en_GB/$s=snowboard";
	private final String filename = "resources/fredhoppersearch/test/response-template/searchSnowboard2.xml";

	protected FASWebService getFasWebservice()
	{
		final DefaultFasWebserviceFactory fasWebserviceFactory = new DefaultFasWebserviceFactory();

		fasWebserviceFactory.setServicePassword("your_password");
		fasWebserviceFactory.setServiceUsername("example5");
		fasWebserviceFactory.setServiceUrl("http://localhost:1080/fredhopper-ws/services/FASWebService");
		return fasWebserviceFactory.getObject();
	}

	@Ignore
	public void testRetry() throws Exception
	{

		System.setProperty("javax.xml.bind.JAXBContext", "com.sun.xml.internal.bind.v2.ContextFactory");

		final DefaultFhQueryService queryService = new DefaultFhQueryService();
		queryService.setFasWebService(getFasWebservice());
		queryService.setMaxRetries(3);

		final Query query = new Query(queryString);



		final Page page = queryService.execute(query.toString());




		final JAXBContext jaxbContext = JAXBContext.newInstance(Page.class);
		final Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

		// output pretty printed
		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

		final OutputStream os = new FileOutputStream(filename);



		jaxbMarshaller.marshal(page, os);



		for (final Universe u : page.getUniverses().getUniverse())
		{
			if (UniverseType.SELECTED.equals(u.getType()))
			{
				final Breadcrumbs breadcrumbs = u.getBreadcrumbs();
				final int nrOfItemsInSelection = breadcrumbs.getNrOfItemsInSelection();
				System.out.println("Items in selection: " + nrOfItemsInSelection);

				// There is only one selected universe in a response, so we can break.
				break;
			}
		}

		final ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData> searchPageData = new ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData>();
		//Populate searchPageData from the Page

		System.out.println(searchPageData);

	}

}

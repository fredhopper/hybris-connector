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

import static org.junit.Assert.assertEquals;

import de.hybris.bootstrap.annotations.UnitTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.junit.Test;

import com.fredhopper.webservice.client.Page;


/**
 *
 */
@UnitTest
public class DeconvertJAXB
{



	@Test
	public void testRetry() throws Exception
	{

		System.setProperty("javax.xml.bind.JAXBContext", "com.sun.xml.internal.bind.v2.ContextFactory");








		final JAXBContext jaxbContext = JAXBContext.newInstance(Page.class);
		final Unmarshaller jaxbUnMarshaller = jaxbContext.createUnmarshaller();

		// output pretty printed
		//	jaxbUnMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);


		final File file = new File(
				this.getClass().getClassLoader().getResource("fredhoppersearch/test/response-template/rootCatalog.xml").getPath());

		final InputStream os = new FileInputStream(file);

		final Page page = (Page) jaxbUnMarshaller.unmarshal(os);

		assertEquals(2, page.getUniverses().getUniverse().size());


	}

}

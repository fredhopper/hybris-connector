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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.fredhopper.webservice.client.Page;


/**
 *
 */
@UnitTest
public abstract class AbstractQueryTest
{



	protected Page getPage(final String filePath)
	{
		try
		{
			System.setProperty("javax.xml.bind.JAXBContext", "com.sun.xml.internal.bind.v2.ContextFactory");
			final JAXBContext jaxbContext = JAXBContext.newInstance(Page.class);
			final Unmarshaller jaxbUnMarshaller = jaxbContext.createUnmarshaller();


			final File file = new File(this.getClass().getClassLoader().getResource(filePath).getPath());
			final InputStream os = new FileInputStream(file);
			return (Page) jaxbUnMarshaller.unmarshal(os);
		}
		catch (final FileNotFoundException e)
		{
			throw new RuntimeException(e);
		}
		catch (final JAXBException e)
		{
			throw new RuntimeException(e);
		}
	}





}

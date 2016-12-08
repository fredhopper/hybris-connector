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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.lang3.RandomStringUtils;


public class FileUtils
{

	private FileUtils()
	{
		//Utility classes, which are a collection of static members, are not meant to be instantiated.
	}

	public static void addToZipFile(final File file, final ZipOutputStream zos) throws IOException
	{

		final FileInputStream fis = new FileInputStream(file);
		final ZipEntry zipEntry = new ZipEntry(file.getName());
		zos.putNextEntry(zipEntry);

		final byte[] bytes = new byte[1024];
		int length;
		while ((length = fis.read(bytes)) >= 0)
		{
			zos.write(bytes, 0, length);
		}

		zos.closeEntry();
		fis.close();
	}

	/**
	 * Create a subdirectory of parentDir. The name is a random 15 character alphabetic string
	 *
	 * @param parentDir
	 * @throws IOException
	 */
	public static File createRandomDirectory(final File parentDir) throws IOException
	{

		final String tempDirName = RandomStringUtils.randomAlphabetic(15);
		final File tempDir = new File(parentDir.getAbsoluteFile() + File.separator + tempDirName);
		org.apache.commons.io.FileUtils.forceMkdir(tempDir);
		return tempDir;

	}
}

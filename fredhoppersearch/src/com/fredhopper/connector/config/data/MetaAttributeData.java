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

package com.fredhopper.connector.config.data;

import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;

import com.fredhopper.core.connector.index.generate.data.FhMetaAttributeData;


/**
 * Data transfer object for meta-attribute properties
 */
public class MetaAttributeData extends FhMetaAttributeData
{

	private String provider;

	private ClassAttributeAssignmentModel classAttributeAssignment;

	private String qualifier;

	public MetaAttributeData()
	{
		// default constructor
	}

	public void setProvider(final String provider)
	{
		this.provider = provider;
	}

	public String getProvider()
	{
		return provider;
	}

	public void setClassAttributeAssignment(final ClassAttributeAssignmentModel classAttributeAssignment)
	{
		this.classAttributeAssignment = classAttributeAssignment;
	}

	public ClassAttributeAssignmentModel getClassAttributeAssignment()
	{
		return classAttributeAssignment;
	}

	public String getQualifier()
	{
		return qualifier;
	}

	public void setQualifier(final String qualifier)
	{
		this.qualifier = qualifier;
	}

}
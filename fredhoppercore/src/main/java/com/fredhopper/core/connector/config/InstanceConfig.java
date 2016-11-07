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
package com.fredhopper.core.connector.config;

public class InstanceConfig
{

	private String scheme;
	private String host;
	private Integer port;
	private String servername;
	private String username;
	private String password;



	public InstanceConfig()
	{
		//Empty constructor
	}

	/**
	 * @param scheme
	 * @param host
	 * @param port
	 * @param servername
	 * @param username
	 * @param password
	 */
	public InstanceConfig(final String scheme, final String host, final Integer port, final String servername,
			final String username, final String password)
	{
		super();
		this.scheme = scheme;
		this.host = host;
		this.port = port;
		this.servername = servername;
		this.username = username;
		this.password = password;

	}

	public String getScheme()
	{
		return scheme;
	}

	public void setScheme(final String scheme)
	{
		this.scheme = scheme;
	}

	public String getHost()
	{
		return host;
	}

	public void setHost(final String host)
	{
		this.host = host;
	}

	public Integer getPort()
	{
		return port;
	}

	public void setPort(final Integer port)
	{
		this.port = port;
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(final String username)
	{
		this.username = username;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(final String password)
	{
		this.password = password;
	}

	public String getServername()
	{
		return servername;
	}

	public void setServername(final String servername)
	{
		this.servername = servername;
	}



}

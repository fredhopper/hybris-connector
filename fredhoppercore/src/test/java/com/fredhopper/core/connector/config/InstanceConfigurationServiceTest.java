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

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@ContextConfiguration(locations =
{ "classpath:/test-fredhoppercore-spring.xml", "classpath:/fredhoppercore-spring.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
/**
 * This test is only a demonstration of how an InstanceConfigService could be implemented via the
 * org.springframework.beans.factory.config.ServiceLocatorFactoryBean pattern
 */
public class InstanceConfigurationServiceTest
{

	@Resource
	private InstanceConfigService instanceConfigService;

	@Test
	public void test()
	{
		final InstanceConfig config = instanceConfigService.getInstageConfig("defaultInstanceConfig");
		Assert.assertNotNull(config);
		Assert.assertEquals(config, instanceConfigService.getInstageConfig("defaultInstanceConfig"));

	}

	@Test(expected = Exception.class)

	public void testWrongBean()
	{

		Assert.assertNull(instanceConfigService.getInstageConfig("wrongId"));
	}

}

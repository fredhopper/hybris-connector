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
package com.fredhopper.connector.index.collector;

import de.hybris.bootstrap.annotations.UnitTest;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@UnitTest
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/fredhoppersearch/fredhoppersearch-test.xml")
public class DefaultMetaAttributeCollectorTest
{

	//	@Resource
	//	private Set<Locale> localesEnDe;
	//	@Mock
	//	private MetaAttributeModel source1;
	//	@Mock
	//	private MetaAttributeModel source2;
	//	@Mock
	//	private MetaAttributeModel source3;
	//	@Mock
	//	private MetaAttributeModel source4;
	//
	//
	//
	//	private FhMetaAttributeData data1;
	//	private FhMetaAttributeData data2;
	//	private FhMetaAttributeData data3;
	//	private FhMetaAttributeData data4;
	//	private FhMetaAttributeData data5;
	//
	//
	//	@Mock
	//	private MetaAttributeConverter converter;
	//
	//	private DefaultMetaAttributeCollector collector;
	//
	//
	//	@Before
	//	public void setUp() throws Exception
	//	{
	//
	//		MockitoAnnotations.initMocks(this);
	//		//		createMetaAttributeMockBehaviour(1, source1);
	//		//		createMetaAttributeMockBehaviour(1, source2);
	//		//		createMetaAttributeMockBehaviour(1, source3);
	//		//		createMetaAttributeMockBehaviour(1, source4);
	//
	//		data1 = createMetaAttributeData(1);
	//		data2 = createMetaAttributeData(2);
	//		data3 = createMetaAttributeData(3);
	//		data4 = createMetaAttributeData(4);
	//
	//		final Set<MetaAttributeModel> attributes = new HashSet<>(Arrays.asList(new MetaAttributeModel[]
	//		{ source1, source2, source3, source4 }));
	//
	//		when(converter.convert(any(), any())).thenReturn(data1, data2, data3, data4);
	//
	//		collector = new DefaultMetaAttributeCollector(converter, attributes, localesEnDe);
	//
	//
	//
	//	}
	//
	//
	//
	//
	//	@Test
	//	public void testNext() throws Exception
	//	{
	//
	//		assertTrue(collector.hasNext());
	//
	//		final Set<FhMetaAttributeData> datas = new HashSet<>();
	//		for (final FhMetaAttributeData data : collector)
	//		{
	//			datas.add(data);
	//		}
	//
	//		assertThat(datas, containsInAnyOrder(data1, data2, data3, data4));
	//
	//
	//	}
	//
	//
	//
	//	private FhMetaAttributeData createMetaAttributeData(final int id)
	//	{
	//		final FhMetaAttributeData data = new FhMetaAttributeData();
	//		data.setAttributeId("id" + id);
	//		return data;
	//	}
	//
	//
	//	//
	//	//	private void createMetaAttributeMockBehaviour(final int id, final MetaAttributeModel mock)
	//	//	{
	//	//		when(mock.getAttributeId()).thenReturn("attid" + id);
	//	//		when(mock.getBaseType()).thenReturn(AttributeBaseType.INT);
	//	//		when(mock.getValuesProvider()).thenReturn("provider" + id);
	//	//		when(Boolean.valueOf(mock.isVariantAttribute())).thenReturn(Boolean.TRUE);
	//	//		for (final Locale loc : localesEnDe)
	//	//		{
	//	//			when(mock.getName(loc)).thenReturn("name" + "id" + loc.toString());
	//	//		}
	//	//	}
}


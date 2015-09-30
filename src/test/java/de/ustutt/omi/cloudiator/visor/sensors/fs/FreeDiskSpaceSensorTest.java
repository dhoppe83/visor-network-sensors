/*
 * Copyright (c) 2015 University of Stuttgart
 *
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.  Licensed under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package de.ustutt.omi.cloudiator.visor.sensors.fs;

import org.hyperic.sigar.SigarException;
import org.junit.Before;
import org.junit.Test;

import de.uniulm.omi.cloudiator.visor.monitoring.DefaultMonitorContext;
import de.uniulm.omi.cloudiator.visor.monitoring.InvalidMonitorContextException;
import de.uniulm.omi.cloudiator.visor.monitoring.MeasurementNotAvailableException;
import de.uniulm.omi.cloudiator.visor.monitoring.SensorInitializationException;
import de.uniulm.omi.cloudiator.visor.monitoring.DefaultMonitorContext.MonitorContextBuilder;
import de.ustutt.omi.cloudiator.visor.sensors.fs.FreeDiskSpaceSensor;

public class FreeDiskSpaceSensorTest {

	private FreeDiskSpaceSensor sensor;

	@Before
	public void setUp() throws SensorInitializationException {
		sensor = new FreeDiskSpaceSensor();
		sensor.init();
	}

	@Test
	public void testFreeDiskSpaceSensor() 
			throws InvalidMonitorContextException, 
				   MeasurementNotAvailableException,
				   SigarException {
		MonitorContextBuilder builder = DefaultMonitorContext.builder();
		builder.addContext("fs_root", "/");
		builder.addContext("unit", "mb");
		sensor.setMonitorContext(builder.build());

		System.out.println(sensor.getMeasurement().getValue());
	}
	
	@Test
	public void testEmptyMonitorContext() 
			throws MeasurementNotAvailableException,
			       InvalidMonitorContextException {
		MonitorContextBuilder builder = DefaultMonitorContext.builder();
		sensor.setMonitorContext(builder.build());
		
		System.out.println(sensor.getMeasurement().getValue());
	}

	@Test
	public void testMissingFsRoot() 
			throws MeasurementNotAvailableException, 
			       InvalidMonitorContextException {
		MonitorContextBuilder builder = DefaultMonitorContext.builder();
		builder.addContext("unit", "mb");
		sensor.setMonitorContext(builder.build());
		
		System.out.println(sensor.getMeasurement().getValue());
	}
	
	@Test
	public void testMissingUnit() 
			throws MeasurementNotAvailableException, 
			       InvalidMonitorContextException {
		MonitorContextBuilder builder = DefaultMonitorContext.builder();
		builder.addContext("fs_root", "/");
		sensor.setMonitorContext(builder.build());
		
		System.out.println(sensor.getMeasurement().getValue());
	}
	
	@Test(expected = MeasurementNotAvailableException.class)
	public void testWrongFsRoot() 
			throws MeasurementNotAvailableException, 
			       InvalidMonitorContextException {
		MonitorContextBuilder builder = DefaultMonitorContext.builder();
		builder.addContext("fs_root", "foobar");
		sensor.setMonitorContext(builder.build());
		
		System.out.println(sensor.getMeasurement().getValue());
	}
	
	@Test
	public void testWrongUnit() 
			throws MeasurementNotAvailableException, 
			       InvalidMonitorContextException {
		MonitorContextBuilder builder = DefaultMonitorContext.builder();
		builder.addContext("unit", "foobar");
		sensor.setMonitorContext(builder.build());
		
		System.out.println(sensor.getMeasurement().getValue());
	}
	
}

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
package de.ustutt.omi.cloudiator.visor.sensors.net;

import org.junit.Before;
import org.junit.Test;

import de.uniulm.omi.cloudiator.visor.monitoring.DefaultMonitorContext;
import de.uniulm.omi.cloudiator.visor.monitoring.InvalidMonitorContextException;
import de.uniulm.omi.cloudiator.visor.monitoring.MeasurementNotAvailableException;
import de.uniulm.omi.cloudiator.visor.monitoring.SensorInitializationException;
import de.uniulm.omi.cloudiator.visor.monitoring.DefaultMonitorContext.MonitorContextBuilder;
import de.ustutt.omi.cloudiator.visor.sensors.net.DownloadBandwidthSensor;

public class DownloadBandwidthSensorTest {

	private DownloadBandwidthSensor sensor;

	@Before
	public void setUp() throws SensorInitializationException {
		sensor = new DownloadBandwidthSensor();
		sensor.init();
	}

	@Test
	public void testEth0Traffic() 
			throws MeasurementNotAvailableException,
			       InvalidMonitorContextException,
			       InterruptedException {
		MonitorContextBuilder builder = DefaultMonitorContext.builder();
		builder.addContext("net_device", "eth0");
		builder.addContext("unit", "kb");

		sensor.setMonitorContext(builder.build());
		try {
			sensor.getMeasurement();
		} catch (Exception e) {
			// first measurement
		}
		Thread.sleep(500);
		
		System.out.println(sensor.getMeasurement().getValue());
	}

}

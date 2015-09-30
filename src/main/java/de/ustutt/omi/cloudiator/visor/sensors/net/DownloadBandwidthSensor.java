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

import de.uniulm.omi.cloudiator.visor.monitoring.Measurement;
import de.uniulm.omi.cloudiator.visor.monitoring.MeasurementImpl;
import de.uniulm.omi.cloudiator.visor.monitoring.MeasurementNotAvailableException;
import de.uniulm.omi.cloudiator.visor.monitoring.MonitorContext;

public class DownloadBandwidthSensor extends RxBytesSensor {

	private long previousVal = 0;
	private long previousTime = 0;
	
	@Override
	protected Measurement getMeasurement(MonitorContext monitorContext)
			throws MeasurementNotAvailableException {
		if (previousVal == 0) {
			previousVal = getNetStat();
			previousTime = System.currentTimeMillis();
			throw new MeasurementNotAvailableException(
					new Error("No data collected"));
		}
		long currentVal = getNetStat();
		long currentTime = System.currentTimeMillis();
		
		double data = currentVal - previousVal;
		double seconds = (currentTime - previousTime) / 1000.0;
		
		previousVal = currentVal;
		previousTime = currentTime;
		
		data = data / unit / seconds;
		data = Math.floor(data * 100.0) / 100.0;
		
		return new MeasurementImpl(System.currentTimeMillis(), data);
	}

}

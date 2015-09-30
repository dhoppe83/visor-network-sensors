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
package de.ustutt.omi.cloudiator.visor.sensors;

import de.uniulm.omi.cloudiator.visor.monitoring.AbstractSensor;
import de.uniulm.omi.cloudiator.visor.monitoring.InvalidMonitorContextException;
import de.uniulm.omi.cloudiator.visor.monitoring.Measurement;
import de.uniulm.omi.cloudiator.visor.monitoring.MeasurementImpl;
import de.uniulm.omi.cloudiator.visor.monitoring.MeasurementNotAvailableException;
import de.uniulm.omi.cloudiator.visor.monitoring.MonitorContext;

/**
 * @author hopped
 *
 */
public abstract class AbstractDiskIoSensor extends AbstractSensor {

	public static final String CONTEXT_DEVICE = "fs_device";
	public static final String CONTEXT_UNIT = "unit";

	private final String defaultDevice = SensorUtils.getDefaultFsDevice();
	private double unit = SensorUtils.FS_DEFAULT_UNIT;
	private String ioDevice = null;
	
	@Override
	public void setMonitorContext(MonitorContext monitorContext)
			throws InvalidMonitorContextException {
		super.setMonitorContext(monitorContext);
		ioDevice = monitorContext.getOrDefault(CONTEXT_DEVICE, defaultDevice);
		String ctx = monitorContext.getValue(CONTEXT_UNIT);
		unit = SensorUtils.parseContextUnit(ctx);
	}

	@Override
	protected Measurement getMeasurement(MonitorContext monitorContext)
			throws MeasurementNotAvailableException {
		double usage = getUsage(ioDevice, unit);
		return new MeasurementImpl(System.currentTimeMillis(), usage);
	}

	public abstract double getUsage(final String name, final double unit)
			throws MeasurementNotAvailableException;

}

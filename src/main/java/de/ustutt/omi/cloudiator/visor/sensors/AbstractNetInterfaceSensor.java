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

import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.SigarProxyCache;

import de.uniulm.omi.cloudiator.visor.monitoring.AbstractSensor;
import de.uniulm.omi.cloudiator.visor.monitoring.InvalidMonitorContextException;
import de.uniulm.omi.cloudiator.visor.monitoring.Measurement;
import de.uniulm.omi.cloudiator.visor.monitoring.MeasurementImpl;
import de.uniulm.omi.cloudiator.visor.monitoring.MeasurementNotAvailableException;
import de.uniulm.omi.cloudiator.visor.monitoring.MonitorContext;

public abstract class AbstractNetInterfaceSensor extends AbstractSensor {

	public static final String CONTEXT_NET_DEVICE = "net_device";
	public static final String CONTEXT_NET_MODE = "mode";
	public static final String CONTEXT_NET_UNIT = "unit";

	protected double unit = SensorUtils.NET_DEFAULT_UNIT;
	protected String device = null;
	
	private static final String DEFAULT_NET_DEVICE = "eth0";
	private static final String DEFAULT_NET_UNIT = "kb";
	
	@Override
	public void setMonitorContext(MonitorContext monitorContext)
			throws InvalidMonitorContextException {
		super.setMonitorContext(monitorContext);
		device = monitorContext.getOrDefault(CONTEXT_NET_DEVICE, DEFAULT_NET_DEVICE);
		try {
			SigarProxyCache.newInstance().getNetInterfaceStat(device);
		} catch (SigarException e) {
			throw new InvalidMonitorContextException(
					new Error("Device (" + device + ") not available."));
		}
		String strUnit = monitorContext.getOrDefault(CONTEXT_NET_UNIT, DEFAULT_NET_UNIT);
		unit = SensorUtils.parseContextNetUnit(strUnit);
	}

	@Override
	protected Measurement getMeasurement(MonitorContext monitorContext)
			throws MeasurementNotAvailableException {
		double val = getNetStat() / unit;
		val = Math.floor(val * 100) / 100;
		return new MeasurementImpl(System.currentTimeMillis(), val);
	}
	
	protected abstract long getNetStat() throws MeasurementNotAvailableException;

}

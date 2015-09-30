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

import org.hyperic.sigar.FileSystem;
import org.hyperic.sigar.FileSystemUsage;
import org.hyperic.sigar.SigarProxy;
import org.hyperic.sigar.SigarProxyCache;

import de.uniulm.omi.cloudiator.visor.monitoring.AbstractSensor;
import de.uniulm.omi.cloudiator.visor.monitoring.InvalidMonitorContextException;
import de.uniulm.omi.cloudiator.visor.monitoring.Measurement;
import de.uniulm.omi.cloudiator.visor.monitoring.MeasurementImpl;
import de.uniulm.omi.cloudiator.visor.monitoring.MeasurementNotAvailableException;
import de.uniulm.omi.cloudiator.visor.monitoring.MonitorContext;
import de.ustutt.omi.cloudiator.visor.sensors.SensorUtils;

public class FreeDiskSpaceSensor extends AbstractSensor {

	public static final String CONTEXT_UNIT = "unit";
	public static final String CONTEXT_FS = "fs_root";

	private final String defaultFSRoot = SensorUtils.getDefaultFsRoot();
	private String fsRoot = null;
	private double unit = 0.0;

	@Override
	public void setMonitorContext(MonitorContext monitorContext)
			throws InvalidMonitorContextException {
		super.setMonitorContext(monitorContext);
		fsRoot = monitorContext.getOrDefault(CONTEXT_FS, defaultFSRoot);
		String ctx = monitorContext.getValue(CONTEXT_UNIT);
		unit = SensorUtils.parseContextUnit(ctx);
	}

	@Override
	protected Measurement getMeasurement(MonitorContext monitorContext)
			throws MeasurementNotAvailableException {
		double usage = getFreeDiskSpace(fsRoot,	unit);
		return new MeasurementImpl(System.currentTimeMillis(), usage);
	}

	private double getFreeDiskSpace(final String name, final double size)
			throws MeasurementNotAvailableException {
		try {
			SigarProxy proxy = SigarProxyCache.newInstance();
			FileSystem fs = proxy.getFileSystemMap().getFileSystem(name);
			FileSystemUsage value = proxy.getFileSystemUsage(fs.getDirName());
			double freeMemory = value.getAvail() / size;
			return Math.floor(freeMemory * 100) / 100;
		} catch (Exception e) {
			throw new MeasurementNotAvailableException(e);
		}
	}

}

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
import org.hyperic.sigar.NfsFileSystem;
import org.hyperic.sigar.SigarProxy;
import org.hyperic.sigar.SigarProxyCache;

import de.uniulm.omi.cloudiator.visor.monitoring.AbstractSensor;
import de.uniulm.omi.cloudiator.visor.monitoring.InvalidMonitorContextException;
import de.uniulm.omi.cloudiator.visor.monitoring.Measurement;
import de.uniulm.omi.cloudiator.visor.monitoring.MeasurementImpl;
import de.uniulm.omi.cloudiator.visor.monitoring.MeasurementNotAvailableException;
import de.uniulm.omi.cloudiator.visor.monitoring.MonitorContext;

public class NfsAccessSensor extends AbstractSensor {

	public static final String CONTEXT_NFS = "nfs_root";

	private String nfsRoot = null;

	@Override
	public void setMonitorContext(MonitorContext monitorContext)
			throws InvalidMonitorContextException {
		super.setMonitorContext(monitorContext);
		nfsRoot = monitorContext.getValue(CONTEXT_NFS);
	}

	@Override
	protected Measurement getMeasurement(MonitorContext monitorContext)
			throws MeasurementNotAvailableException {
		boolean avail = checkNFSAvailability(nfsRoot);
		return new MeasurementImpl(System.currentTimeMillis(), avail);
	}

	public boolean checkNFSAvailability(final String name) {
		try {
			SigarProxy proxy = SigarProxyCache.newInstance();
			FileSystem fs = proxy.getFileSystemMap().getFileSystem(name);
			if (fs instanceof NfsFileSystem) {
				NfsFileSystem nfs = (NfsFileSystem) fs;
				return nfs.ping();
			}
		} catch (Exception e) {
			return false;
		}

		return false;
	}

}

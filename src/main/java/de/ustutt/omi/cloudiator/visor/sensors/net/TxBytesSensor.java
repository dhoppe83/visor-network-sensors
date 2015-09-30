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

import org.hyperic.sigar.NetInterfaceStat;
import org.hyperic.sigar.SigarProxy;
import org.hyperic.sigar.SigarProxyCache;

import de.uniulm.omi.cloudiator.visor.monitoring.MeasurementNotAvailableException;
import de.ustutt.omi.cloudiator.visor.sensors.AbstractNetInterfaceSensor;

public class TxBytesSensor extends AbstractNetInterfaceSensor {

	@Override
	protected long getNetStat() throws MeasurementNotAvailableException {
		try {
			SigarProxy proxy = SigarProxyCache.newInstance();
			NetInterfaceStat stat = proxy.getNetInterfaceStat(device);
			return stat.getTxBytes();
		} catch (Exception e) {
			throw new MeasurementNotAvailableException(e);
		}
	}

}

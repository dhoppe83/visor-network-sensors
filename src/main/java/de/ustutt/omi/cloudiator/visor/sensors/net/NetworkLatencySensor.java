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

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import de.uniulm.omi.cloudiator.visor.monitoring.AbstractSensor;
import de.uniulm.omi.cloudiator.visor.monitoring.InvalidMonitorContextException;
import de.uniulm.omi.cloudiator.visor.monitoring.Measurement;
import de.uniulm.omi.cloudiator.visor.monitoring.MeasurementImpl;
import de.uniulm.omi.cloudiator.visor.monitoring.MeasurementNotAvailableException;
import de.uniulm.omi.cloudiator.visor.monitoring.MonitorContext;

public class NetworkLatencySensor extends AbstractSensor {

	public static final String CONTEXT_HOST = "host";
	public static final String CONTEXT_PORT = "port";
	public static final String CONTEXT_LOOPS = "loops";

	private static final String DEFAULT_PORT = "80";
	private static final String DEFAULT_LOOPS = "1";

	private String host;
	private int port;
	private int loops;

	@Override
	public void setMonitorContext(MonitorContext monitorContext)
			throws InvalidMonitorContextException {
		super.setMonitorContext(monitorContext);
		host = monitorContext.getValue(CONTEXT_HOST);
		if (host == null) {
			throw new InvalidMonitorContextException(
					new Error("Given host is null."));
		}
		String strLoops = monitorContext.getOrDefault(CONTEXT_LOOPS, DEFAULT_LOOPS);
		String strPort = monitorContext.getOrDefault(CONTEXT_PORT, DEFAULT_PORT);
		
		try {
			port = Integer.parseInt(strPort);
		} catch (NumberFormatException nfe) {
			throw new InvalidMonitorContextException(
					new Error("Port (" + strPort + ") is invalid."));
		}
		
		try {
			loops = Integer.parseInt(strLoops);
		} catch (NumberFormatException nfe) {
			throw new InvalidMonitorContextException(
					new Error("Given loops (" + strLoops + ") are not a number."));
		}
		
		checkURL(host, port);
	}

	private void checkURL(final String host, final int port)
			throws InvalidMonitorContextException {
		Socket socket = null;
		try {
			SocketAddress sockaddr = new InetSocketAddress(host, port);
			socket = new Socket();
			socket.connect(sockaddr, 500);
		} catch (Exception e) {
			throw new InvalidMonitorContextException(e);
		} finally {
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException ex) {
					throw new InvalidMonitorContextException(ex);
				}
			}
		}
	}

	@Override
	protected Measurement getMeasurement(MonitorContext monitorContext)
			throws MeasurementNotAvailableException {
		double latency = computeAverageLatency();
		return new MeasurementImpl(System.currentTimeMillis(), latency);
	}

	private double computeAverageLatency() {
		double latency = 0.0;

		Socket socket = null;
		SocketAddress address = new InetSocketAddress(host, port);
		for (int i = 0; i != loops; ++i) {
			long start = System.currentTimeMillis();
			socket = new Socket();
			try {
				socket.connect(address);
				socket.close();
			} catch (IOException e) {
				continue;
			}
			long end = System.currentTimeMillis();
			latency = latency + (end - start);
		}
		double average = latency / loops;
		
		return Math.floor(average * 100) / 100;
	}

}

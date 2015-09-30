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

import java.util.HashMap;
import java.util.Map;

/**
 * @author hopped
 *
 */
public final class SensorUtils {

	/* file system */
	public static final String MB = "mb";
	public static final String GB = "gb";
	public static final String TB = "tb";

	public static final double MBytes = 1024.0;
	public static final double GBytes = 1048576.0;
	public static final double TBytes = 1073741824.0;
	public static final double FS_DEFAULT_UNIT = GBytes;

	public static final String FS_DEFAULT_LINUX = "/";
	public static final String FS_DEFAULT_WINDOWS = "D:/";

	private static String OS = System.getProperty("os.name").toLowerCase();

	/* network interface */
	public static final String KB = "kb";
	public static final String TX = "tx";
	public static final String RX = "rx";
	public static final String BYTES = "bytes";
	public static final String PACKETS = "packets";
	public static final double NetKBytes = 1024.0;
	public static final double NetMBytes = 1048576.0;
	public static final double NetGBytes = 1073741824.0;
	public static final double NetTBytes = 1099511627776.0;
	public static final double NET_DEFAULT_UNIT = NetKBytes;
	
	/* iostat interface */
	public static final String READS = "r";
	public static final String WRITES = "w";
	public static final String IO_DEFAULT_DEVICE_UNIX = "sda";
	public static final String IO_DEFAULT_DEVICE_WINDOWS = "sda";
	public static final String IO_DEFAULT_MODE = READS;

	private static Map<String, Double> metricsMap = null;
	static {
		metricsMap = new HashMap<String, Double>(3);
		metricsMap.put(SensorUtils.MB, MBytes);
		metricsMap.put(SensorUtils.GB, GBytes);
		metricsMap.put(SensorUtils.TB, TBytes);
	}
	
	private static Map<String, Double> netBytesMap = null;
	static {
		netBytesMap = new HashMap<String, Double>(2);
		netBytesMap.put(SensorUtils.KB, NetKBytes);
		netBytesMap.put(SensorUtils.MB, NetMBytes);
		netBytesMap.put(SensorUtils.GB, NetGBytes);
		netBytesMap.put(SensorUtils.TB, NetTBytes);
	}
	
	public static boolean isWindows() {
		return (OS.indexOf("win") >= 0);
	}

	public static boolean isUnix() {
		return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0);
	}
	
	public static String getDefaultFsRoot() {
		if (isUnix()) {
			return FS_DEFAULT_LINUX;
		}
		if (isWindows()) {
			return FS_DEFAULT_WINDOWS;
		}
		return null;
	}
	
	public static String getDefaultFsDevice() {
		if (isUnix()) {
			return IO_DEFAULT_DEVICE_UNIX;
		}
		if (isWindows()) {
			return IO_DEFAULT_DEVICE_WINDOWS;
		}
		return null;
	}
	
	public static double parseContextUnit(String strMetric) {
		if (strMetric == null) {
			return SensorUtils.FS_DEFAULT_UNIT;
		}
		
		strMetric = strMetric.toLowerCase();
		return metricsMap.getOrDefault(strMetric, SensorUtils.FS_DEFAULT_UNIT);
	}

	public static double parseContextNetUnit(String name) {
		if (name == null) {
			return SensorUtils.NET_DEFAULT_UNIT;
		}
		
		name = name.toLowerCase();
		return netBytesMap.getOrDefault(name, SensorUtils.NET_DEFAULT_UNIT);
	}
}

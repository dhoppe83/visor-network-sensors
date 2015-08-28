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

/**
 * This file was modified by University of Ulm.
 */

package de.ustutt.cloudiator.visor.monitoring.sensors;


import de.uniulm.omi.cloudiator.visor.monitoring.*;
import org.hyperic.sigar.*;

import java.util.ArrayList;


public class AvgReceivedBytesProbe implements Sensor {

    private final static int SMALL_CYCLE = 1000;
    private final static int BIG_CYCLE = 5000;
    private Sigar sigarImpl;

    public AvgReceivedBytesProbe() {
        this.sigarImpl = new Sigar();

    }

    /**
     * Average rate of received bytes in bytes per second. Blocking method, execute it in a separate thread
     *
     * @return
     * @throws SigarException
     * @throws InterruptedException
     */
    public double getAverageRxRate() throws SigarException, InterruptedException {
        int smallCycle = SMALL_CYCLE;
        int bigCycle = BIG_CYCLE;
        NetInterfaceStat netStat = null;
        long rxBytesLastCycle = 0;
        long rxBytesNewCycle = 0;
        ArrayList<Long> rxBytesTotal = new ArrayList<Long>();
        long averageBytesPerSecond;

        // measure received bytes n times, where n is bigCycle/smallCycle
        for (int i = 0; i <= bigCycle; i += smallCycle) {
            rxBytesNewCycle = 0;
            SigarProxy sigar = SigarProxyCache.newInstance(sigarImpl);

            // measure the number of received bytes on all network interfaces
            for (String ni : sigar.getNetInterfaceList()) {
                netStat = sigar.getNetInterfaceStat(ni);
                if (i == 0)
                    rxBytesLastCycle += netStat.getRxBytes();
                else
                    rxBytesNewCycle += netStat.getRxBytes();
            }
            // we are interested on the changed values
            if (rxBytesNewCycle - rxBytesLastCycle > 0) {
                rxBytesTotal.add(rxBytesNewCycle - rxBytesLastCycle);
                rxBytesLastCycle = rxBytesNewCycle;
            }
            // sleep till the next measure cycle
            Thread.sleep(smallCycle);
        }
        // get average value for all non zero measurements
        averageBytesPerSecond = this.calculateAverageRate(rxBytesTotal);

        return averageBytesPerSecond;
    }

    //TODO: substitute by some more beautiful calculation
    private long calculateAverageRate(ArrayList<Long> measurements) {
        long result = 0;
        for (Long m : measurements)
            result += m;
        if (result != 0)
            result = result / measurements.size();
        return result;
    }


    @Override public void init() throws SensorInitializationException {
        // TODO Auto-generated method stub

    }

    @Override public void setMonitorContext(MonitorContext monitorContext)
        throws InvalidMonitorContextException {
        // TODO Auto-generated method stub

    }

    @Override public Measurement getMeasurement() throws MeasurementNotAvailableException {
        //KBytes|sec
        double averageRxRate = 0;
        try {
            averageRxRate = getAverageRxRate() / 1024;
        } catch (SigarException | InterruptedException e) {
            throw new MeasurementNotAvailableException(e);
        }
        return new MeasurementImpl(System.currentTimeMillis(), averageRxRate);
    }

}

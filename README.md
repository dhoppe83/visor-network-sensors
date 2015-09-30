# HLRS Visor Sensors

> Providing sensors for the [Cloudiator Visor monitoring framework][visor]. The sensors collect file system statistics as well as network statistics on Linux and Windows platforms.


## Motivation
The Cloudiator Visor monitoring framework is extended with eleven additional sensors to support substantial network monitoring and status reports about the underlying file systems. Most sensors rely on the [System Information Gatherer and Reporter (Sigar) API][sigar] that provides a unified access to network interfaces and to underlying file systems. That way, a single sensor is guaranteed to monitor metrics independent of the operating system.


## Available Sensors
This modules provides four file system sensors and seven network interface sensors. Please read more about each sensor and available configuration parameter in the [documentation][documentation].

### File System Sensors
| Name of sensor                             | Description                                         |
| :----------------------------------------- |:----------------------------------------------------|
| [DiskIoReadSensor][DiskIoReadSensor]       | Reports on data reads per second for a given disk   |
| [DiskIoWriteSensor][DiskIoWriteSensor]     | Reports on data writes per second for a given disk  |
| [FreeDiskSpaceSensor][FreeDiskSpaceSensor] | Reports on available disk space                     |
| [NfsAccessSensor][NfsAccessSensor]         | Checks the availability of a NFS via pinging        |

### Network Sensors
| Name of sensor                                   | Description                                               |
| :----------------------------------------------- |:----------------------------------------------------------|
| [DownloadBandwithSensor][DownloadBandwithSensor] | Reports on the download speed of a given network device   |
| [NetworkLatencySensor][NetworkLatencySensor]     | Reports on the network latency for a specific host        |
| [RxBytesSensor][RxBytesSensor]                   | Reports on the accumulated RxBytes for a given device     |
| [RxPacketsSensor][RxPacketsSensor]               | Reports on the accumulated RxPackets for a given device   |
| [TxBytesSensor][TxBytesSensor]                   | Reports on the accumulated TxBytes for a given device     |
| [TxPacketsSensor][TxPacketsSensor]               | Reports on the accumulated TxPackets for a given device   |
| [UploadBandwithSensor][UploadBandwithSensor]     | Reports on the upload speed of a given network device     |


## Prerequisites
Please ensure that a current version of Maven is installed. Moreover, please keep in mind that the Visor monitoring framework requires JRE 8.

There are no further system requirements for using the monitoring framework and the HLRS visor sensors. However, it should be noted that these sensors were tested only for Linux and Windows. As a consequence, we cannot ensure their functionality beyond those operating systems mentioned.


## Installation
```bash
$ cd /tmp
$ git clone https://github.com/dhoppe83/visor.git
$ cd visor
$ git checkout hlrs-sensors
$ mvn clean install
$ unzip visor-service/target/visor.zip -d run
```

## Run Cloudiator Visor
```bash
$ cd run/visor/bin
$ ./visor.sh -conf ../conf/default.properties
```

## Run HLRS Visor Sensors
Each sensor can be added to the Visor monitoring framework using the RESTful service available per default at __http://0.0.0.0:31415__. The following example shows how to add the __FreeDiskSpaceSensor__ (sensorClassName) to the framework. The sensor is configured via the __contexts__ field to collect the free disk space for the root folder (/) of the underling file system.

```bash
$ curl -XPOST "http://0.0.0.0:31415/monitors" -d'
{  
  "contexts": [  
    {  
      "key": "fs_root",
      "value": "/"
    }
  ],
  "interval": {  
    "period": 1,
    "timeUnit": "SECONDS"
  },
  "metricName": "free_disk_space",
  "sensorClassName": "de.ustutt.omi.cloudiator.visor.sensors.fs.FreeDiskSpaceSensor"
}'
```


## Contributing
Find a bug? Have a feature request?
Please [create](https://github.com/dhoppe83/visor/issues) an issue.


## Authors
**Dennis Hoppe**
+ [github/hopped](https://github.com/hopped)

**Yosandra Sandoval**
+ [github/yosandra](https://github.com/yosandra)

**Pavel Skortsov**
+ [skvortsov@hlrs.de](mailto:skvortsov@hlrs.de)


## Release History

| Date        | Version | Comment          |
| ----------- | ------- | ---------------- |
| 2015-09-03  | 0.1.0   | Initial release. |


## License
Copyright 2015 University of Stuttgart.

[Apache License](LICENSE).


[documentation]: documentation/README.md
[hlrs]: http://www.hlrs.de
[sigar]: http://sigar.hyperic.com/
[visor]: https://github.com/cloudiator/visor

[DiskIoReadSensor]: documentation/DiskIoReadSensor.md
[DiskIoWriteSensor]: documentation/DiskIoWriteSensor.md
[FreeDiskSpaceSensor]: documentation/FreeDiskSpaceSensor.md
[NfsAccessSensor]: documentation/NfsAccessSensor.md

[DownloadBandwithSensor]: documentation/DownloadBandwithSensor.md
[NetworkLatencySensor]: documentation/NetworkLatencySensor.md
[RxBytesSensor]: documentation/RxBytesSensor.md
[RxPacketsSensor]: documentation/RxPacketsSensor.md
[TxBytesSensor]: documentation/TxBytesSensor.md
[TxPacketsSensor]: documentation/TxPacketsSensor.md
[UploadBandwithSensor]: documentation/UploadBandwithSensor.md


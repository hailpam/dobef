dobef
=====

DOBEF (Data Oriented Benchmark Framework) provides a generalized framework to executed benchmarks 
for data-oriented distributed applications.

How it works
============

In order to benchmark a data-oriented distributed application, three classes must be written if 
not already enlisted in the 'impl/' package:

- Specific Writer/Producer. It implements a 'Runnable' and writes/produces data.
- Specific Reader/Consumer. It implements a 'Runnable' and reads/consumes data.
- Specific Benchmark. It extends the 'Benchmark' class and represents the benchmark session pilot, 
driving the 'init', 'warmup' and run 'phases' of each benchmark.

Multiple Producers/Writers and Consumers/Readers can be detached for each specific benchmark. The 
probing activities and collection of statistcs is done OTB (Out of the Box) by the framework itself.
Producers/Writers and Consumers/Readers can be distributed on different machines.

Example
=======

Hazelcast Benchmark in local.

- Writer Node Log and Statistics.
```
:configure
 DONE
:init
Nov 08, 2015 10:37:39 AM com.hazelcast.instance.DefaultAddressPicker
INFO: [LOCAL] [dev] [3.3.2] Prefer IPv4 stack is true.
Nov 08, 2015 10:37:39 AM com.hazelcast.instance.DefaultAddressPicker
INFO: [LOCAL] [dev] [3.3.2] Picked Address[192.168.122.1]:5701, using socket ServerSocket[addr=/0:0:0:0:0:0:0:0,localport=5701], bind any local is true
Nov 08, 2015 10:37:39 AM com.hazelcast.spi.impl.BasicOperationScheduler
INFO: [192.168.122.1]:5701 [dev] [3.3.2] Starting with 4 generic operation threads and 4 partition operation threads.
Nov 08, 2015 10:37:39 AM com.hazelcast.system
INFO: [192.168.122.1]:5701 [dev] [3.3.2] Hazelcast 3.3.2 (20141023 - 7a40e93) starting at Address[192.168.122.1]:5701
Nov 08, 2015 10:37:39 AM com.hazelcast.system
INFO: [192.168.122.1]:5701 [dev] [3.3.2] Copyright (C) 2008-2014 Hazelcast.com
Nov 08, 2015 10:37:39 AM com.hazelcast.instance.Node
INFO: [192.168.122.1]:5701 [dev] [3.3.2] Creating MulticastJoiner
Nov 08, 2015 10:37:39 AM com.hazelcast.core.LifecycleService
INFO: [192.168.122.1]:5701 [dev] [3.3.2] Address[192.168.122.1]:5701 is STARTING
Nov 08, 2015 10:37:42 AM com.hazelcast.cluster.MulticastJoiner
INFO: [192.168.122.1]:5701 [dev] [3.3.2] 


Members [1] {
	Member [192.168.122.1]:5701 this
}

Nov 08, 2015 10:37:42 AM com.hazelcast.core.LifecycleService
INFO: [192.168.122.1]:5701 [dev] [3.3.2] Address[192.168.122.1]:5701 is STARTED
 DONE
:warmUp
Nov 08, 2015 10:37:42 AM com.hazelcast.partition.InternalPartitionService
INFO: [192.168.122.1]:5701 [dev] [3.3.2] Initializing cluster partition table first arrangement...
 DONE - 90ms
:benchmark

Benchmark Summary- Data [64], Test(s) [100], Repetition(s) [2]
{
  elapsed[ms]        : 1407.0
  mean[us]           : 7025.4434550000005
  variance[us]       : 1.8004617006952386E7
  std deviation[us]  : 4243.184771719514
  min[us]            : 1931.0695
  max[us]            : 24392.4985
  95th percentile[us]: 15872.51885
}

:writeResults
 DONE
```

Versions
========
- 1.0   Framework, plus Hazelcast and A-MQ benchmarks

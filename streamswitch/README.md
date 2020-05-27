# StreamSwitch Core
This repository is the source code of StreamSwitch APIs and our latency-guarantee model

## Introduction
This repository contains two major parts:
- Generic StreamSwitch abstract interfaces that can be embedded into different stream processing engines.
- Latency-guaratee model that builds on abstract interfaces.

## Usage
In order to use StreamSwitch, you need to implement two classes mainly:
- `OperatorControllerListener`: Your own `OperatorControllerListener` instance in your stream processing engine which supports load-balacing and scaling.
- `MetricsRetriever`: StreamSwitch will use your metrics retriever to retrieve metrics.
Check `YarnApplicationMaster.java` and `JMXMetricsRetriever.java` in Samza directory to see how to use our interfaces

# Stream Switch Core
This repository is the source code of Stream Switch APIs and our latency-guarantee model

## Introduction
Two major part:
- Generic Stream Switch abstract interfaces that can be embedded into different stream processing engines.
- Latency-guaratee model that builds based on abstract interfaces.

## Usage
In order to use Stream Switch, you need to implement two major clases:
- `OperatorControllerListener`: Your own `OperatorControllerListener` instance in your stream processing engine which supports load-balacing and scaling.
- `MetricsRetriever`: SS will use your metrics retriever to retrieve metrics then make decisions.
Check `YarnApplicationMaster` and `JMXMetricsRetriever` in Samza directory to see how to use our interfaces



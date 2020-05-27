# Flink example

This is the Nexmark query2 example on Flink.

### Prerequisite

Install and start [Apache ZooKeeper](https://downloads.apache.org/zookeeper/zookeeper-3.6.1/apache-zookeeper-3.6.1-bin.tar.gz)

Install and start [Apache Kafka](https://archive.apache.org/dist/kafka/0.10.1.1/kafka_2.11-0.10.1.1.tgz)

### Setup & Run

Here we illustrate an application example that runs in a local Flink cluster.

1. Build Flink from `Flink/` directory.

    ```shell
    mvn clean install -DskipTests -Dcheckstyle.skip
    ```

2. Update configurations in `${FLINK_DIR}/conf/flink-conf.yaml`, one configuration example is inside `${FLINK_EXAMPLE_DIR}/conf/flink-conf.yaml`, and you can specify your requirement accordingly.

3. Build this application.

    ```shell
    mvn clean package
    ```

4. Run local Flink cluster.

    ```shell
    bin/start-cluster.sh
    ```

5. Run this application on the local Flink cluster.

    ```shell
    bin/flink run ${FLINK_EXAMPLE_DIR}/target/testbed-1.0-SNAPSHOT.jar
    ```

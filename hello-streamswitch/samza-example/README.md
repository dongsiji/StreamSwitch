# Samza Example

This is the Nexmark query2 example on Samza.

### Prerequisite
Install and start [Apache Hadoop YARN](http://archive.apache.org/dist/hadoop/common/hadoop-2.6.1/hadoop-2.6.1.tar.gz)

Install and start [Apache ZooKeeper](https://downloads.apache.org/zookeeper/zookeeper-3.6.1/apache-zookeeper-3.6.1-bin.tar.gz)

Install and start [Apache Kafka](https://archive.apache.org/dist/kafka/0.10.1.1/kafka_2.11-0.10.1.1.tgz)

### Setup

Here we illustrate an Samza application example that runs on YARN.

1. Build Samza from `samza/` directory.

   ```shell
   ./gradlew publishToMavenLocal
   ```

2. Update configurations in `${SAMZA_EXAMPLE_DIR}/application/src/main/config/`. One example configuration file is available named `nexmark-q2.properties`.

3. Build application in `${SAMZA_EXAMPLE_DIR}/application` directory.

   ```shell
   mvn clean package
   cd target
   tar -zvxf testbed_1.0.0-0.0.1-dist.tar.gz
   ```

4. Upload application jar file to HDFS. 

   ```shell
   yarn/bin/hdfs dfs -mkdir  hdfs://localhost:9000/testbed-nexmark/
   yarn/bin/hdfs dfs -put  ${SAMZA_EXAMPLE_DIR}/application/target/testbed_1.0.0-0.0.1-dist.tar.gz hdfs://localhost:9000/testbed-nexmark
   ```

5. Go to generator folder, and build Kafka generator.

   ```shell
   mvn clean package
   ```

### Run

1. Submit application to YARN.

   ```shell
   ${SAMZA_EXAMPLE_DIR}/application/target/bin/run-app.sh --config-factory=org.apache.samza.config.factories.PropertiesConfigFactory --config-path=file://${SAMZA_EXAMPLE_DIR}/application/target/config/nexmark-q2.properties
   ```

2. Use Kafka CLI to create the topic that will be consumed by application.

   ```shell
   kafka/bin/kafka-topics.sh --zookeeper localhost:2181 --create --topic bids --partitions 64 --replication-factor 1
   ```

3. Start data generator for the application.

   ```shell
   java -cp ${SAMZA_EXAMPLE_DIR}/producer/target/kafka_producer-0.0.1-jar-with-dependencies.jar kafka.Nexmark.KafkaBidGenerator
   ```

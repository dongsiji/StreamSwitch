package kafka.Nexmark.refactored;

import kafka.Nexmark.Util;
import org.apache.beam.sdk.nexmark.NexmarkConfiguration;
import org.apache.beam.sdk.nexmark.sources.generator.GeneratorConfig;
import org.apache.beam.sdk.nexmark.sources.generator.model.AuctionGenerator;
import org.apache.beam.sdk.nexmark.sources.generator.model.BidGenerator;
import org.apache.beam.sdk.nexmark.sources.generator.model.PersonGenerator;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;
import java.util.Random;

public class NexGenMain {
    private String TOPIC;

    private static KafkaProducer<Long, String> producer;
    private final GeneratorConfig config = new GeneratorConfig(NexmarkConfiguration.DEFAULT, 1, 1000L, 0, 1);
    private volatile boolean running = true;
    private long eventsCountSoFar = 0;
    private int rate;
    private int cycle;

    public NexGenMain(String input, String BROKERS, int rate, int cycle) {
        Properties props = new Properties();
        props.put("bootstrap.servers", BROKERS);
        props.put("client.id", "ProducerExample");
        props.put("key.serializer", "org.apache.kafka.common.serialization.LongSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
//        props.put("partitioner.class", "generator.SSEPartitioner");
        producer = new KafkaProducer<Long, String>(props);
        TOPIC = input;
        this.rate = rate;
        this.cycle = cycle;
    }

    public void generate() throws InterruptedException {
        int epoch = 0;
        int count = 0;

        long emitStartTime = 0;

        while (running && eventsCountSoFar < 20_000_000) {

            emitStartTime = System.currentTimeMillis();

            if (count == 20) {
                // change input rate every 1 second.
                epoch++;
                int curRate = Util.changeRateSin(rate, cycle, epoch);
                System.out.println("epoch: " + epoch%cycle + " current rate is: " + curRate);
                count = 0;
            }

            for (int i = 0; i < Integer.valueOf(rate/20); i++) {
                auctionGen();
                bidGen();
                personGen();
            }
            // Sleep for the rest of timeslice if needed
            pause(emitStartTime);
            count++;
        }

        producer.close();
    }

    private void pause(long emitStartTime) throws InterruptedException {
        long emitTime = System.currentTimeMillis() - emitStartTime;
        if (emitTime < 1000/20) {
            Thread.sleep(1000/20 - emitTime);
        }
    }

    private void personGen() {
        long nextId = nextId();
        Random rnd = new Random(nextId);

        // When, in event time, we should generate the event. Monotonic.
        long eventTimestamp =
                config.timestampAndInterEventDelayUsForEvent(
                        config.nextEventNumber(eventsCountSoFar)).getKey();

        ProducerRecord<Long, String> newRecord = new ProducerRecord<Long, String>(TOPIC, nextId,
                PersonGenerator.nextPerson(nextId, rnd, eventTimestamp, config).toString());
        producer.send(newRecord);
        eventsCountSoFar++;
    }

    private void bidGen() {
        long nextId = nextId();
        Random rnd = new Random(nextId);

        // When, in event time, we should generate the event. Monotonic.
        long eventTimestamp =
                config.timestampAndInterEventDelayUsForEvent(
                        config.nextEventNumber(eventsCountSoFar)).getKey();

        ProducerRecord<Long, String> newRecord = new ProducerRecord<Long, String>(TOPIC, nextId,
                BidGenerator.nextBid(nextId, rnd, eventTimestamp, config).toString());
        producer.send(newRecord);
        eventsCountSoFar++;
    }

    private void auctionGen() {
        long nextId = nextId();
        Random rnd = new Random(nextId());

        // When, in event time, we should generate the event. Monotonic.
        long eventTimestamp =
                config.timestampAndInterEventDelayUsForEvent(
                        config.nextEventNumber(eventsCountSoFar)).getKey();

        ProducerRecord<Long, String> newRecord = new ProducerRecord<Long, String>(TOPIC, nextId,
                AuctionGenerator.nextAuction(eventsCountSoFar, nextId, rnd, eventTimestamp, config).toString());
        producer.send(newRecord);
        eventsCountSoFar++;
    }

    private long nextId() {
        return config.firstEventId + config.nextAdjustedEventNumber(eventsCountSoFar);
    }

    public static void main(String[] args) throws InterruptedException {
        final ParameterTool params = ParameterTool.fromArgs(args);

        String BROKERS = params.get("host", "localhost:9092");
        String TOPIC = params.get("topic", "auctions");
        int rate = params.getInt("rate", 1000);
        int cycle = params.getInt("cycle", 360);

        new NexGenMain(TOPIC, BROKERS, rate, cycle).generate();
    }
}

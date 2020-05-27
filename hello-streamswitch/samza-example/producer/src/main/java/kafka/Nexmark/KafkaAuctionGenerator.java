package kafka.Nexmark;

import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.beam.sdk.nexmark.sources.generator.model.AuctionGenerator;
import org.apache.beam.sdk.nexmark.model.Auction;
import java.util.Properties;
import java.util.Random;
import org.apache.beam.sdk.nexmark.NexmarkConfiguration;
import org.apache.beam.sdk.nexmark.sources.generator.GeneratorConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import static java.lang.Thread.sleep;

/**
 * SSE generaor
 */
public class KafkaAuctionGenerator {

    private String TOPIC;

    private static KafkaProducer<Long, String> producer;
    private final GeneratorConfig config;
    private volatile boolean running = true;
    private long eventsCountSoFar = 0;
    private int rate;
    private int cycle;
    private int base;

    public KafkaAuctionGenerator(String input, String BROKERS, int rate, int cycle, int base) {
        Properties props = new Properties();
        props.put("bootstrap.servers", BROKERS);
        props.put("client.id", "Auction");
        props.put("batch.size", "163840");
        props.put("linger.ms", "10");
        props.put("key.serializer", "org.apache.kafka.common.serialization.LongSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
//        props.put("partitioner.class", "generator.SSEPartitioner");
        producer = new KafkaProducer<Long, String>(props);
        TOPIC = input;
        this.rate = rate;
        this.cycle = cycle;
       	this.base = base;
        NexmarkConfiguration nexconfig = NexmarkConfiguration.DEFAULT;
        nexconfig.hotBiddersRatio=1;
        nexconfig.hotAuctionRatio=1;
        nexconfig.hotSellersRatio=1;
        nexconfig.numInFlightAuctions=1;
        nexconfig.numEventGenerators=1;
        nexconfig.avgAuctionByteSize=100;
        config = new GeneratorConfig(nexconfig, 1, 1000L, 0, 1);
    }

    public void generate() throws InterruptedException {
//        long streamStartTime = System.currentTimeMillis();
        int epoch = 0;
        int count = 0;
        int tupleCounter = 0;

        long emitStartTime = 0;
        int curRate = rate + base;

        System.out.println("++++++enter warm up");
        warmup();
        System.out.println("++++++end warm up");


        long start = System.currentTimeMillis();
        int sleepCnt = 0;
        long cur = 0;

        while (running) {

            emitStartTime = System.currentTimeMillis();

            if (emitStartTime >= start + (epoch + 1) * 1000) {
                // change input rate every 1 second.
                //epoch++;
		epoch = (int)((emitStartTime - start)/1000);
                curRate = base + changeRate(epoch);
                System.out.println("auction epoch: " + epoch%cycle + " current rate is: " + curRate);
                System.out.println("auction epoch: " + epoch + " actual current rate is: " + tupleCounter);
                count = 0;
                tupleCounter = 0;
            }

            for (int i = 0; i < Integer.valueOf(curRate/20); i++) {

                long nextId = nextId();
                Random rnd = new Random(nextId);

                // When, in event time, we should generate the event. Monotonic.
                long eventTimestamp =
                        config.timestampAndInterEventDelayUsForEvent(
                                config.nextEventNumber(eventsCountSoFar)).getKey();

//                System.out.println(AuctionGenerator.nextAuction(eventsCountSoFar, nextId, rnd, eventTimestamp, config).toString());

//                ProducerRecord<Long, String> newRecord = new ProducerRecord<Long, String>(TOPIC, null, System.currentTimeMillis(), nextId,
//                        AuctionGenerator.nextAuction(eventsCountSoFar, nextId, rnd, eventTimestamp, config).toString());
                ProducerRecord<Long, String> newRecord = new ProducerRecord<Long, String>(TOPIC, nextId,
                        AuctionGenerator.nextAuction(eventsCountSoFar, nextId, rnd, eventTimestamp, config).toString());
                producer.send(newRecord);
                tupleCounter++;
                eventsCountSoFar++;
            }

            // Sleep for the rest of timeslice if needed
            long emitTime = System.currentTimeMillis() - emitStartTime;
            if (emitTime < 1000/20) {
                Thread.sleep(1000/20 - emitTime);
            }

//            cur = System.currentTimeMillis();
//            if (cur < sleepCnt*50 + start) {
//                sleep((sleepCnt*50 + start) - cur);
//            } else {
//                System.out.println("rate exceeds" + 50 + "ms.");
//            }

            sleepCnt++;
            count++;
        }

        producer.close();
    }

    private void warmup() throws InterruptedException {
        long emitStartTime = 0;
        long warmupStart = System.currentTimeMillis();
        int curRate = rate + base;
        while (System.currentTimeMillis()-warmupStart < 120000) {
            emitStartTime = System.currentTimeMillis();
            System.out.println("Warm up still has: " + String.valueOf(120000 - (emitStartTime - warmupStart)));
            for (int i = 0; i < Integer.valueOf(curRate/20); i++) {

                long nextId = nextId();
                Random rnd = new Random(nextId);

                // When, in event time, we should generate the event. Monotonic.
                long eventTimestamp =
                        config.timestampAndInterEventDelayUsForEvent(
                                config.nextEventNumber(eventsCountSoFar)).getKey();

//                System.out.println(AuctionGenerator.nextAuction(eventsCountSoFar, nextId, rnd, eventTimestamp, config).toString());

//                ProducerRecord<Long, String> newRecord = new ProducerRecord<Long, String>(TOPIC, null, System.currentTimeMillis(), nextId,
//                        AuctionGenerator.nextAuction(eventsCountSoFar, nextId, rnd, eventTimestamp, config).toString());
                ProducerRecord<Long, String> newRecord = new ProducerRecord<Long, String>(TOPIC, nextId,
                        AuctionGenerator.nextAuction(eventsCountSoFar, nextId, rnd, eventTimestamp, config).toString());
                producer.send(newRecord);
                eventsCountSoFar++;
            }

            // Sleep for the rest of timeslice if needed
            long emitTime = System.currentTimeMillis() - emitStartTime;
            if (emitTime < 1000/20) {
                Thread.sleep(1000/20 - emitTime);
            }
        }
    }

    private long nextId() {
        return config.firstEventId + config.nextAdjustedEventNumber(eventsCountSoFar);
    }

    private int changeRate(int epoch) {
        double sineValue = Math.sin(Math.toRadians(epoch*360/cycle)) + 1;
        System.out.println(sineValue);

        Double curRate = (sineValue * rate);
        return curRate.intValue();
    }


    public static void main(String[] args) throws InterruptedException {
        final ParameterTool params = ParameterTool.fromArgs(args);

        String BROKERS = params.get("host", "localhost:9092");
        String TOPIC = params.get("topic", "auctions");
        int rate = params.getInt("rate", 1000);
        int cycle = params.getInt("cycle", 360);
        int base = params.getInt("base", 0);

        new KafkaAuctionGenerator(TOPIC, BROKERS, rate, cycle, base).generate();
    }
}


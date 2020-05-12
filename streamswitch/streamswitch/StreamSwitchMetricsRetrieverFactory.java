package streamswitch-core.streamswitch;

import org.apache.samza.config.Config;

interface StreamSwitchMetricsRetrieverFactory {
    StreamSwitchMetricsRetriever getRetriever(Config config);
}

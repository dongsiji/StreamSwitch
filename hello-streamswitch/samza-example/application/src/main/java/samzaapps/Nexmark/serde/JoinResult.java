package samzaapps.Nexmark.serde;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * An ad click event.
 */
public class JoinResult {
    public String name;
    public String city;
    public String state;
    public long auctionId;

    public JoinResult(
            @JsonProperty("name") String name,
            @JsonProperty("city") String city,
            @JsonProperty("state") String state,
            @JsonProperty("auctionId") long auctionId) {
        this.name = name;
        this.city = city;
        this.state = state;
        this.auctionId = auctionId;
    }

    @Override
    public String toString() {
        return "joinResult: { name:" + name + ", city: " + city + ", state: " + state + ", auctionId: " + auctionId + "}";

    }
}
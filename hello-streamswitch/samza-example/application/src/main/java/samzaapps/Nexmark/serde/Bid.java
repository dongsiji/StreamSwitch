package samzaapps.Nexmark.serde;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * An ad click event.
 */
public class Bid {

    private long auction; // the unique id of the page that the ad was clicked on
    private long bidder; // an unique id for the ad
    private long price; // the user that clicked the ad
    private long dateTime; // the user that clicked the ad
    private String extra; // the user that clicked the ad

    public Bid(
            @JsonProperty("auction") long auction,
            @JsonProperty("bidder") long bidder,
            @JsonProperty("price") long price,
            @JsonProperty("dataTime") long dateTime,
            @JsonProperty("extra") String extra){
        this.auction = auction;
        this.bidder = bidder;
        this.price = price;
        this.dateTime = dateTime;
        this.extra = extra;
    }

    public long getAuction() {
        return auction;
    }

    public void setAuction(long auction) {
        this.auction = auction;
    }

    public long getBidder() {
        return bidder;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public long getPrice() {
        return price;
    }

    public long getDateTime() {
        return dateTime;
    }

    public String getExtra() {
        return extra;
    }

}
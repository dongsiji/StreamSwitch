package samzaapps.Nexmark.serde;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * An ad click event.
 */
public class Auction {

    private long id; // the unique id of the page that the ad was clicked on
    private String itemName; // an unique id for the ad
    private String description; // the user that clicked the ad
    private long initialBid; // the user that clicked the ad
    private long reserve; // the user that clicked the ad
    private long dateTime;
    private long expires; // the user that clicked the ad
    private long seller; // the user that clicked the ad
    private int  category;
    private String extra; // the user that clicked the ad

    public Auction(
            @JsonProperty("id") long id,
            @JsonProperty("itemName") String itemName,
            @JsonProperty("description") String description,
            @JsonProperty("initialBid") long initialBid,
            @JsonProperty("reserve") long reserve,
            @JsonProperty("dateTime") long dateTime,
            @JsonProperty("expires") long expires,
            @JsonProperty("seller") long seller,
            @JsonProperty("category") int category,
            @JsonProperty("extra") String extra) {
        this.id = id;
        this.itemName = itemName;
        this.description = description;
        this.initialBid = initialBid;
        this.reserve = reserve;
        this.dateTime = dateTime;
        this.expires = expires;
        this.seller = seller;
        this.category = category;
        this.extra = extra;
    }

    public long getId() {
        return id;
    }

    public String getItemName() {
        return itemName;
    }

    public String getDescription() {
        return description;
    }

    public long getInitialBid() {
        return initialBid;
    }

    public long getReserve() {
        return reserve;
    }

    public long getExpires() {
        return expires;
    }

    public long getSeller() {
        return seller;
    }

    public String getExtra() {
        return extra;
    }
}
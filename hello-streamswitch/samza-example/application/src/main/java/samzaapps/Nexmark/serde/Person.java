package samzaapps.Nexmark.serde;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * An ad click event.
 */
public class Person {

    private long id; // the unique id of the page that the ad was clicked on
    private String name; // an unique id for the ad
    private String email; // the user that clicked the ad
    private String creditCard; // the user that clicked the ad
    private String city; // the user that clicked the ad
    private String state; // the user that clicked the ad
    private long dateTime; // the user that clicked the ad
    private String extra; // the user that clicked the ad

    public Person(
            @JsonProperty("id") long id,
            @JsonProperty("name") String name,
            @JsonProperty("emailAddress") String email,
            @JsonProperty("creditCard") String creditCard,
            @JsonProperty("city") String city,
            @JsonProperty("state") String state,
            @JsonProperty("dateTime") long dateTime,
            @JsonProperty("extra") String extra){
        this.id = id;
        this.name = name;
        this.email = email;
        this.creditCard = creditCard;
        this.city = city;
        this.state = state;
        this.dateTime = dateTime;
        this.extra = extra;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getCreditCard() {
        return creditCard;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public long getDateTime() {
        return dateTime;
    }

    public String getExtra() {
        return extra;
    }
}
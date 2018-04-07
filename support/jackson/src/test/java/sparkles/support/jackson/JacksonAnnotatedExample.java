package sparkles.support.jackson;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"uuid",
"dateTime"
})
public class JacksonAnnotatedExample {

/**
* Universally unique identifier for this item.
*
*/
@JsonProperty("uuid")
@JsonPropertyDescription("Universally unique identifier for this item.")
private UUID uuid;
/**
* A date time object w/o timezone.
*
*/
@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "UTC")
@JsonProperty("dateTime")
@JsonPropertyDescription("A date time object w/o timezone.")
private LocalDateTime dateTime;
@JsonIgnore
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

/**
* No args constructor for use in serialization
*
*/
public JacksonAnnotatedExample() {
}

/**
*
* @param dateTime
* @param uuid
*/
public JacksonAnnotatedExample(UUID uuid, LocalDateTime dateTime) {
super();
this.uuid = uuid;
this.dateTime = dateTime;
}

/**
* Universally unique identifier for this item.
*
*/
@JsonProperty("uuid")
public UUID getUuid() {
return uuid;
}

/**
* Universally unique identifier for this item.
*
*/
@JsonProperty("uuid")
public void setUuid(UUID uuid) {
this.uuid = uuid;
}

public JacksonAnnotatedExample withUuid(UUID uuid) {
this.uuid = uuid;
return this;
}

/**
* A date time object w/o timezone.
*
*/
@JsonProperty("dateTime")
public LocalDateTime getDateTime() {
return dateTime;
}

/**
* A date time object w/o timezone.
*
*/
@JsonProperty("dateTime")
public void setDateTime(LocalDateTime dateTime) {
this.dateTime = dateTime;
}

public JacksonAnnotatedExample withDateTime(LocalDateTime dateTime) {
this.dateTime = dateTime;
return this;
}

@JsonAnyGetter
public Map<String, Object> getAdditionalProperties() {
return this.additionalProperties;
}

@JsonAnySetter
public void setAdditionalProperty(String name, Object value) {
this.additionalProperties.put(name, value);
}

public JacksonAnnotatedExample withAdditionalProperty(String name, Object value) {
this.additionalProperties.put(name, value);
return this;
}

}

package fr.zenigata;

import com.google.gson.annotations.SerializedName;

public class Episode {
  @SerializedName("Record ID")
  private String id;

  @SerializedName("Nom")
  private String name;

  @SerializedName("Path")
  private String fictionNumber;

  @SerializedName("Piste")
  private String track;

  @SerializedName("Durée")
  private Float duration;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getTrack() {
    return track;
  }

  public void setTrack(String track) {
    this.track = track;
  }

  public Float getDuration() {
    return duration;
  }

  public void setDuration(Float duration) {
    this.duration = duration;
  }

  public String getFictionNumber() {
    return fictionNumber;
  }

  public void setFictionNumber(String fictionNumber) {
    this.fictionNumber = fictionNumber;
  }
}

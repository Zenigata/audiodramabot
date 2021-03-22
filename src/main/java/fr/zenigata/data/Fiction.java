package fr.zenigata.data;

import java.util.List;

import com.google.gson.annotations.SerializedName;
import com.sybit.airtable.vo.Attachment;

public class Fiction {
  @SerializedName("Record ID")
  private String id;

  @SerializedName("Nom")
  private String name;

  @SerializedName("Statut")
  private String status;

  @SerializedName("Synopsis")
  private String synopsis;

  @SerializedName("Site")
  private String site;

  @SerializedName("Genre")
  private List<String> genres;

  @SerializedName("Durée")
  private Float duration;

  @SerializedName("Auteur")
  private List<String> authors;

  @SerializedName("Pochette")
  private List<Attachment> covers;

  @SerializedName("Youtube")
  private String youtube;

  @SerializedName("Website")
  private String website;

  @SerializedName("RSS")
  private String rss;

  @SerializedName("Soundcloud")
  private String soundcloud;

  @SerializedName("Spotify")
  private String spotify;

  @SerializedName("Discord")
  private boolean playableOnDiscord;

  @SerializedName("Récompense")
  private List<String> award;

  @SerializedName("Citation")
  private String quote;

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

  public String getSynopsis() {
    return synopsis;
  }

  public void setSynopsis(String synopsis) {
    this.synopsis = synopsis;
  }

  public String getSite() {
    return site;
  }

  public void setSite(String site) {
    this.site = site;
  }

  public Float getDuration() {
    return duration;
  }

  public void setDuration(Float duration) {
    this.duration = duration;
  }

  public List<Attachment> getCovers() {
    return covers;
  }

  public void setCovers(List<Attachment> covers) {
    this.covers = covers;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public List<String> getGenres() {
    return genres;
  }

  public void setGenres(List<String> genres) {
    this.genres = genres;
  }

  public List<String> getAuthors() {
    return authors;
  }

  public void setAuthors(List<String> authors) {
    this.authors = authors;
  }

  public String getYoutube() {
    return youtube;
  }

  public void setYoutube(String youtube) {
    this.youtube = youtube;
  }

  public String getWebsite() {
    return website;
  }

  public void setWebsite(String website) {
    this.website = website;
  }

  public String getRss() {
    return rss;
  }

  public void setRss(String rss) {
    this.rss = rss;
  }

  public String getSoundcloud() {
    return soundcloud;
  }

  public void setSoundcloud(String soundcloud) {
    this.soundcloud = soundcloud;
  }

  public String getSpotify() {
    return spotify;
  }

  public void setSpotify(String spotify) {
    this.spotify = spotify;
  }

  public List<String> getAward() {
    return award;
  }

  public void setAward(List<String> award) {
    this.award = award;
  }

  public boolean isPlayableOnDiscord() {
    return playableOnDiscord;
  }

  public void setPlayableOnDiscord(boolean playableOnDiscord) {
    this.playableOnDiscord = playableOnDiscord;
  }

  public String getQuote() {
    return quote;
  }

  public void setQuote(String quote) {
    this.quote = quote;
  }

}

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

}

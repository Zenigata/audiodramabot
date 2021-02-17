package fr.zenigata;

import com.google.gson.annotations.SerializedName;

public class Fiction {
  private String id;

  @SerializedName("Nom")
  private String name;

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

}

package fr.zenigata.data;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EpisodeRequest {
  public static final String DEFAULT_SEASON_ONE = "S01";
  public static final String DEFAULT_EPISODE_ONE = "E01";

  private String fictionName = "";
  private String seasonNumber = DEFAULT_SEASON_ONE;
  private String episodeNumber = DEFAULT_EPISODE_ONE;

  public EpisodeRequest(String parameter) {
    if (parameter == null || parameter.trim().isEmpty()) {
      return;
    }

    String[] splitList = parameter.trim().split(" ");
    String fictionNameToSearch, seasonNumber, episodeNumber;

    if (isAnEpisodeNumber(splitList[splitList.length - 1])) {
      episodeNumber = splitList[splitList.length - 1];
      if (isASeasonNumber(splitList[splitList.length - 2])) {
        seasonNumber = splitList[splitList.length - 2];
        fictionNameToSearch = parameter.substring(0, parameter.indexOf(seasonNumber) - 1);
      } else {
        seasonNumber = DEFAULT_SEASON_ONE;
        fictionNameToSearch = parameter.substring(0, parameter.indexOf(episodeNumber) - 1);
      }
    } else {
      episodeNumber = DEFAULT_EPISODE_ONE;
      if (isASeasonNumber(splitList[splitList.length - 1])) {
        seasonNumber = splitList[splitList.length - 1];
        fictionNameToSearch = parameter.substring(0, parameter.indexOf(seasonNumber) - 1);
      } else {
        seasonNumber = DEFAULT_SEASON_ONE;
        fictionNameToSearch = parameter;
      }
    }

    this.fictionName = fictionNameToSearch;
    this.seasonNumber = seasonNumber.toUpperCase();
    this.episodeNumber = episodeNumber.toUpperCase();
  }

  private boolean isASeasonNumber(String season) {
    Pattern pattern = Pattern.compile("s\\d{2}", Pattern.CASE_INSENSITIVE);
    Matcher matcher = pattern.matcher(season);
    return matcher.find();
  }

  private boolean isAnEpisodeNumber(String episode) {
    Pattern pattern = Pattern.compile("e\\d{2}", Pattern.CASE_INSENSITIVE);
    Matcher matcher = pattern.matcher(episode);
    return matcher.find();
  }

  public String getFictionName() {
    return this.fictionName;
  }

  public String getSeasonNumber() {
    return this.seasonNumber;
  }

  public String getEpisodeNumber() {
    return this.episodeNumber;
  }

}

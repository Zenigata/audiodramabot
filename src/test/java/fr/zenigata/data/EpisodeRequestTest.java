package fr.zenigata.data;

import org.junit.Assert;
import org.junit.Test;

public class EpisodeRequestTest {
  @Test
  public void empty() {
    EpisodeRequest request = new EpisodeRequest("");

    Assert.assertEquals("", request.getFictionName());
    Assert.assertEquals(EpisodeRequest.DEFAULT_SEASON_ONE, request.getSeasonNumber());
    Assert.assertEquals(EpisodeRequest.DEFAULT_EPISODE_ONE, request.getEpisodeNumber());
  }

  @Test
  public void only_spaces() {
    EpisodeRequest request = new EpisodeRequest("        ");

    Assert.assertEquals("", request.getFictionName());
    Assert.assertEquals(EpisodeRequest.DEFAULT_SEASON_ONE, request.getSeasonNumber());
    Assert.assertEquals(EpisodeRequest.DEFAULT_EPISODE_ONE, request.getEpisodeNumber());
  }

  @Test
  public void a_word() {
    EpisodeRequest request = new EpisodeRequest("rico");

    Assert.assertEquals("rico", request.getFictionName());
    Assert.assertEquals(EpisodeRequest.DEFAULT_SEASON_ONE, request.getSeasonNumber());
    Assert.assertEquals(EpisodeRequest.DEFAULT_EPISODE_ONE, request.getEpisodeNumber());
  }

  @Test
  public void a_phrase() {
    EpisodeRequest request = new EpisodeRequest("mes rêves me parlent");

    Assert.assertEquals("mes rêves me parlent", request.getFictionName());
    Assert.assertEquals(EpisodeRequest.DEFAULT_SEASON_ONE, request.getSeasonNumber());
    Assert.assertEquals(EpisodeRequest.DEFAULT_EPISODE_ONE, request.getEpisodeNumber());
  }

  @Test
  public void season_only() {
    EpisodeRequest request = new EpisodeRequest("mes rêves me parlent S02");

    Assert.assertEquals("mes rêves me parlent", request.getFictionName());
    Assert.assertEquals("S02", request.getSeasonNumber());
    Assert.assertEquals(EpisodeRequest.DEFAULT_EPISODE_ONE, request.getEpisodeNumber());
  }

  @Test
  public void complete() {
    EpisodeRequest request = new EpisodeRequest("mes rêves me parlent S02 E02");

    Assert.assertEquals("mes rêves me parlent", request.getFictionName());
    Assert.assertEquals("S02", request.getSeasonNumber());
    Assert.assertEquals("E02", request.getEpisodeNumber());
  }

  @Test
  public void another_complete() {
    EpisodeRequest request = new EpisodeRequest("mes rêves me parlent S02 E03");

    Assert.assertEquals("mes rêves me parlent", request.getFictionName());
    Assert.assertEquals("S02", request.getSeasonNumber());
    Assert.assertEquals("E03", request.getEpisodeNumber());
  }

  @Test
  public void bad_format() {
    EpisodeRequest request = new EpisodeRequest("mes rêves me parlent S02 S03 E02 S03 Test");

    Assert.assertEquals("mes rêves me parlent S02 S03 E02 S03 Test", request.getFictionName());
    Assert.assertEquals(EpisodeRequest.DEFAULT_SEASON_ONE, request.getSeasonNumber());
    Assert.assertEquals(EpisodeRequest.DEFAULT_EPISODE_ONE, request.getEpisodeNumber());
  }

  @Test
  public void with_capitals() {
    EpisodeRequest request = new EpisodeRequest("Le Dernier Souffle des Zéphyrs S01 E01");

    Assert.assertEquals("Le Dernier Souffle des Zéphyrs", request.getFictionName());
    Assert.assertEquals("S01", request.getSeasonNumber());
    Assert.assertEquals("E01", request.getEpisodeNumber());
  }

  @Test
  public void another_capitals() {
    EpisodeRequest request = new EpisodeRequest("Et la Terre éclata S12 E03");

    Assert.assertEquals("Et la Terre éclata", request.getFictionName());
    Assert.assertEquals("S12", request.getSeasonNumber());
    Assert.assertEquals("E03", request.getEpisodeNumber());
  }

  @Test
  public void a_lot_of_seasons_and_episodes() {
    EpisodeRequest request = new EpisodeRequest("Et la Terre éclata S42 E53");

    Assert.assertEquals("Et la Terre éclata", request.getFictionName());
    Assert.assertEquals("S42", request.getSeasonNumber());
    Assert.assertEquals("E53", request.getEpisodeNumber());
  }

  @Test
  public void bad_format_season() {
    EpisodeRequest request = new EpisodeRequest("Et la Terre éclata S2 E53");

    Assert.assertEquals("Et la Terre éclata S2", request.getFictionName());
    Assert.assertEquals(EpisodeRequest.DEFAULT_SEASON_ONE, request.getSeasonNumber());
    Assert.assertEquals("E53", request.getEpisodeNumber());
  }

  @Test
  public void bad_format_episode() {
    EpisodeRequest request = new EpisodeRequest("Et la Terre éclata S42 E3");

    Assert.assertEquals("Et la Terre éclata S42 E3", request.getFictionName());
    Assert.assertEquals(EpisodeRequest.DEFAULT_SEASON_ONE, request.getSeasonNumber());
    Assert.assertEquals(EpisodeRequest.DEFAULT_EPISODE_ONE, request.getEpisodeNumber());
  }

  @Test
  public void capitals_and_episode() {
    EpisodeRequest request = new EpisodeRequest("Le Dernier Souffle des Zéphyrs S01 E04");

    Assert.assertEquals("Le Dernier Souffle des Zéphyrs", request.getFictionName());
    Assert.assertEquals("S01", request.getSeasonNumber());
    Assert.assertEquals("E04", request.getEpisodeNumber());
  }

  @Test
  public void lower_case() {
    EpisodeRequest request = new EpisodeRequest("Le Dernier Souffle des Zéphyrs s01 e04");

    Assert.assertEquals("Le Dernier Souffle des Zéphyrs", request.getFictionName());
    Assert.assertEquals("S01", request.getSeasonNumber());
    Assert.assertEquals("E04", request.getEpisodeNumber());
  }

  @Test
  public void name_in_bad_format() {
    EpisodeRequest request = new EpisodeRequest("Et la Terre éclata S12 E32 bis E02");

    Assert.assertEquals("Et la Terre éclata S12 E32 bis", request.getFictionName());
    Assert.assertEquals(EpisodeRequest.DEFAULT_SEASON_ONE, request.getSeasonNumber());
    Assert.assertEquals("E02", request.getEpisodeNumber());
  }

}

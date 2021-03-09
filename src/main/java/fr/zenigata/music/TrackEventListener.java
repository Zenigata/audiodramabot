package fr.zenigata.music;

import java.util.Objects;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import discord4j.common.util.Snowflake;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

public class TrackEventListener extends AudioEventAdapter {

  private static final Logger logger = LoggerFactory.getLogger(TrackEventListener.class);

  private final Snowflake guildId;

  public TrackEventListener(Snowflake guildId) {
    this.guildId = guildId;
  }

  @Override
  public void onTrackStart(AudioPlayer player, AudioTrack track) {
    Mono.empty().subscribeOn(Schedulers.boundedElastic()).subscribe(null, err -> handleUnknownError(err));
  }

  private void handleUnknownError(Throwable err) {
    logger.error(String.format("An unknown error occurred: %s", Objects.requireNonNullElse(err.getMessage(), "")), err);
  }

}

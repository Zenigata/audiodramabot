package fr.zenigata.music;

import java.util.Objects;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import discord4j.common.util.Snowflake;
import fr.zenigata.CommandManager;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

public class AudioLoadResultListener implements AudioLoadResultHandler {

  private static final Logger logger = LoggerFactory.getLogger(AudioLoadResultListener.class);

  private Snowflake guildId;

  public AudioLoadResultListener(Snowflake guildId) {
    this.guildId = guildId;
  }

  @Override
  public void loadFailed(FriendlyException arg0) {
    // LavaPlayer could not parse an audio source for some reason
  }

  @Override
  public void noMatches() {
    // LavaPlayer did not find any audio to extract
  }

  @Override
  public void playlistLoaded(AudioPlaylist playlist) {
    // LavaPlayer found multiple AudioTracks from some playlist
  }

  @Override
  public void trackLoaded(AudioTrack track) {
    logger.debug("{Guild ID: {}} Track loaded: {}", this.guildId.asLong(), track.hashCode());
    Mono.justOrEmpty(CommandManager.getInstance().getMusicManager().getGuildMusic(this.guildId))
        .filter(guildMusic -> !guildMusic.getTrackScheduler().startOrQueue(track))
        .flatMap(GuildMusic::getMessageChannel).then(this.terminate()).subscribeOn(Schedulers.boundedElastic())
        .subscribe(null, AudioLoadResultListener::handleUnknownError);
  }

  private Mono<Void> terminate() {
    return Mono.justOrEmpty(CommandManager.getInstance().getMusicManager().getGuildMusic(this.guildId))
        .flatMap(guildMusic -> guildMusic.removeAudioLoadResultListener(this));
  }

  public static void handleUnknownError(Throwable err) {
    logger.error(String.format("An unknown error occurred: %s", Objects.requireNonNullElse(err.getMessage(), "")), err);
  }

}

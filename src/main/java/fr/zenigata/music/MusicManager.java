package fr.zenigata.music;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.track.playback.NonAllocatingAudioFrameBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import discord4j.common.util.Snowflake;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.object.entity.channel.VoiceChannel;
import discord4j.voice.AudioProvider;
import discord4j.voice.VoiceConnection;
import reactor.core.publisher.Mono;

public class MusicManager {

  private static final Logger logger = LoggerFactory.getLogger(MusicManager.class);

  private AudioPlayerManager playerManager;

  private final Map<Snowflake, GuildMusic> guildMusics;

  public MusicManager() {
    this.playerManager = new DefaultAudioPlayerManager();
    playerManager.getConfiguration().setFrameBufferFactory(NonAllocatingAudioFrameBuffer::new);
    AudioSourceManagers.registerRemoteSources(playerManager);

    this.guildMusics = new ConcurrentHashMap<>();
  }

  public Mono<GuildMusic> getOrCreate(GatewayDiscordClient gateway, Snowflake guildId, Snowflake voiceChannelId) {
    return Mono.justOrEmpty(this.getGuildMusic(guildId)).switchIfEmpty(Mono.defer(() -> {
      final AudioPlayer player = this.playerManager.createPlayer();
      player.addListener(new TrackEventListener(guildId));
      final AudioProvider provider = new LavaPlayerAudioProvider(player);

      return this.joinVoiceChannel(gateway, guildId, voiceChannelId, provider)
          .map(volume -> new TrackScheduler(player, volume))
          .map(trackScheduler -> new GuildMusic(gateway, guildId, trackScheduler)).doOnNext(guildMusic -> {
            this.guildMusics.put(guildId, guildMusic);
            logger.debug("{Guild ID: {}} Guild music created", guildId.asLong());
          });
    }));
  }

  private Mono<VoiceConnection> joinVoiceChannel(GatewayDiscordClient gateway, Snowflake guildId,
      Snowflake voiceChannelId, AudioProvider provider) {

    final Mono<Boolean> isDisconnected = gateway.getVoiceConnectionRegistry().getVoiceConnection(guildId)
        .flatMapMany(VoiceConnection::stateEvents).next().map(VoiceConnection.State.DISCONNECTED::equals)
        .defaultIfEmpty(true);

    return gateway.getChannelById(voiceChannelId).cast(VoiceChannel.class)
        // Do not join the voice channel if the current voice connection is in not
        // disconnected
        .filterWhen(ignored -> isDisconnected)
        .doOnNext(ignored -> logger.info("{Guild ID: {}} Joining voice channel...", guildId.asLong()))
        .flatMap(voiceChannel -> voiceChannel.join(spec -> spec.setProvider(provider)));
  }

  public Optional<GuildMusic> getGuildMusic(Snowflake guildId) {
    final GuildMusic guildMusic = this.guildMusics.get(guildId);
    logger.info("{Guild ID: {}} Guild music request: {}", guildId.asLong(), guildMusic);
    return Optional.ofNullable(guildMusic);
  }

  public AudioPlayerManager getPlayerManager() {
    return playerManager;
  }

  public Future<Void> loadItemOrdered(long guildId, String identifier, AudioLoadResultListener listener) {
    return this.playerManager.loadItemOrdered(guildId, identifier, listener);
  }

  public Mono<Void> destroyConnection(Snowflake guildId) {
    final GuildMusic guildMusic = this.guildMusics.remove(guildId);
    if (guildMusic != null) {
      guildMusic.destroy();
      logger.debug("{Guild ID: {}} Guild music destroyed", guildId.asLong());
    }

    return Mono.justOrEmpty(guildMusic).map(GuildMusic::getGateway)
        .map(GatewayDiscordClient::getVoiceConnectionRegistry).flatMap(registry -> registry.getVoiceConnection(guildId))
        .flatMap(VoiceConnection::disconnect);
  }

}

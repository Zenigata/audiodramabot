package fr.zenigata;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import discord4j.common.util.Snowflake;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.voice.VoiceConnection;
import reactor.core.publisher.Mono;

public class GuildMusic {

  private static final Logger logger = LoggerFactory.getLogger(GuildMusic.class);

  private final GatewayDiscordClient gateway;
  private long guildId;
  private TrackScheduler trackScheduler;
  private final Map<AudioLoadResultListener, Future<Void>> listeners;
  private AtomicLong messageChannelId;

  public GuildMusic(GatewayDiscordClient gateway, Snowflake guildId, TrackScheduler trackScheduler) {
    this.gateway = gateway;
    this.guildId = guildId.asLong();
    this.trackScheduler = trackScheduler;

    this.listeners = new ConcurrentHashMap<>();
    this.messageChannelId = new AtomicLong();
  }

  public Mono<MessageChannel> getMessageChannel() {
    return this.gateway.getChannelById(this.getMessageChannelId()).cast(MessageChannel.class);
  }

  private Snowflake getMessageChannelId() {
    return Snowflake.of(this.messageChannelId.get());
  }

  public void setMessageChannelId(Snowflake messageChannelId) {
    this.messageChannelId.set(messageChannelId.asLong());
  }

  public void addAudioLoadResultListener(AudioLoadResultListener listener, String identifier) {
    logger.debug("{Guild ID: {}} Adding audio load result listener: {}", this.guildId, listener.hashCode());
    this.listeners.put(listener,
        CommandManager.getInstance().getMusicManager().loadItemOrdered(this.guildId, identifier, listener));
  }

  public TrackScheduler getTrackScheduler() {
    return this.trackScheduler;
  }

  public Mono<Void> removeAudioLoadResultListener(AudioLoadResultListener listener) {
    logger.debug("{Guild ID: {}} Removing audio load result listener: {}", this.guildId, listener.hashCode());
    this.listeners.remove(listener);
    // If there is no music playing and nothing is loading, leave the voice channel
    if (this.trackScheduler.isStopped() && this.listeners.values().stream().allMatch(Future::isDone)) {
      return this.gateway.getVoiceConnectionRegistry().getVoiceConnection(Snowflake.of(this.guildId))
          .flatMap(VoiceConnection::disconnect);
    }
    return Mono.empty();
  }

  public void destroy() {
    this.listeners.values().forEach(task -> task.cancel(true));
    this.listeners.clear();
    this.trackScheduler.destroy();
  }

  public GatewayDiscordClient getGateway() {
    return gateway;
  }

}

package fr.zenigata;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sybit.airtable.Base;

import discord4j.voice.AudioProvider;

public class CommandManager {

  private static CommandManager instance = null;
  private Base base;
  private AudioProvider provider;
  private TrackScheduler scheduler;
  private AudioPlayerManager playerManager;
  private AudioPlayer player;

  public static CommandManager getInstance() {
    if (instance == null) {
      instance = new CommandManager();
    }
    return instance;
  }

  public Base getBase() {
    return base;
  }

  public void setBase(Base base) {
    this.base = base;
  }

  public AudioProvider getProvider() {
    return provider;
  }

  public void setProvider(AudioProvider provider) {
    this.provider = provider;
  }

  public TrackScheduler getScheduler() {
    return scheduler;
  }

  public void setScheduler(TrackScheduler scheduler) {
    this.scheduler = scheduler;
  }

  public AudioPlayerManager getPlayerManager() {
    return playerManager;
  }

  public void setPlayerManager(AudioPlayerManager playerManager) {
    this.playerManager = playerManager;
  }

  public AudioPlayer getPlayer() {
    return player;
  }

  public void setPlayer(AudioPlayer player) {
    this.player = player;
  }

}

package fr.zenigata.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackState;

import discord4j.voice.VoiceConnection;

public class TrackScheduler {

  private AudioPlayer player;
  private AudioTrack currentTrack;

  public TrackScheduler(AudioPlayer player, VoiceConnection volume) {
    this.player = player;
  }

  public boolean startOrQueue(AudioTrack track) {
    this.currentTrack = track;
    return this.player.startTrack(track, false);
  }

  public boolean isStopped() {
    return !this.isPlaying();
  }

  private boolean isPlaying() {
    return this.player.getPlayingTrack() != null;
  }

  public AudioPlayer getPlayer() {
    return player;
  }

  public void destroy() {
    if (this.currentTrack != null && this.currentTrack.getState() == AudioTrackState.PLAYING) {
      this.currentTrack.stop();
    }
    this.player.destroy();
  }

}

package fr.zenigata;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

public class TrackScheduler implements AudioLoadResultHandler {

  private AudioPlayer player;

  public TrackScheduler(AudioPlayer player) {
    this.player = player;
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
    // LavaPlayer found an audio source for us to play
    player.playTrack(track);
  }

}

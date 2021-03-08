package fr.zenigata;

import com.sybit.airtable.Base;

public class CommandManager {

  private static CommandManager instance = null;
  private Base base;
  private MusicManager musicManager;

  public static CommandManager getInstance() {
    if (instance == null) {
      instance = new CommandManager();
    }
    return instance;
  }

  public CommandManager() {
    this.musicManager = new MusicManager();
  }

  public Base getBase() {
    return base;
  }

  public void setBase(Base base) {
    this.base = base;
  }

  public MusicManager getMusicManager() {
    return musicManager;
  }

  public void setMusicManager(MusicManager musicManager) {
    this.musicManager = musicManager;
  }

}

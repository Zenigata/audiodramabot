package fr.zenigata.command;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

import org.reactivestreams.Publisher;

import discord4j.core.event.domain.message.MessageCreateEvent;
import fr.zenigata.CommandManager;
import reactor.core.publisher.Mono;

public class PauseCommand implements Command {

  @Override
  public String getName() {
    return "pause";
  }

  @Override
  public Publisher<?> execute(MessageCreateEvent event, String parameter) {
    AudioPlayer player = CommandManager.getInstance().getMusicManager().getGuildMusic(event.getGuildId().orElseThrow())
        .get().getTrackScheduler().getPlayer();
    player.setPaused(!player.isPaused());
    return Mono.empty();
  }
}

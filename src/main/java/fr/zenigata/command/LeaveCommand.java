package fr.zenigata.command;

import org.reactivestreams.Publisher;

import discord4j.core.event.domain.message.MessageCreateEvent;
import fr.zenigata.CommandManager;

public class LeaveCommand implements Command {
  @Override
  public String getName() {
    return "leave";
  }

  @Override
  public Publisher<?> execute(MessageCreateEvent event, String parameter) {
    CommandManager.getInstance().getPlayer().stopTrack();
    return event.getClient().getVoiceConnectionRegistry().disconnect(event.getGuildId().get());
  }
}

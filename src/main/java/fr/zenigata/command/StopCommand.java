package fr.zenigata.command;

import org.reactivestreams.Publisher;

import discord4j.common.util.Snowflake;
import discord4j.core.event.domain.message.MessageCreateEvent;
import fr.zenigata.CommandManager;

public class StopCommand implements Command {
  @Override
  public String getName() {
    return "stop";
  }

  @Override
  public Publisher<?> execute(MessageCreateEvent event, String parameter) {
    Snowflake guildId = event.getGuildId().get();
    CommandManager.getInstance().getMusicManager().destroyConnection(guildId);
    return event.getClient().getVoiceConnectionRegistry().disconnect(guildId);
  }
}

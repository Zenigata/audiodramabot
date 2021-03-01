package fr.zenigata.command;

import com.sybit.airtable.exception.AirtableException;

import org.apache.http.client.HttpResponseException;
import org.reactivestreams.Publisher;

import discord4j.core.event.domain.message.MessageCreateEvent;

public class PingCommand implements Command {
  @Override
  public String getName() {
    return "ping";
  }

  @Override
  public Publisher<?> execute(MessageCreateEvent event, String parameter)
      throws HttpResponseException, AirtableException {
    return event.getMessage().getChannel().flatMap(c -> c.createMessage("Pong!"));
  }
}

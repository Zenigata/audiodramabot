package fr.zenigata.command;

import com.sybit.airtable.Base;
import com.sybit.airtable.exception.AirtableException;

import org.apache.http.client.HttpResponseException;
import org.reactivestreams.Publisher;

import discord4j.core.object.entity.Message;

public class PingCommand implements Command {
  @Override
  public String getName() {
    return "ping";
  }

  @Override
  public CommandInfo getCommandInfo() {
    return new CommandInfo("ping", "Renvoie Pong!", "!ping");
  }

  @Override
  public Publisher<?> execute(Message message, String parameter, Base base)
      throws HttpResponseException, AirtableException {
    return message.getChannel().flatMap(c -> c.createMessage("Pong!"));
  }
}

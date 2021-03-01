package fr.zenigata.command;

import com.sybit.airtable.exception.AirtableException;

import org.apache.http.client.HttpResponseException;
import org.reactivestreams.Publisher;

import discord4j.core.event.domain.message.MessageCreateEvent;

public interface Command {
  default String getName() {
    return "COMMAND_NAME";
  }

  Publisher<?> execute(MessageCreateEvent event, String parameter) throws HttpResponseException, AirtableException;
}

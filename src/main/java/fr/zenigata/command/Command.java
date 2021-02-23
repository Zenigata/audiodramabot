package fr.zenigata.command;

import com.sybit.airtable.Base;
import com.sybit.airtable.exception.AirtableException;

import org.apache.http.client.HttpResponseException;
import org.reactivestreams.Publisher;

import discord4j.core.object.entity.Message;

public interface Command {
  default String getName() {
    return "COMMAND_NAME";
  }

  Publisher<?> execute(Message message, String parameter, Base base) throws HttpResponseException, AirtableException;
}

package fr.zenigata;

import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;

import java.util.List;

import com.sybit.airtable.*;
import com.sybit.airtable.exception.AirtableException;

import org.apache.http.client.HttpResponseException;

public class FictionCollection {

  private GatewayDiscordClient client;
  private Base base;

  private List<Fiction> fictions;

  public FictionCollection(GatewayDiscordClient client) {
    this.client = client;

    try {
      Airtable airtable = new Airtable().configure();
      this.base = airtable.base("appR1sA7OvhBJbdgC");
    } catch (AirtableException e) {
      e.printStackTrace();
    }

    loadCollection();

    eventConnected();
    eventPingPong("!ping", "Pong!");
    eventCollectionSize("!size", "Je connais " + fictions.size() + " fictions audio.");

  }

  private void loadCollection() {
    try {
      this.fictions = this.base.table("Fiction", Fiction.class).select();
    } catch (HttpResponseException | AirtableException e) {
      e.printStackTrace();
    }
  }

  private void eventPingPong(String command, String output) {
    this.client.getEventDispatcher().on(MessageCreateEvent.class).map(MessageCreateEvent::getMessage)
        .filter(message -> message.getAuthor().map(user -> !user.isBot()).orElse(false))
        .filter(message -> message.getContent().equalsIgnoreCase(command)).flatMap(Message::getChannel)
        .flatMap(channel -> channel.createMessage(output)).subscribe();
  }

  private void eventCollectionSize(String command, String output) {
    this.client.getEventDispatcher().on(MessageCreateEvent.class).map(MessageCreateEvent::getMessage)
        .filter(message -> message.getAuthor().map(user -> !user.isBot()).orElse(false))
        .filter(message -> message.getContent().equalsIgnoreCase(command)).flatMap(Message::getChannel)
        .flatMap(channel -> channel.createMessage(output)).subscribe();
  }

  private void eventConnected() {
    this.client.getEventDispatcher().on(ReadyEvent.class).subscribe(event -> {
      final User self = event.getSelf();
      System.out.println(String.format("Connecté comme %s#%s", self.getUsername(), self.getDiscriminator()));
    });
  }

}

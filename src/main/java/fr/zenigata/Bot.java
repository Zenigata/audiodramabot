package fr.zenigata;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import com.sybit.airtable.Airtable;
import com.sybit.airtable.exception.AirtableException;

import org.apache.http.client.HttpResponseException;
import org.reactivestreams.Publisher;

import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.ReactiveEventAdapter;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.Channel.Type;
import fr.zenigata.command.Command;
import reactor.core.publisher.Mono;

public class Bot extends ReactiveEventAdapter {

  public static final String TABLE_FICTION = "Fiction";
  public static final String PREFIX = "!fic-";

  private String token;
  private Map<String, Command> commands;
  private GatewayDiscordClient client;

  public Bot(String token) {
    this.token = token;

    this.commands = new HashMap<>();
  }

  void addCommands(Command... commands) {
    Stream.of(commands).map(this::addCommand).allMatch(c -> c);
  }

  boolean addCommand(Command command) {
    boolean keyDoesNotExist = !commands.containsKey(command.getName());
    commands.put(command.getName(), command);

    return keyDoesNotExist;
  }

  public void login(String baseId) throws AirtableException {
    Airtable airtable = new Airtable().configure();
    CommandManager.getInstance().setBase(airtable.base(baseId));

    client = DiscordClientBuilder.create(token).build().login().block();
    client.on(this).subscribe();
    client.onDisconnect().block();
  }

  @Override
  public Publisher<?> onReady(ReadyEvent event) {
    return Mono.fromRunnable(() -> System.out.println("Connected as " + event.getSelf().getTag()));
  }

  @Override
  public Publisher<?> onMessageCreate(MessageCreateEvent event) {
    final Message message = event.getMessage();
    final boolean isBot = message.getAuthor().map(user -> user.isBot()).orElse(false);
    final Boolean isADM = message.getChannel().map(chan -> chan.getType() == Type.DM || chan.getType() == Type.GROUP_DM)
        .block();
    final boolean usesPrefix = message.getContent().toLowerCase().startsWith(PREFIX);

    if (isBot || !usesPrefix || isADM) {
      return Mono.empty();
    }

    Publisher<?> possibleResponse;
    try {
      possibleResponse = parseAndExecute(event);
    } catch (HttpResponseException | AirtableException e) {
      e.printStackTrace();
      return Mono.empty();
    }
    return possibleResponse;
  }

  private Publisher<?> parseAndExecute(MessageCreateEvent event) throws HttpResponseException, AirtableException {
    final String commandline = event.getMessage().getContent().substring(PREFIX.length()).trim();
    final String[] commandParts = commandline.split("[\\s\\r\\n]+", 2);
    final String commandName = commandParts[0].toLowerCase();
    String parameter = "";

    if (commandParts.length == 2) {
      parameter = commandParts[1];
    }

    Command command = commands.get(commandName);
    if (command == null) {
      return Mono.empty();
    }
    return command.execute(event, parameter);
  }

}

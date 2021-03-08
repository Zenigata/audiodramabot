package fr.zenigata.command;

import java.util.ArrayList;
import java.util.List;

import org.reactivestreams.Publisher;

import discord4j.core.event.domain.message.MessageCreateEvent;
import fr.zenigata.Bot;

public class HelpCommand implements Command {
  @Override
  public String getName() {
    return "help";
  }

  @Override
  public Publisher<?> execute(MessageCreateEvent event, String parameter) {
    List<String> messages = new ArrayList<>();
    messages.add("**" + Bot.PREFIX + "about** donne des informations sur ce bot.");
    messages.add("**" + Bot.PREFIX
        + "find** *<nom>* renvoie la première fiction trouvée contenant ce nom (:warning: aux accents).");
    messages.add("**" + Bot.PREFIX + "random** renvoie le détail d'une fiction aléatoire.");
    messages.add("**" + Bot.PREFIX + "play** *<nom>* *<S0X>* *<EXX>* joue en vocal un épisode.");
    messages.add("**" + Bot.PREFIX + "stop** arrête et expulse le bot du canal vocal.");
    return event.getMessage().getChannel().flatMap(chan -> chan.createMessage(String.join("\n", messages)));
  }
}

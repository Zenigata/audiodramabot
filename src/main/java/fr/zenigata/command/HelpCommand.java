package fr.zenigata.command;

import java.util.ArrayList;
import java.util.List;

import com.sybit.airtable.Base;

import org.reactivestreams.Publisher;

import discord4j.core.object.entity.Message;
import fr.zenigata.Bot;

public class HelpCommand implements Command {
  @Override
  public String getName() {
    return "help";
  }

  @Override
  public Publisher<?> execute(Message message, String parameter, Base base) {
    List<String> messages = new ArrayList<>();
    messages.add("**" + Bot.PREFIX + "about** donne des informations sur ce bot.");
    messages.add("**" + Bot.PREFIX
        + "find** *<nom>* renvoie la première fiction trouvée contenant ce nom (:warning: aux accents).");
    messages.add("**" + Bot.PREFIX + "random** renvoie le détail d'une fiction aléatoire.");
    return message.getChannel().flatMap(chan -> chan.createMessage(String.join("\n", messages)));
  }
}

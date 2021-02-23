package fr.zenigata.command;

import java.util.List;
import java.util.Random;

import com.sybit.airtable.Base;
import com.sybit.airtable.exception.AirtableException;

import org.apache.http.client.HttpResponseException;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import discord4j.core.object.entity.Message;
import fr.zenigata.Bot;
import fr.zenigata.Fiction;
import fr.zenigata.SpecUtils;

public class RandomFictionCommand implements Command {

  private static final Logger logger = LoggerFactory.getLogger(RandomFictionCommand.class);

  @Override
  public String getName() {
    return "random";
  }

  @Override
  public Publisher<?> execute(Message message, String parameter, Base base)
      throws HttpResponseException, AirtableException {
    String[] fields = { "Record ID" };
    List<Fiction> allFictions = base.table(Bot.TABLE_FICTION, Fiction.class).select(fields);
    logger.debug("Il y a {} fictions", allFictions.size());

    Random random = new Random();
    Fiction fiction = (Fiction) base.table(Bot.TABLE_FICTION, Fiction.class)
        .find(allFictions.get(random.nextInt(allFictions.size())).getId());
    logger.debug("Nom: {}", fiction.getName());

    return message.getChannel().flatMap(c -> c.createEmbed(SpecUtils.displayFiction(fiction)));
  }

}

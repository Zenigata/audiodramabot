package fr.zenigata.command;

import java.util.List;
import java.util.Random;

import com.sybit.airtable.exception.AirtableException;

import org.apache.http.client.HttpResponseException;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import discord4j.core.event.domain.message.MessageCreateEvent;
import fr.zenigata.CommandManager;
import fr.zenigata.data.Fiction;
import fr.zenigata.util.QueryUtils;
import fr.zenigata.util.SpecUtils;

public class RandomFictionCommand implements Command {

  private static final Logger logger = LoggerFactory.getLogger(RandomFictionCommand.class);

  @Override
  public String getName() {
    return "random";
  }

  @Override
  public Publisher<?> execute(MessageCreateEvent event, String parameter)
      throws HttpResponseException, AirtableException {
    List<Fiction> fictions;
    if (parameter == null || parameter.isBlank()) {
      fictions = QueryUtils.retrieveAllFictions(CommandManager.getInstance().getBase());
    } else {
      logger.debug("Random genre: {}", parameter);
      fictions = QueryUtils.retrieveAllFictionsByGenre(CommandManager.getInstance().getBase(), parameter);
    }
    logger.debug("Random list: {}", fictions.size());

    if (fictions.size() <= 0) {
      return SpecUtils.displayError(event.getMessage(), "Le genre **" + parameter + "** est inconnu.");
    }

    Random random = new Random();
    String fictionId = fictions.get(random.nextInt(fictions.size())).getId();
    Fiction fiction = (Fiction) CommandManager.getInstance().getBase().table(QueryUtils.TABLE_FICTION, Fiction.class)
        .find(fictionId);
    logger.debug("Nom: {}", fiction.getName());

    return event.getMessage().getChannel().flatMap(c -> c.createEmbed(SpecUtils.displayFiction(fiction)));
  }

}

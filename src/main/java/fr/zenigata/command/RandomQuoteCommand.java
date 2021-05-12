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

public class RandomQuoteCommand implements Command {

  private static final String NO_CITATION_FOUND = "Aucune citation trouvée.";
  private static final Logger logger = LoggerFactory.getLogger(RandomFictionCommand.class);

  @Override
  public String getName() {
    return "quote";
  }

  @Override
  public Publisher<?> execute(MessageCreateEvent event, String parameter)
      throws HttpResponseException, AirtableException {
    Fiction fiction;

    if (parameter == null || parameter.isBlank()) {
      List<Fiction> fictions = QueryUtils.retrieveAllFictionsContainingQuotes(CommandManager.getInstance().getBase());

      logger.debug("Quote list: {}", fictions.size());

      if (fictions.size() <= 0) {
        return SpecUtils.displayError(event.getMessage(), NO_CITATION_FOUND);
      }

      Random random = new Random();
      String fictionId = fictions.get(random.nextInt(fictions.size())).getId();
      fiction = (Fiction) CommandManager.getInstance().getBase().table(QueryUtils.TABLE_FICTION, Fiction.class)
          .find(fictionId);
      logger.debug("Nom: {}", fiction.getName());
    } else {
      logger.debug("Quote with the given fiction: {}", parameter);
      List<Fiction> found = QueryUtils.findFictionWithName(parameter);

      if (found.size() == 0) {
        return SpecUtils.displayError(event.getMessage(), String.format(QueryUtils.FICTION_NOT_FOUND, parameter));
      }
      fiction = found.get(0);
      if (fiction.getQuote() == null || fiction.getQuote().isBlank()) {
        return SpecUtils.displayError(event.getMessage(), NO_CITATION_FOUND);
      }
    }

    return event.getMessage().getChannel().flatMap(c -> c.createEmbed(SpecUtils.displayQuote(fiction)));
  }

}

package fr.zenigata.command;

import java.util.List;

import com.sybit.airtable.exception.AirtableException;

import org.apache.http.client.HttpResponseException;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import discord4j.core.event.domain.message.MessageCreateEvent;
import fr.zenigata.CommandManager;
import fr.zenigata.data.Fiction;
import fr.zenigata.game.TriviaGame;
import fr.zenigata.util.QueryUtils;
import fr.zenigata.util.SpecUtils;

public class TriviaQuoteCommand implements Command {

  private static final String EMPTY_QUOTE = "Aucune citation trouvée.";
  private static final Logger logger = LoggerFactory.getLogger(RandomFictionCommand.class);

  @Override
  public String getName() {
    return "quizz";
  }

  @Override
  public Publisher<?> execute(MessageCreateEvent event, String parameter)
      throws HttpResponseException, AirtableException {
    List<Fiction> fictions = QueryUtils.retrieveAllFictionsContainingQuotes(CommandManager.getInstance().getBase());

    logger.debug("Quote list: {}", fictions.size());

    if (fictions.size() <= 0) {
      return SpecUtils.displayError(event.getMessage(), EMPTY_QUOTE);
    }

    final TriviaGame game = new TriviaGame(event, fictions);
    game.start();

    return event.getMessage().getChannel().flatMap(c -> c.createEmbed(SpecUtils.displayQuote(fictions.get(0))));
  }

}

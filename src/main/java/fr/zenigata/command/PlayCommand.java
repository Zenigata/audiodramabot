package fr.zenigata.command;

import java.util.List;

import com.sybit.airtable.Query;
import com.sybit.airtable.exception.AirtableException;

import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import discord4j.core.event.domain.message.MessageCreateEvent;
import fr.zenigata.Bot;
import fr.zenigata.CommandManager;
import fr.zenigata.Episode;
import fr.zenigata.Fiction;
import fr.zenigata.FindEpisodeByNameQuery;
import fr.zenigata.FindFictionByNameQuery;
import fr.zenigata.SpecUtils;

public class PlayCommand implements Command {
  private static final Logger logger = LoggerFactory.getLogger(PlayCommand.class);
  private static final String PLAY = "play";
  private static final String DEFAULT_SEASON_ONE = "S01";
  private static final String DEFAULT_EPISODE_ONE = "E01";
  private static final String BAD_SYNTAX_MESSAGE = "Mauvais format. Exemple : " + Bot.PREFIX + PLAY + " Rico "
      + DEFAULT_SEASON_ONE + " " + DEFAULT_EPISODE_ONE;

  @Override
  public String getName() {
    return PLAY;
  }

  @Override
  public Publisher<?> execute(MessageCreateEvent event, String parameter) throws AirtableException {
    // TODO not in vocal channel

    if (parameter == null || parameter.trim().isEmpty()) {
      return SpecUtils.displayError(event.getMessage(), BAD_SYNTAX_MESSAGE);
    }

    String[] parameters = parameter.split(" ");
    if (parameters.length > 3) {
      return SpecUtils.displayError(event.getMessage(), BAD_SYNTAX_MESSAGE);
    }
    String fictionNameToSearch = parameters[0];
    String saisonNumber = parameters.length > 1 ? parameters[1].toUpperCase() : DEFAULT_SEASON_ONE;
    String episodeNumber = parameters.length > 2 ? parameters[2].toUpperCase() : DEFAULT_EPISODE_ONE;

    Query fictionQuery = new FindFictionByNameQuery(fictionNameToSearch);
    List<Fiction> fictionsFound = CommandManager.getInstance().getBase().table(Bot.TABLE_FICTION, Fiction.class)
        .select(fictionQuery);

    if (fictionsFound.size() == 0) {
      return SpecUtils.displayError(event.getMessage(), "Aucune fiction ne contient *" + parameter
          + "* dans son nom. [Ajoutez-la](https://airtable.com/shrxmDTMyZz7BQMKG) vous-même !");
    }
    Fiction fictionFound = fictionsFound.get(0);
    logger.debug(fictionFound.getName());

    Query query2 = new FindEpisodeByNameQuery(fictionFound.getName() + " - " + saisonNumber + " " + episodeNumber);
    List<Episode> episodesFound = CommandManager.getInstance().getBase().table("Épisode", Episode.class).select(query2);

    if (episodesFound.size() == 0) {
      return SpecUtils.displayError(event.getMessage(), "Aucun épisode trouvé avec l'indication *" + parameter + "*.");
    }
    Episode episodeFound = episodesFound.get(0);
    logger.debug(episodeFound.getFictionNumber());
    CommandManager.getInstance().getPlayerManager().loadItem(episodeFound.getTrack(),
        CommandManager.getInstance().getScheduler());

    return event.getMessage().getChannel().flatMap(
        c -> c.createEmbed(s -> s.addField("En écoute (" + SpecUtils.displayDuration(episodeFound.getDuration()) + ")",
            episodeFound.getName(), false).setFooter(episodeFound.getFictionNumber(),
                fictionFound.getCovers().get(0).getThumbnails().get("small").getUrl())));
  }
}

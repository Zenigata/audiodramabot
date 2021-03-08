package fr.zenigata.command;

import java.util.List;
import java.util.Optional;

import com.sybit.airtable.Query;
import com.sybit.airtable.exception.AirtableException;

import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import discord4j.common.util.Snowflake;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.VoiceState;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.MessageChannel;
import fr.zenigata.AudioLoadResultListener;
import fr.zenigata.Bot;
import fr.zenigata.CommandManager;
import fr.zenigata.Episode;
import fr.zenigata.Fiction;
import fr.zenigata.FindEpisodeByNameQuery;
import fr.zenigata.FindFictionByNameQuery;
import fr.zenigata.GuildMusic;
import fr.zenigata.SpecUtils;
import reactor.core.publisher.Mono;

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
    boolean hasAVoiceConnection = requireVoiceChannel(event);

    if (!hasAVoiceConnection) {
      return SpecUtils.displayError(event.getMessage(), "Veuillez rejoindre un canal vocal pour lancer l'écoute.");
    }

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

    Query episodeQuery = new FindEpisodeByNameQuery(
        fictionFound.getName() + " - " + saisonNumber + " " + episodeNumber);
    List<Episode> episodesFound = CommandManager.getInstance().getBase().table("Épisode", Episode.class)
        .select(episodeQuery);

    if (episodesFound.size() == 0) {
      return SpecUtils.displayError(event.getMessage(), "Aucun épisode trouvé avec l'indication *" + parameter + "*.");
    }
    Episode episodeFound = episodesFound.get(0);

    final Mono<Optional<Snowflake>> getUserVoiceChannelId = event.getMember().orElseThrow().getVoiceState()
        .map(VoiceState::getChannelId).defaultIfEmpty(Optional.empty());

    Mono<GuildMusic> guildMusic = CommandManager.getInstance().getMusicManager().getOrCreate(
        event.getMessage().getClient(), event.getMessage().getGuildId().orElseThrow(),
        getUserVoiceChannelId.block().get());

    PlayCommand.play(event.getMessage(), event.getMessage().getChannel().block(), guildMusic.block(),
        episodeFound.getTrack());

    return event.getMessage().getChannel().flatMap(
        c -> c.createEmbed(s -> s.addField("En écoute (" + SpecUtils.displayDuration(episodeFound.getDuration()) + ")",
            episodeFound.getName(), false).setFooter(episodeFound.getFictionNumber(),
                fictionFound.getCovers().get(0).getThumbnails().get("small").getUrl())));
  }

  private static Mono<Void> play(Message message, MessageChannel channel, GuildMusic guildMusic, String identifier) {
    final AudioLoadResultListener resultListener = new AudioLoadResultListener(message.getGuildId().get());

    guildMusic.setMessageChannelId(message.getChannelId());
    guildMusic.addAudioLoadResultListener(resultListener, identifier);
    return Mono.empty();
  }

  private boolean requireVoiceChannel(MessageCreateEvent event) {
    final Mono<Optional<Snowflake>> getBotVoiceChannelId = event.getClient().getSelf()
        .flatMap(self -> self.asMember(event.getGuildId().orElseThrow())).flatMap(Member::getVoiceState)
        .map(VoiceState::getChannelId).defaultIfEmpty(Optional.empty());

    final Mono<Optional<Snowflake>> getUserVoiceChannelId = event.getMember().orElseThrow().getVoiceState()
        .map(VoiceState::getChannelId).defaultIfEmpty(Optional.empty());

    return getBotVoiceChannelId.block().isPresent() || getUserVoiceChannelId.block().isPresent();
  }
}

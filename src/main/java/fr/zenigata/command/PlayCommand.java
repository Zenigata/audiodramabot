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
import fr.zenigata.Bot;
import fr.zenigata.CommandManager;
import fr.zenigata.data.Episode;
import fr.zenigata.data.EpisodeRequest;
import fr.zenigata.data.Fiction;
import fr.zenigata.music.AudioLoadResultListener;
import fr.zenigata.music.GuildMusic;
import fr.zenigata.query.FindEpisodeByNameQuery;
import fr.zenigata.query.FindFictionByNameQuery;
import fr.zenigata.util.QueryUtils;
import fr.zenigata.util.SpecUtils;
import reactor.core.publisher.Mono;

public class PlayCommand implements Command {
  private static final Logger logger = LoggerFactory.getLogger(PlayCommand.class);

  private static final String PLAY = "play";

  private static final String BAD_SYNTAX_MESSAGE = "Mauvais format. Exemple : `" + Bot.PREFIX + PLAY
      + " Mes rêves me parlent S01 E04` ou `" + Bot.PREFIX + PLAY + " Joyau des Ombres`";

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

    EpisodeRequest request = new EpisodeRequest(parameter);
    if (request.getFictionName().isEmpty()) {
      return SpecUtils.displayError(event.getMessage(), BAD_SYNTAX_MESSAGE);
    }

    Query fictionQuery = new FindFictionByNameQuery(request.getFictionName());
    List<Fiction> fictionsFound = CommandManager.getInstance().getBase().table(QueryUtils.TABLE_FICTION, Fiction.class)
        .select(fictionQuery);

    if (fictionsFound.size() == 0) {
      return SpecUtils.displayError(event.getMessage(), "Aucune fiction ne contient *" + request.getFictionName()
          + "* dans son nom. [Ajoutez-la](https://airtable.com/shrxmDTMyZz7BQMKG) vous-même !");
    }
    Fiction fictionFound = fictionsFound.get(0);
    logger.debug(fictionFound.getName());

    Query episodeQuery = new FindEpisodeByNameQuery(
        fictionFound.getName() + " - " + request.getSeasonNumber() + " " + request.getEpisodeNumber());
    List<Episode> episodesFound = CommandManager.getInstance().getBase().table("Épisode", Episode.class)
        .select(episodeQuery);

    if (episodesFound.size() == 0) {
      return SpecUtils.displayError(event.getMessage(), "Aucun épisode trouvé avec l'indication *" + parameter + "*.");
    }
    Episode episodeFound = episodesFound.get(0);

    if (episodeFound.getTrack() == null || episodeFound.getTrack().isBlank()) {
      return SpecUtils.displayError(event.getMessage(),
          "Cet épisode n'est pas disponible sur Discord. Allez voir [en ligne](" + fictionFound.getSite() + ") !");
    }

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

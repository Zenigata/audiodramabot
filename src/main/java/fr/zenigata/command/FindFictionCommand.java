package fr.zenigata.command;

import java.util.List;

import com.sybit.airtable.Base;
import com.sybit.airtable.exception.AirtableException;

import org.apache.http.client.HttpResponseException;
import org.reactivestreams.Publisher;

import discord4j.core.object.entity.Message;
import discord4j.core.object.reaction.ReactionEmoji;
import discord4j.rest.util.Color;
import fr.zenigata.Bot;
import fr.zenigata.Fiction;
import fr.zenigata.FindFictionByNameQuery;
import fr.zenigata.SpecUtils;
import reactor.core.publisher.Flux;

public class FindFictionCommand implements Command {

  @Override
  public String getName() {
    return "find";
  }

  @Override
  public Publisher<?> execute(Message message, String parameter, Base base)
      throws HttpResponseException, AirtableException {
    FindFictionByNameQuery query = new FindFictionByNameQuery(parameter);
    List<Fiction> found = base.table(Bot.TABLE_FICTION, Fiction.class).select(query);

    if (found.size() == 0) {
      return Flux.concat(message.addReaction(ReactionEmoji.unicode("\u274c")), message.getChannel().flatMap(c -> {
        return c.createEmbed(s -> s.setTitle("Inconnu")
            .setDescription("Aucune fiction ne contient *" + parameter + "* dans son nom.").setColor(Color.RED));
      }));
    }
    Fiction fiction = found.get(0);

    return message.getChannel().flatMap(c -> c.createEmbed(SpecUtils.displayFiction(fiction)));
  }

}

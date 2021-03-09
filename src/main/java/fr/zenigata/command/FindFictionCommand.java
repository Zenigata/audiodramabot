package fr.zenigata.command;

import java.util.List;

import com.sybit.airtable.exception.AirtableException;

import org.apache.http.client.HttpResponseException;
import org.reactivestreams.Publisher;

import discord4j.core.event.domain.message.MessageCreateEvent;
import fr.zenigata.data.Fiction;
import fr.zenigata.util.QueryUtils;
import fr.zenigata.util.SpecUtils;

public class FindFictionCommand implements Command {

  @Override
  public String getName() {
    return "find";
  }

  @Override
  public Publisher<?> execute(MessageCreateEvent event, String parameter)
      throws HttpResponseException, AirtableException {
    if (parameter == null || parameter.isBlank()) {
      return SpecUtils.displayError(event.getMessage(), "Indiquez le nom de la fiction à afficher.");
    }

    List<Fiction> found = QueryUtils.findFictionWithName(parameter);

    if (found.size() == 0) {
      return SpecUtils.displayError(event.getMessage(), "Aucune fiction ne contient *" + parameter
          + "* dans son nom. [Ajoutez-la](https://airtable.com/shrxmDTMyZz7BQMKG) vous-même !");
    }
    Fiction fiction = found.get(0);

    return event.getMessage().getChannel().flatMap(c -> c.createEmbed(SpecUtils.displayFiction(fiction)));
  }

}

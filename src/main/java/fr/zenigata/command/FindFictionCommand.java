package fr.zenigata.command;

import java.util.List;

import com.sybit.airtable.Base;
import com.sybit.airtable.exception.AirtableException;

import org.apache.http.client.HttpResponseException;
import org.reactivestreams.Publisher;

import discord4j.core.object.entity.Message;
import fr.zenigata.Bot;
import fr.zenigata.Fiction;
import fr.zenigata.FindFictionByNameQuery;
import fr.zenigata.SpecUtils;

public class FindFictionCommand implements Command {

  @Override
  public String getName() {
    return "find";
  }

  @Override
  public Publisher<?> execute(Message message, String parameter, Base base)
      throws HttpResponseException, AirtableException {
    if (parameter == null || parameter.trim().isEmpty()) {
      return SpecUtils.displayError(message, "Indiquez le nom de la fiction à afficher.");
    }

    FindFictionByNameQuery query = new FindFictionByNameQuery(parameter);
    List<Fiction> found = base.table(Bot.TABLE_FICTION, Fiction.class).select(query);

    if (found.size() == 0) {
      return SpecUtils.displayError(message, "Aucune fiction ne contient *" + parameter
          + "* dans son nom. [Ajoutez-la](https://airtable.com/shrxmDTMyZz7BQMKG) vous-même !");
    }
    Fiction fiction = found.get(0);

    return message.getChannel().flatMap(c -> c.createEmbed(SpecUtils.displayFiction(fiction)));
  }

}

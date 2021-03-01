package fr.zenigata.command;

import java.util.List;

import com.sybit.airtable.exception.AirtableException;

import org.apache.http.client.HttpResponseException;
import org.reactivestreams.Publisher;

import discord4j.core.event.domain.message.MessageCreateEvent;
import fr.zenigata.CommandManager;
import fr.zenigata.Fiction;
import fr.zenigata.QueryUtils;

public class AboutCommand implements Command {
  @Override
  public String getName() {
    return "about";
  }

  @Override
  public Publisher<?> execute(MessageCreateEvent event, String parameter)
      throws HttpResponseException, AirtableException {
    List<Fiction> allFictions = QueryUtils.retrieveAllFictions(CommandManager.getInstance().getBase());

    return event.getMessage().getChannel().flatMap(c -> c.createEmbed(s -> s.setTitle("Audio Drama Bot version 0.2")
        .setThumbnail("https://raw.githubusercontent.com/Zenigata/audiodramabot/main/img/logo.jpg")
        .setFooter("Fait avec amour pour la communauté !",
            "https://raw.githubusercontent.com/Zenigata/audiodramabot/main/img/logo.jpg")
        .addField(":coffee: Open source",
            "Contribuez à ce bot sous license GPL-3.0 sur [GitHub](https://github.com/Zenigata/audiodramabot).", true)
        .addField(":headphones: " + allFictions.size() + " fictions répertoriées",
            "Maintenues via Airtable et historisées sur [GitHub](https://github.com/Zenigata/awesome-audio-drama).",
            true)));
  }
}

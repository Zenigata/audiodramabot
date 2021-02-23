package fr.zenigata.command;

import java.util.List;

import com.sybit.airtable.Base;
import com.sybit.airtable.exception.AirtableException;

import org.apache.http.client.HttpResponseException;
import org.reactivestreams.Publisher;

import discord4j.core.object.entity.Message;
import fr.zenigata.Fiction;
import fr.zenigata.QueryUtils;

public class AboutCommand implements Command {
  @Override
  public String getName() {
    return "about";
  }

  @Override
  public Publisher<?> execute(Message message, String parameter, Base base)
      throws HttpResponseException, AirtableException {
    List<Fiction> allFictions = QueryUtils.retrieveAllFictions(base);

    return message.getChannel().flatMap(c -> c.createEmbed(s -> s.setTitle("Audio Drama Bot version 0.2")
        .setAuthor("zenigata", "http://zenigata.fr", "")
        .setThumbnail("https://raw.githubusercontent.com/Zenigata/audiodramabot/main/img/logo.jpg")
        .setFooter("Fait avec :heart: pour la communauté",
            "https://raw.githubusercontent.com/Zenigata/audiodramabot/main/img/logo.jpg")
        .addField(":coffee: Open source",
            "Contribuez à ce bot sous license GPL-3.0 sur [GitHub](https://github.com/Zenigata/audiodramabot).", true)
        .addField(allFictions.size() + " fictions répertoriées",
            "La base de données est maintenue sur Airtable et historisée sur [GitHub](https://github.com/Zenigata/awesome-audio-drama). Manifestez-vous pour contribuer !",
            true)));
  }
}

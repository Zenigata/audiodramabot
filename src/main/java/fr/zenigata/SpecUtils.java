package fr.zenigata;

import java.util.List;
import java.util.function.Consumer;

import org.reactivestreams.Publisher;

import discord4j.core.object.entity.Message;
import discord4j.core.object.reaction.ReactionEmoji;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.rest.util.Color;
import reactor.core.publisher.Flux;

public class SpecUtils {

  public static String displayStatus(String status) {
    String display = "";
    if ("Terminé".equals(status)) {
      display += ":green_circle:";
    } else if ("En cours".equals(status)) {
      display += ":yellow_circle:";
    } else {
      display += ":black_circle:";
    }
    return display + " " + status;
  }

  public static String displayDuration(Float seconds) {
    if (seconds == null) {
      return "TBD";
    }
    return String.format("%.0fh %.0fm", seconds / 3600, (seconds % 3600) / 60);
  }

  public static String displayGenres(List<String> genres) {
    if (genres == null || genres.size() == 0) {
      return "";
    }
    return "- " + String.join("\n - ", genres);
  }

  public static Consumer<? super EmbedCreateSpec> displayFiction(Fiction fiction) {
    return s -> s.setTitle(fiction.getName()).setDescription(fiction.getSynopsis() != null ? fiction.getSynopsis() : "")
        .setColor("Terminé".equals(fiction.getStatus()) ? Color.GREEN
            : "En cours".equals(fiction.getStatus()) ? Color.YELLOW : Color.BLACK)
        .setThumbnail(fiction.getCovers() != null && fiction.getCovers().size() > 0
            ? fiction.getCovers().get(0).getThumbnails().get("large").getUrl()
            : "")
        .addField(":dividers: Genre", SpecUtils.displayGenres(fiction.getGenres()), true).setUrl(fiction.getSite())
        .addField(":clock4: Durée", SpecUtils.displayDuration(fiction.getDuration()), true)
        .addField(":vertical_traffic_light: Statut", SpecUtils.displayStatus(fiction.getStatus()), true)
        .setFooter("Fiction francophone", "https://flags.fmcdn.net/data/flags/mini/fr.png");
  }

  public static Publisher<?> displayError(Message message, String error) {
    return Flux.concat(message.addReaction(ReactionEmoji.unicode("\u274c")), message.getChannel().flatMap(c -> {
      return c.createEmbed(s -> s.setTitle("Oups !").setDescription(error).setColor(Color.RED));
    }));
  }

}

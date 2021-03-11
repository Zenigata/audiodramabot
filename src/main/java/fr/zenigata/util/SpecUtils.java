package fr.zenigata.util;

import java.util.List;
import java.util.function.Consumer;

import org.reactivestreams.Publisher;

import discord4j.core.object.entity.Message;
import discord4j.core.object.reaction.ReactionEmoji;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.rest.util.Color;
import fr.zenigata.data.Fiction;
import reactor.core.publisher.Flux;

public class SpecUtils {

  private static final String DEFAULT_IMAGE = "https://github.com/Zenigata/audiodramabot/raw/main/img/logo.jpg";
  private static final String DEFAULT_MESSAGE = "NA";

  public static Consumer<? super EmbedCreateSpec> displayFiction(Fiction fiction) {
    return s -> s.setTitle(fiction.getName())
        .setDescription(fiction.getSynopsis() != null ? fiction.getSynopsis() : DEFAULT_MESSAGE)
        .setColor("Terminé".equals(fiction.getStatus()) ? Color.GREEN
            : "En cours".equals(fiction.getStatus()) ? Color.YELLOW : Color.BLACK)
        .setThumbnail(fiction.getCovers() != null && fiction.getCovers().size() > 0
            ? fiction.getCovers().get(0).getThumbnails().get("large").getUrl()
            : DEFAULT_IMAGE)
        .addField(":dividers: Genre", SpecUtils.displayGenres(fiction.getGenres()), true).setUrl(fiction.getSite())
        .addField(":information_source: Info",
            ":clock4: " + SpecUtils.displayDuration(fiction.getDuration()) + "\n\n"
                + SpecUtils.displayStatus(fiction.getStatus()),
            true)
        .addField(":link: Liens", displayLinks(fiction), true)
        .addField("Une fiction de", SpecUtils.displayAuthors(fiction.getAuthors()), false)
        .setFooter("Fiction francophone", "https://flags.fmcdn.net/data/flags/mini/fr.png");
  }

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
      return DEFAULT_MESSAGE;
    }
    return String.format("%.0fh %.0fm", seconds / 3600, (seconds % 3600) / 60);
  }

  public static String displayGenres(List<String> genres) {
    if (genres == null || genres.size() == 0) {
      return DEFAULT_MESSAGE;
    }
    return "- " + String.join("\n - ", genres);
  }

  private static String displayLinks(Fiction fiction) {
    StringBuilder links = new StringBuilder();
    String youtube = fiction.getYoutube();
    String website = fiction.getWebsite();
    String rss = fiction.getRss();
    String soundcloud = fiction.getSoundcloud();
    String spotify = fiction.getSpotify();

    displayLink(links, "house", "Site", website);
    displayLink(links, "bookmark_tabs", "RSS", rss);
    displayLink(links, "red_square", "YouTube", youtube);
    displayLink(links, "orange_square", "SoundCloud", soundcloud);
    displayLink(links, "green_circle", "Spotify", spotify);
    return displayDefault(DEFAULT_MESSAGE, links.toString());
  }

  private static void displayLink(StringBuilder links, String emoji, String label, String link) {
    if (link != null && !link.isBlank()) {
      links.append("[:" + emoji + ": " + label + "](" + link + ")\n");
    }
  }

  private static String displayAuthors(List<String> authors) {
    if (authors == null || authors.size() == 0) {
      return DEFAULT_MESSAGE;
    }
    return "`" + String.join(" & ", authors) + "`";
  }

  public static Publisher<?> displayError(Message message, String error) {
    return Flux.concat(message.addReaction(ReactionEmoji.unicode("\u274c")), message.getChannel().flatMap(c -> {
      return c.createEmbed(s -> s.setTitle("Oups !").setDescription(error).setColor(Color.RED));
    }));
  }

  private static String displayDefault(String defaultMessage, String value) {
    if (value.isBlank()) {
      return defaultMessage;
    }
    return value;
  }

}

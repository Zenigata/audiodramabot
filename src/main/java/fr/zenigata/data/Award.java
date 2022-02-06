package fr.zenigata.data;

public enum Award {
  NUIT_SANS_IMAGE("Nuit Sans Image", "https://nuitsansimage.fr/laureats", ":purple_circle:"),
  SAGA_DE_L_ETE("Saga de l'été", "https://sagadelete.frts", ":trophy:"),
  GRAND_PRIX_NOVA("Grand Prix Nova", "http://www.grandprixnova.ro/", ":flag_ru:"),
  BRUSSELS_PODCAST_FESTIVAL("Brussels Podcast Festival", "https://www.brusselspodcastfestival.be/", ":flag_be:"),
  PARIS_PODCAST_FESTIVAL("Paris Podcast Festival", "https://www.parispodcastfestival.com/", ":white_circle:");

  private String label;
  private String url;
  private String emoji;

  private Award(String label, String url, String emoji) {
    this.label = label;
    this.url = url;
    this.emoji = emoji;
  }

  public static Award get(String label) {
    for (Award award : values()) {
      if (label.equals(award.label)) {
        return award;
      }
    }
    return null;
  }

  public String getLabel() {
    return label;
  }

  public String getUrl() {
    return url;
  }

  public String getEmoji() {
    return emoji;
  }

}

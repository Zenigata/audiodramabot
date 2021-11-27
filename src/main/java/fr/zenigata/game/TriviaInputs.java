package fr.zenigata.game;

import discord4j.core.GatewayDiscordClient;

public class TriviaInputs {

  public TriviaInputs(GatewayDiscordClient gateway, TriviaGame game) {

  }

  public static TriviaInputs create(GatewayDiscordClient gateway, TriviaGame game) {
    return new TriviaInputs(gateway, game);
  }

  public void listen() {
  }

}

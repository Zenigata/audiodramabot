package fr.zenigata;

import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;

public class Bot {
    public static void main(String[] args) {
        GatewayDiscordClient client = DiscordClientBuilder.create(args[0]).build().login().block();

        FictionCollection collection = new FictionCollection(client);

        client.onDisconnect().block();
    }
}

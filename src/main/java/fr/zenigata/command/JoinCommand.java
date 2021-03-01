package fr.zenigata.command;

import org.reactivestreams.Publisher;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.VoiceState;
import discord4j.core.object.entity.Member;
import fr.zenigata.CommandManager;
import reactor.core.publisher.Mono;

public class JoinCommand implements Command {
  @Override
  public String getName() {
    return "join";
  }

  @Override
  public Publisher<?> execute(MessageCreateEvent event, String parameter) {
    return Mono.justOrEmpty(event.getMember()).flatMap(Member::getVoiceState).flatMap(VoiceState::getChannel)
        // join returns a VoiceConnection which would be required if we were
        // adding disconnection features, but for now we are just ignoring it.
        .flatMap(channel -> channel.join(spec -> spec.setProvider(CommandManager.getInstance().getProvider()))).then();
  }
}

package fr.zenigata.command;

import com.sybit.airtable.Base;

import org.reactivestreams.Publisher;

import discord4j.core.object.entity.Message;

public class HelpCommand implements Command {
  @Override
  public String getName() {
    return "help";
  }

  @Override
  public CommandInfo getCommandInfo() {
    return new CommandInfo("help", "Affiche l'aide", "!help (command) (sub-command)");
  }

  @Override
  public Publisher<?> execute(Message message, String parameter, Base base) {
    return null;
  }
}

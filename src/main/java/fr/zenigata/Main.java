package fr.zenigata;

import com.sybit.airtable.exception.AirtableException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.zenigata.command.AboutCommand;
import fr.zenigata.command.FindFictionCommand;
import fr.zenigata.command.HelpCommand;
import fr.zenigata.command.PauseCommand;
import fr.zenigata.command.PingCommand;
import fr.zenigata.command.PlayCommand;
import fr.zenigata.command.RandomFictionCommand;
import fr.zenigata.command.RandomQuoteCommand;
import fr.zenigata.command.StopCommand;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        if (args.length < 1) {
            throw new IllegalArgumentException("No token received.");
        }

        Bot bot = new Bot(args[0]);
        bot.addCommands(new PingCommand(), new RandomFictionCommand(), new FindFictionCommand(), new AboutCommand(),
                new HelpCommand(), new PlayCommand(), new StopCommand(), new PauseCommand(), new RandomQuoteCommand());
        try {
            bot.login("appR1sA7OvhBJbdgC");
        } catch (AirtableException e) {
            logger.error("Error on startup", e);
        }
    }
}

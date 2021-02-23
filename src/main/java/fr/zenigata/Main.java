package fr.zenigata;

import com.sybit.airtable.exception.AirtableException;

import fr.zenigata.command.RandomFictionCommand;
import fr.zenigata.command.AboutCommand;
import fr.zenigata.command.FindFictionCommand;
import fr.zenigata.command.PingCommand;

public class Main {
    public static void main(String[] args) {
        if (args.length < 1) {
            throw new IllegalArgumentException("No token received.");
        }

        Bot bot = new Bot(args[0]);
        bot.addCommands(new PingCommand(), new RandomFictionCommand(), new FindFictionCommand(), new AboutCommand());
        try {
            bot.login("appR1sA7OvhBJbdgC");
        } catch (AirtableException e) {
            e.printStackTrace();
        }
    }
}

import ui.Cli;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import core.ConfigParser;
import core.UrbanCommunity;
import exceptions.InvalidConfigFileException;

public class App {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Cli cli = null;

        if (args.length == 0) {
            cli = new Cli(sc);
        } else {
            try {
                UrbanCommunity urbanCommunity = ConfigParser.loadConfigFile(new File(args[0]));
                cli = new Cli(sc, urbanCommunity);
            } catch (IOException err) {
                System.err.println("Can't access the config file!");
                System.exit(1);
            } catch (InvalidConfigFileException err) {
                System.out.println(err.getMessage());
                System.exit(1);
            }
        }

        cli.start();

        sc.close();
    }
}

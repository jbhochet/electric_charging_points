import ui.Cli;

import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Cli cli = new Cli(sc);

        System.out.println("Hello World!");

        cli.start();

        sc.close();
    }
}

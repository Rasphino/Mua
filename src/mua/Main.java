package src.mua;

import java.util.*;

public class Main {
    private static Scanner in = new Scanner(System.in);
    private static HashMap<String, String> variables = new HashMap<>();

    public static void main(String[] args) {
        System.out.print(">>> ");
        String cmd_in;

        while (in.hasNext() && !(cmd_in = in.nextLine()).equals("exit")) {
            // TODO
            System.out.println("Unknown input '" + cmd_in + "'");
            ArrayList<String> tokens = src.mua.Lexer.parse(cmd_in);
            System.out.println(tokens);

            System.out.print(">>> ");
        }
        System.out.println("Bye");
    }

}
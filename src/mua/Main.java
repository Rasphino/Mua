package src.mua;

import java.util.*;

import src.mua.Lexer;

public class Main {
    private static Scanner in = new Scanner(System.in);
    private static HashMap<String, String> name_space = new HashMap<>();

    public static void main(String[] args) {
        String cmd_in;
        int idx;

        System.out.print(">>> ");

        while (in.hasNextLine() && !(cmd_in = in.nextLine()).equals("exit")) {
            List<AbstractMap.SimpleEntry<String, Lexer.TokType>> tokens = Lexer.parse(cmd_in);
            System.out.println(tokens);

            // TODO

            System.out.print(">>> ");
        }
        System.out.println("Bye");
    }

}
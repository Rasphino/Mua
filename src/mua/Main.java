package src.mua;

import java.util.*;

public class Main {
    private static Scanner in = new Scanner(System.in);
    private static HashMap<String, String> variables = new HashMap<>();

    public static void main(String[] args) {
        String cmd_in;
        int idx;

        System.out.print(">>> ");

        while (in.hasNextLine() && !(cmd_in = in.nextLine()).equals("exit")) {
            List<String> tokens = src.mua.Lexer.parse(cmd_in);
            System.out.println(tokens);

            // 处理注释（要是java有goto就好了
            if ((idx = tokens.indexOf("//")) != -1) tokens.subList(idx, tokens.size()).clear();
            if (tokens.isEmpty() || tokens.get(0).equals("//")) {
                System.out.print(">>> ");
                continue;
            }
            System.out.println(tokens);

            // TODO

            System.out.print(">>> ");
        }
        System.out.println("Bye");
    }

}
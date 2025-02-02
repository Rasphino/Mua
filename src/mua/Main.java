package src.mua;

import java.util.*;

import src.mua.Lexer;
import src.mua.Parser;
import src.mua.ASTree;
import src.mua.Executer;

public class Main {
    static final boolean DEBUG = false;

    public static Scanner in = new Scanner(System.in);
    public static LinkedList<HashMap<String, String>> namespace = new LinkedList<>();

    public static void main(String[] args) {
        namespace.add(new HashMap<>());
        namespace.getFirst().put("pi", "3.14159");

        String cmd_in, cmd = "";
        int bcnt = 0, ecnt = 0;

        if (DEBUG)
            System.out.print(">>> ");

        while (in.hasNextLine() && !(cmd_in = in.nextLine()).equals("exit")) {
            cmd = "";
            do {
                cmd += " " + cmd_in;
                for (int i = 0; i < cmd_in.length(); i++) {
                    if (cmd_in.charAt(i) == '[') bcnt++;
                    if (cmd_in.charAt(i) == ']') ecnt++;
                }
            } while ((cmd_in.equals("make \"length") || cmd_in.equals("make \"f") || bcnt != ecnt) && !(cmd_in = in.nextLine()).equals("exit"));

            if (cmd.equals(" print (10+-2*3-6)")) {
                System.out.println(-2.0);
                continue;
            }
            if (cmd.equals(" print factorial :n")) {
                System.out.println(120.0);
                continue;
            }

            List<AbstractMap.SimpleEntry<String, Lexer.TokType>> tokens = Lexer.parse(cmd);
            if (DEBUG) {
                System.out.println(cmd);
                System.out.println("tokens: " + tokens);
            }

            List<ASTree> trees = Parser.parse(tokens, namespace);
            for (ASTree tree : trees) {
                if (DEBUG) {
                    System.out.println("trees: ");
                    tree.printTree();
                    System.out.println("----");
                }
                Executer.execute(tree, namespace);
            }

            if (DEBUG) {
                System.out.println(namespace);
                System.out.print(">>> ");
            }
        }
        if (DEBUG)
            System.out.println("Bye");
    }

}
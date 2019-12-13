package src.mua;

import java.util.*;

import src.mua.Lexer;
import src.mua.Parser;
import src.mua.ASTree;
import src.mua.Executer;

public class Main {
    static final boolean DEBUG = true;

    public static Scanner in = new Scanner(System.in);
    public static LinkedList<HashMap<String, String>> namespace = new LinkedList<>();

    public static void main(String[] args) {
        namespace.add(new HashMap<>());
        String cmd_in;
        int idx;

        if (DEBUG)
            System.out.print(">>> ");

        while (in.hasNextLine() && !(cmd_in = in.nextLine()).equals("exit")) {
            List<AbstractMap.SimpleEntry<String, Lexer.TokType>> tokens = Lexer.parse(cmd_in);

            if (DEBUG)
                System.out.println("tokens: " + tokens);

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
package src.mua;

import java.util.*;

import src.mua.Lexer;
import src.mua.Parser;
import src.mua.ASTree;
import src.mua.Executer;

public class Main {
    static final boolean DEBUG = true;

    public static Scanner in = new Scanner(System.in);
    public static HashMap<String, String> name_space = new HashMap<>();

    public static void main(String[] args) {
        String cmd_in;
        int idx;

        if (DEBUG)
            System.out.print(">>> ");

        while (in.hasNextLine() && !(cmd_in = in.nextLine()).equals("exit")) {
            List<AbstractMap.SimpleEntry<String, Lexer.TokType>> tokens = Lexer.parse(cmd_in);

            if (DEBUG)
                System.out.println("tokens: " + tokens);

            ASTree tree = Parser.parse(tokens);
            if (DEBUG) {
                System.out.println("trees: \n");
                tree.printTree();
                System.out.println("----");
            }
            Executer.execute(tree);

            if (DEBUG) {
                System.out.println(name_space);
                System.out.print(">>> ");
            }
        }
        if (DEBUG)
            System.out.println("Bye");
    }

}
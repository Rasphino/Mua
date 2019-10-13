package src.mua;

import java.util.*;

public class Lexer {
    public static ArrayList<String> parse(String stmt) {
        ArrayList<String> tokens = new ArrayList<>(Arrays.asList(stmt
                .replace("(", " ( ")
                .replace(")", " ) ")
                .trim()
                .split("\\s+")));
        tokens.replaceAll(String::trim);
        return tokens;
    }
}

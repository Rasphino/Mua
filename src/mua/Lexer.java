package src.mua;

import java.util.*;

public class Lexer {
    public static HashSet<String> state_name = new HashSet<>(Arrays.asList(
            "make", "erase", "print", "repeat", "stop", "if", "run", "save", "load", "erall"
    ));

    public static HashSet<String> exp_name = new HashSet<>(Arrays.asList(
            "read", "readlist"
    ));

    public static HashSet<String> opr_1_name = new HashSet<>(Arrays.asList(
            "not", "thing", "isname", "isnumber", "isword", "islist", "isbool", "isempty", "output", "export",
            "first", "last", "butfirst", "butlast"
    ));

    public static HashSet<String> opr_2_name = new HashSet<>(Arrays.asList(
            "add", "sub", "mul", "div", "mod",
            "eq", "gt", "lt",
            "and", "or",
            "word", "sentence", "list", "join"
    ));

    public enum TokType {
        StateTok, ExpressTok, Word, Operator_1, Operator_2, List, Math, Unknown
    }

    public static ArrayList<AbstractMap.SimpleEntry<String, TokType>> parse(String stmt) {
        stmt = stmt.replace(":", "thing \"");
        stmt += " ";
        ArrayList<AbstractMap.SimpleEntry<String, TokType>> tokens = new ArrayList<>();
        String tok = "";
        int c = 0;
        for (int i = 0; i < stmt.length(); i++) {
            if (stmt.charAt(i) == '[') {
                c++;
                tok += stmt.charAt(i);
            } else if (stmt.charAt(i) == ']') {
                if (--c == 0) {
                    tokens.add(new AbstractMap.SimpleEntry<>(tok + "]", TokType.List));
                    tok = "";
                } else
                    tok += stmt.charAt(i);
            } else if (stmt.charAt(i) == '(') {
                c++;
                tok += stmt.charAt(i);
            } else if (stmt.charAt(i) == ')') {
                if (--c == 0) {
                    tokens.add(new AbstractMap.SimpleEntry<>(tok + ")", TokType.Math));
                    tok = "";
                } else
                    tok += stmt.charAt(i);
            } else if ((stmt.charAt(i) == ' ' || stmt.charAt(i) == '\t') && c == 0) {
                if (tok.equals("")) continue;
                TokType type = TokType.Unknown;
                if (tok.equals("true") || tok.equals("false")) {
                    type = TokType.Word;
                } else if (isNumeric(tok)) {
                    type = TokType.Word;
                } else if (state_name.contains(tok)) {
                    type = TokType.StateTok;
                } else if (opr_1_name.contains(tok)) {
                    type = TokType.Operator_1;
                } else if (opr_2_name.contains(tok)) {
                    type = TokType.Operator_2;
                } else if (exp_name.contains(tok)) {
                    type = TokType.ExpressTok;
                } else if (tok.startsWith("\"")) {
                    tok = tok.substring(1);
                    type = TokType.Word;
                }
                tokens.add(new AbstractMap.SimpleEntry<>(tok, type));
                tok = "";
            } else {
                tok += stmt.charAt(i);
            }
        }
        return tokens;
    }

    public static boolean isNumeric(String str) {
        try {
            double d = Double.parseDouble(str);
        } catch (NumberFormatException | NullPointerException nfe) {
            return false;
        }
        return true;
    }

    public static boolean isList(String str) {
        if (str.length() < 2) return false;
        String t = str.trim();
        return t.charAt(0) == '[' && t.charAt(t.length() - 1) == ']';
    }

    public static String getListContent(String list) {
        return list.substring(1, list.length() - 1).trim();
    }
}

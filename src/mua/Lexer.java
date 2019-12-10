package src.mua;

import java.util.*;

public class Lexer {
    public static HashSet<String> state_name = new HashSet<>(Arrays.asList(
            "make", "erase", "print"
    ));

    public static HashSet<String> exp_name = new HashSet<>(Arrays.asList(
            "thing", "isname", "read"
    ));

    public static HashSet<String> opr_1_name = new HashSet<>(Arrays.asList(
            "not"
    ));

    public static HashSet<String> opr_2_name = new HashSet<>(Arrays.asList(
            "add", "sub", "mul", "div", "mod",
            "eq", "gt", "lt",
            "and", "or"
    ));

    public enum TokType {
        StateTok, ExpressTok, Word, Operator_1, Operator_2, ListSep, Unknown
    }

    public static ArrayList<AbstractMap.SimpleEntry<String, TokType>> parse(String stmt) {
        ArrayList<String> tmp = new ArrayList<>(Arrays.asList(stmt
                .replace("(", " ( ")
                .replace(")", " ) ")
                .replace("[", " [ ")
                .replace("]", " ] ")
                .replace(":", "thing \"")
                .trim()
                .split("\\s+")));
        tmp.replaceAll(String::trim);

        // deal with comment
        int idx;
        if ((idx = tmp.indexOf("//")) != -1) tmp.subList(idx, tmp.size()).clear();

        ArrayList<AbstractMap.SimpleEntry<String, TokType>> tokens = new ArrayList<>();
        TokType type;
        for (int i = 0; i < tmp.size(); i++) {
            String now_tok = tmp.get(i);
            if (now_tok.equals("true") || now_tok.equals("false")) {
                type = TokType.Word;
            } else if (isNumeric(now_tok)) {
                type = TokType.Word;
            } else if (state_name.contains(now_tok)) {
                type = TokType.StateTok;
            } else if (opr_1_name.contains(now_tok)) {
                type = TokType.Operator_1;
            } else if (opr_2_name.contains(now_tok)) {
                type = TokType.Operator_2;
            } else if (exp_name.contains(now_tok)) {
                type = TokType.ExpressTok;
            } else if (now_tok.startsWith("\"")) {
                now_tok = now_tok.substring(1);
                type = TokType.Word;
            } else if(now_tok.equals("[") || now_tok.equals("]")) {
                type = TokType.ListSep;
            } else {
                type = TokType.Unknown;
            }
            tokens.add(new AbstractMap.SimpleEntry<String, TokType>(now_tok, type));
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
}

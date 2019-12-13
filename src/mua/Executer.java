package src.mua;

import java.util.*;

import src.mua.ASTree;
import src.mua.ASTreeNode;
import src.mua.Main;
import src.mua.Lexer;
import src.mua.Parser;

public class Executer {
    public static void execute(ASTree tree) {
        helper(tree.root);
//        tree.printTree();
//        System.out.println();
    }

    private static void helper(ASTreeNode node) {
        for (ASTreeNode c : node.childList) {
            if (c != null && (c.getData().getValue() == Lexer.TokType.Operator_1 ||
                    c.getData().getValue() == Lexer.TokType.Operator_2 ||
                    c.getData().getValue() == Lexer.TokType.ExpressTok)) {
                helper(c);
            }
        }

        String opr_name = node.getData().getKey();
        if (opr_name.equals("make")) {
            String key = node.getNth(0).getData().getKey();
            String value = node.getNth(1).getData().getKey();
            Main.name_space.put(key, value);
        } else if (opr_name.equals("print")) {
            String value = node.getNth(0).getData().getKey();
            System.out.println(value);
        } else if (opr_name.equals("repeat")) {
            int time = parseInt(node.getNth(0).getData().getKey());
            String list = node.getNth(1).getData().getKey();
            for (int i = 0; i < time; i++) {
                List<AbstractMap.SimpleEntry<String, Lexer.TokType>> tokens = Lexer.parse(list);
                List<src.mua.ASTree> trees = Parser.parse(tokens);
                for (src.mua.ASTree tree : trees) {
                    Executer.execute(tree);
                }
            }
        } else if (opr_name.equals("thing")) {
            String key = node.getNth(0).getData().getKey();
            String value = Main.name_space.get(key);
            node.setData(new AbstractMap.SimpleEntry<>(value, Lexer.TokType.Word));
            node.add(null);
        } else if (opr_name.equals("erase")) {
            String key = node.getNth(0).getData().getKey();
            Main.name_space.remove(key);
        } else if (opr_name.equals("isname")) {
            String key = node.getNth(0).getData().getKey();
            if (Main.name_space.containsKey(key))
                node.setData(new AbstractMap.SimpleEntry<>("true", Lexer.TokType.Word));
            else
                node.setData(new AbstractMap.SimpleEntry<>("false", Lexer.TokType.Word));
        } else if (opr_name.equals("read")) {
            String value;
            value = Main.in.nextLine();
            node.setData(new AbstractMap.SimpleEntry<>(value, Lexer.TokType.Word));
        } else if (opr_name.equals("readlist")) {
            String value;
            value = Main.in.nextLine();
            node.setData(new AbstractMap.SimpleEntry<>(value, Lexer.TokType.List));
        } else if (opr_name.equals("add")) {
            double left_val = parseDouble(node.getNth(0).getData().getKey());
            double right_val = parseDouble(node.getNth(1).getData().getKey());
            String res = Double.toString(left_val + right_val);
            node.setData(new AbstractMap.SimpleEntry<>(res, Lexer.TokType.Word));
        } else if (opr_name.equals("sub")) {
            double left_val = parseDouble(node.getNth(0).getData().getKey());
            double right_val = parseDouble(node.getNth(1).getData().getKey());
            String res = Double.toString(left_val - right_val);
            node.setData(new AbstractMap.SimpleEntry<>(res, Lexer.TokType.Word));
        } else if (opr_name.equals("mul")) {
            double left_val = parseDouble(node.getNth(0).getData().getKey());
            double right_val = parseDouble(node.getNth(1).getData().getKey());
            String res = Double.toString(left_val * right_val);
            node.setData(new AbstractMap.SimpleEntry<>(res, Lexer.TokType.Word));
        } else if (opr_name.equals("div")) {
            double left_val = parseDouble(node.getNth(0).getData().getKey());
            double right_val = parseDouble(node.getNth(1).getData().getKey());
            String res = Double.toString(left_val / right_val);
            node.setData(new AbstractMap.SimpleEntry<>(res, Lexer.TokType.Word));
        } else if (opr_name.equals("mod")) {
            double left_val = parseDouble(node.getNth(0).getData().getKey());
            double right_val = parseDouble(node.getNth(1).getData().getKey());
            String res = Double.toString(left_val % right_val);
            node.setData(new AbstractMap.SimpleEntry<>(res, Lexer.TokType.Word));
        } else if (opr_name.equals("eq")) {
            String left_str = node.getNth(0).getData().getKey();
            String right_str = node.getNth(1).getData().getKey();
            if ((Lexer.isNumeric(left_str) && isParseableNumber(right_str)) ||
                    (Lexer.isNumeric(right_str) && isParseableNumber(left_str))) {
                double left_val = parseDouble(left_str);
                double right_val = parseDouble(right_str);
                String res = (left_val == right_val) ? "true" : "false";
                node.setData(new AbstractMap.SimpleEntry<>(res, Lexer.TokType.Word));
            } else {
                String res = (left_str.equals(right_str)) ? "true" : "false";
                node.setData(new AbstractMap.SimpleEntry<>(res, Lexer.TokType.Word));
            }
        } else if (opr_name.equals("gt")) {
            String left_str = node.getNth(0).getData().getKey();
            String right_str = node.getNth(1).getData().getKey();
            if ((Lexer.isNumeric(left_str) && isParseableNumber(right_str)) ||
                    (Lexer.isNumeric(right_str) && isParseableNumber(left_str))) {
                double left_val = parseDouble(left_str);
                double right_val = parseDouble(right_str);
                String res = (left_val > right_val) ? "true" : "false";
                node.setData(new AbstractMap.SimpleEntry<>(res, Lexer.TokType.Word));
            } else {
                String res = (left_str.compareTo(right_str) > 0) ? "true" : "false";
                node.setData(new AbstractMap.SimpleEntry<>(res, Lexer.TokType.Word));
            }
        } else if (opr_name.equals("lt")) {
            String left_str = node.getNth(0).getData().getKey();
            String right_str = node.getNth(1).getData().getKey();
            if ((Lexer.isNumeric(left_str) && isParseableNumber(right_str)) ||
                    (Lexer.isNumeric(right_str) && isParseableNumber(left_str))) {
                double left_val = parseDouble(left_str);
                double right_val = parseDouble(right_str);
                String res = (left_val < right_val) ? "true" : "false";
                node.setData(new AbstractMap.SimpleEntry<>(res, Lexer.TokType.Word));
            } else {
                String res = (left_str.compareTo(right_str) < 0) ? "true" : "false";
                node.setData(new AbstractMap.SimpleEntry<>(res, Lexer.TokType.Word));
            }
        } else if (opr_name.equals("and")) {
            String left_val = node.getNth(0).getData().getKey();
            String right_val = node.getNth(1).getData().getKey();
            if ((left_val.equals("true") || left_val.equals("\"true")) && (right_val.equals("true") || right_val.equals("\"true")))
                node.setData(new AbstractMap.SimpleEntry<>("true", Lexer.TokType.Word));
            else
                node.setData(new AbstractMap.SimpleEntry<>("false", Lexer.TokType.Word));
        } else if (opr_name.equals("or")) {
            String left_val = node.getNth(0).getData().getKey();
            String right_val = node.getNth(1).getData().getKey();
            if ((left_val.equals("false") || left_val.equals("\"false")) && (right_val.equals("false") || right_val.equals("\"false")))
                node.setData(new AbstractMap.SimpleEntry<>("false", Lexer.TokType.Word));
            else
                node.setData(new AbstractMap.SimpleEntry<>("true", Lexer.TokType.Word));
        } else if (opr_name.equals("not")) {
            String val = node.getNth(0).getData().getKey();
            if (val.equals("true") || val.equals("\"true"))
                node.setData(new AbstractMap.SimpleEntry<>("false", Lexer.TokType.Word));
            else
                node.setData(new AbstractMap.SimpleEntry<>("true", Lexer.TokType.Word));
        } else if (Main.name_space.containsKey(opr_name)) {

        } else {

        }
    }

    private static int parseInt(String num_str) {
        if (num_str.startsWith("\"")) num_str = num_str.substring(1);
        return Integer.parseInt(num_str);
    }

    private static double parseDouble(String num_str) {
        if (num_str.startsWith("\"")) num_str = num_str.substring(1);
        return Double.parseDouble(num_str);
    }

    private static boolean isParseableNumber(String num_str) {
        if (num_str.startsWith("\"")) num_str = num_str.substring(1);
        try {
            Double.parseDouble(num_str);
        } catch (NumberFormatException | NullPointerException nfe) {
            return false;
        }
        return true;
    }
}

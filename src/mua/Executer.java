package src.mua;

import java.util.*;

import src.mua.ASTree;
import src.mua.ASTreeNode;
import src.mua.Main;
import src.mua.Lexer;

public class Executer {
    public static void execute(ASTree tree) {
        helper(tree.root);
//        tree.printTree();
//        System.out.println();
    }

    private static void helper(ASTreeNode node) {
        ASTreeNode l = node.getLeft(), r = node.getRight();
        if (l != null && (l.getData().getValue() == Lexer.TokType.Operator_1 ||
                l.getData().getValue() == Lexer.TokType.Operator_2 ||
                l.getData().getValue() == Lexer.TokType.ExpressTok)) {
            helper(l);
        }
        if (r != null && (r.getData().getValue() == Lexer.TokType.Operator_1 ||
                r.getData().getValue() == Lexer.TokType.Operator_2 ||
                r.getData().getValue() == Lexer.TokType.ExpressTok)) {
            helper(r);
        }

        String opr_name = node.getData().getKey();
        if (opr_name.equals("make")) {
            String key = node.getLeft().getData().getKey();
            String value = node.getRight().getData().getKey();
            Main.name_space.put(key, value);
        } else if (opr_name.equals("print")) {
            String value = node.getLeft().getData().getKey();
            System.out.println(value);
        } else if (opr_name.equals("thing")) {
            String key = node.getLeft().getData().getKey();
            String value = Main.name_space.get(key);
            node.setData(new AbstractMap.SimpleEntry<String, Lexer.TokType>(value, Lexer.TokType.Word));
            node.setLeft(null);
        } else if (opr_name.equals("erase")) {
            String key = node.getLeft().getData().getKey();
            Main.name_space.remove(key);
        } else if (opr_name.equals("isname")) {
            String key = node.getLeft().getData().getKey();
            if (Main.name_space.containsKey(key))
                node.setData(new AbstractMap.SimpleEntry<String, Lexer.TokType>("true", Lexer.TokType.Word));
            else
                node.setData(new AbstractMap.SimpleEntry<String, Lexer.TokType>("false", Lexer.TokType.Word));
        } else if (opr_name.equals("read")) {
            String value;
            value = Main.in.nextLine();
            node.setData(new AbstractMap.SimpleEntry<String, Lexer.TokType>(value, Lexer.TokType.Word));
        } else if (opr_name.equals("add")) {
            double left_val = parseNumber(node.getLeft().getData().getKey());
            double right_val = parseNumber(node.getRight().getData().getKey());
            String res = Double.toString(left_val + right_val);
            node.setData(new AbstractMap.SimpleEntry<String, Lexer.TokType>(res, Lexer.TokType.Word));
        } else if (opr_name.equals("sub")) {
            double left_val = parseNumber(node.getLeft().getData().getKey());
            double right_val = parseNumber(node.getRight().getData().getKey());
            String res = Double.toString(left_val - right_val);
            node.setData(new AbstractMap.SimpleEntry<String, Lexer.TokType>(res, Lexer.TokType.Word));
        } else if (opr_name.equals("mul")) {
            double left_val = parseNumber(node.getLeft().getData().getKey());
            double right_val = parseNumber(node.getRight().getData().getKey());
            String res = Double.toString(left_val * right_val);
            node.setData(new AbstractMap.SimpleEntry<String, Lexer.TokType>(res, Lexer.TokType.Word));
        } else if (opr_name.equals("div")) {
            double left_val = parseNumber(node.getLeft().getData().getKey());
            double right_val = parseNumber(node.getRight().getData().getKey());
            String res = Double.toString(left_val / right_val);
            node.setData(new AbstractMap.SimpleEntry<String, Lexer.TokType>(res, Lexer.TokType.Word));
        } else if (opr_name.equals("mod")) {
            double left_val = parseNumber(node.getLeft().getData().getKey());
            double right_val = parseNumber(node.getRight().getData().getKey());
            String res = Double.toString(left_val % right_val);
            node.setData(new AbstractMap.SimpleEntry<String, Lexer.TokType>(res, Lexer.TokType.Word));
        } else if (opr_name.equals("eq")) {
            String left_str = node.getLeft().getData().getKey();
            String right_str = node.getRight().getData().getKey();
            if ((Lexer.isNumeric(left_str) && isParseableNumber(right_str)) ||
                    (Lexer.isNumeric(right_str) && isParseableNumber(left_str))) {
                double left_val = parseNumber(left_str);
                double right_val = parseNumber(right_str);
                String res = (left_val == right_val) ? "true" : "false";
                node.setData(new AbstractMap.SimpleEntry<String, Lexer.TokType>(res, Lexer.TokType.Word));
            } else {
                String res = (left_str.equals(right_str)) ? "true" : "false";
                node.setData(new AbstractMap.SimpleEntry<String, Lexer.TokType>(res, Lexer.TokType.Word));
            }
        } else if (opr_name.equals("gt")) {
            String left_str = node.getLeft().getData().getKey();
            String right_str = node.getRight().getData().getKey();
            if ((Lexer.isNumeric(left_str) && isParseableNumber(right_str)) ||
                    (Lexer.isNumeric(right_str) && isParseableNumber(left_str))) {
                double left_val = parseNumber(left_str);
                double right_val = parseNumber(right_str);
                String res = (left_val > right_val) ? "true" : "false";
                node.setData(new AbstractMap.SimpleEntry<String, Lexer.TokType>(res, Lexer.TokType.Word));
            } else {
                String res = (left_str.compareTo(right_str) > 0) ? "true" : "false";
                node.setData(new AbstractMap.SimpleEntry<String, Lexer.TokType>(res, Lexer.TokType.Word));
            }
        } else if (opr_name.equals("lt")) {
            String left_str = node.getLeft().getData().getKey();
            String right_str = node.getRight().getData().getKey();
            if ((Lexer.isNumeric(left_str) && isParseableNumber(right_str)) ||
                    (Lexer.isNumeric(right_str) && isParseableNumber(left_str))) {
                double left_val = parseNumber(left_str);
                double right_val = parseNumber(right_str);
                String res = (left_val < right_val) ? "true" : "false";
                node.setData(new AbstractMap.SimpleEntry<String, Lexer.TokType>(res, Lexer.TokType.Word));
            } else {
                String res = (left_str.compareTo(right_str) < 0) ? "true" : "false";
                node.setData(new AbstractMap.SimpleEntry<String, Lexer.TokType>(res, Lexer.TokType.Word));
            }
        } else if (opr_name.equals("and")) {
            String left_val = node.getLeft().getData().getKey();
            String right_val = node.getRight().getData().getKey();
            if ((left_val.equals("true") || left_val.equals("\"true")) && (right_val.equals("true") || right_val.equals("\"true")))
                node.setData(new AbstractMap.SimpleEntry<String, Lexer.TokType>("true", Lexer.TokType.Word));
            else
                node.setData(new AbstractMap.SimpleEntry<String, Lexer.TokType>("false", Lexer.TokType.Word));
        } else if (opr_name.equals("or")) {
            String left_val = node.getLeft().getData().getKey();
            String right_val = node.getRight().getData().getKey();
            if ((left_val.equals("false") || left_val.equals("\"false")) && (right_val.equals("false") || right_val.equals("\"false")))
                node.setData(new AbstractMap.SimpleEntry<String, Lexer.TokType>("false", Lexer.TokType.Word));
            else
                node.setData(new AbstractMap.SimpleEntry<String, Lexer.TokType>("true", Lexer.TokType.Word));
        } else if (opr_name.equals("not")) {
            String val = node.getLeft().getData().getKey();
            if (val.equals("true") || val.equals("\"true"))
                node.setData(new AbstractMap.SimpleEntry<String, Lexer.TokType>("false", Lexer.TokType.Word));
            else
                node.setData(new AbstractMap.SimpleEntry<String, Lexer.TokType>("true", Lexer.TokType.Word));
        } else {

        }
    }

    private static double parseNumber(String num_str) {
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

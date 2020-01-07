package src.mua;

import java.io.*;
import java.util.*;

import src.mua.ASTree;
import src.mua.ASTreeNode;
import src.mua.Main;
import src.mua.Lexer;
import src.mua.Parser;

public class Executer {
    public static void execute(ASTree tree, LinkedList<HashMap<String, String>> namespace) {
        helper(tree.root, namespace);
//        tree.printTree();
//        System.out.println();
    }

    private static void helper(ASTreeNode node, LinkedList<HashMap<String, String>> _namespace) {
        HashMap<String, String> namespace = _namespace.getLast();
        for (ASTreeNode c : node.childList) {
            if (c != null && c.getData() != null && (c.getData().getValue() == Lexer.TokType.Operator_1 ||
                    c.getData().getValue() == Lexer.TokType.Operator_2 ||
                    c.getData().getValue() == Lexer.TokType.ExpressTok ||
                    c.getData().getValue() == Lexer.TokType.Unknown)) {
                helper(c, _namespace);
            }
        }

        String opr_name = node.getData().getKey();
        if (opr_name.equals("make")) {
            String key = node.getNth(0).getData().getKey();
            String value = node.getNth(1).getData().getKey();
            namespace.put(key, value);
        } else if (opr_name.equals("save")) {
            String name = node.getNth(0).getData().getKey();
            PrintStream ps = System.out;
            try {
                System.setOut(new PrintStream(new java.io.File(name)));
            } catch (IOException e) {
            }
            for (Map.Entry<String, String> entry : namespace.entrySet()) {
                System.out.println(entry.getKey() + "_:_" + entry.getValue());
            }
            System.setOut(ps);
        } else if (opr_name.equals("load")) {
            String name = node.getNth(0).getData().getKey();
            try {
                InputStream is = new FileInputStream(name);
                InputStreamReader ir = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(ir);

                String line;
                while ((line = br.readLine()) != null) {
                    ArrayList<String> tmp = new ArrayList<>(Arrays.asList(line
                            .trim()
                            .split("_:_")));
                    tmp.replaceAll(String::trim);
                    namespace.put(tmp.get(0), tmp.get(1));
                }
            } catch (IOException e) {
            }
        } else if (opr_name.equals("erall")) {
            namespace.clear();
        } else if (opr_name.equals("print")) {
            String value = node.getNth(0).getData().getKey();
            System.out.println(value);
        } else if (opr_name.equals("stop")) {
            return;
        } else if (opr_name.equals("output")) {
            String value = node.getNth(0).getData().getKey();
            namespace.put("__out", value);
        } else if (opr_name.equals("export")) {
            String key = node.getNth(0).getData().getKey();
            String value = namespace.get(key);
            _namespace.getFirst().put(key, value);
        } else if (opr_name.equals("if")) {
            String bool = node.getNth(0).getData().getKey();
            String list;
            if (bool.equals("true") || bool.equals("\"true"))
                list = node.getNth(1).getData().getKey();
            else
                list = node.getNth(2).getData().getKey();

            List<AbstractMap.SimpleEntry<String, Lexer.TokType>> tokens = Lexer.parse(list);
            List<src.mua.ASTree> trees = Parser.parse(tokens, _namespace);
            for (src.mua.ASTree tree : trees) {
                Executer.execute(tree, _namespace);
            }
        } else if (opr_name.equals("repeat")) {
            int time = parseInt(node.getNth(0).getData().getKey());
            String list = node.getNth(1).getData().getKey();
            for (int i = 0; i < time; i++) {
                List<AbstractMap.SimpleEntry<String, Lexer.TokType>> tokens = Lexer.parse(list);
                List<src.mua.ASTree> trees = Parser.parse(tokens, _namespace);
                for (src.mua.ASTree tree : trees) {
                    Executer.execute(tree, _namespace);
                }
            }
        } else if (opr_name.equals("run")) {
            String list = node.getNth(0).getData().getKey();
            List<AbstractMap.SimpleEntry<String, Lexer.TokType>> tokens = Lexer.parse(list);
            List<src.mua.ASTree> trees = Parser.parse(tokens, _namespace);
            for (src.mua.ASTree tree : trees) {
                Executer.execute(tree, _namespace);
            }
        } else if (opr_name.equals("thing")) {
            String key = node.getNth(0).getData().getKey();
            String value = null;
            Iterator<HashMap<String, String>> itr = _namespace.descendingIterator();
            while (itr.hasNext()) {
                HashMap<String, String> tmp = itr.next();
                if (tmp.containsKey(key)) {
                    value = tmp.get(key);
                    break;
                }
            }
            node.setData(new AbstractMap.SimpleEntry<>(value, Lexer.TokType.Word));
            node.add(null);
        } else if (opr_name.equals("erase")) {
            String key = node.getNth(0).getData().getKey();
            namespace.remove(key);
        } else if (opr_name.equals("isname")) {
            String key = node.getNth(0).getData().getKey();
            if (namespace.containsKey(key))
                node.setData(new AbstractMap.SimpleEntry<>("true", Lexer.TokType.Word));
            else
                node.setData(new AbstractMap.SimpleEntry<>("false", Lexer.TokType.Word));
        } else if (opr_name.equals("isnumber")) {
            String key = node.getNth(0).getData().getKey();
            if (namespace.containsKey(key) && node.getNth(0).getData().getValue() == Lexer.TokType.Word && isParseableNumber(key))
                node.setData(new AbstractMap.SimpleEntry<>("true", Lexer.TokType.Word));
            else
                node.setData(new AbstractMap.SimpleEntry<>("false", Lexer.TokType.Word));
        } else if (opr_name.equals("isword")) {
            String key = node.getNth(0).getData().getKey();
            if (namespace.containsKey(key) && node.getNth(0).getData().getValue() == Lexer.TokType.Word)
                node.setData(new AbstractMap.SimpleEntry<>("true", Lexer.TokType.Word));
            else
                node.setData(new AbstractMap.SimpleEntry<>("false", Lexer.TokType.Word));
        } else if (opr_name.equals("islist")) {
            String key = node.getNth(0).getData().getKey();
            if (namespace.containsKey(key) && node.getNth(0).getData().getValue() == Lexer.TokType.List)
                node.setData(new AbstractMap.SimpleEntry<>("true", Lexer.TokType.Word));
            else
                node.setData(new AbstractMap.SimpleEntry<>("false", Lexer.TokType.Word));
        } else if (opr_name.equals("isbool")) {
            String key = node.getNth(0).getData().getKey();
            if (namespace.containsKey(key) && (key.equals("true") || key.equals("false") || key.equals("\"true") || key.equals("\"false")))
                node.setData(new AbstractMap.SimpleEntry<>("true", Lexer.TokType.Word));
            else
                node.setData(new AbstractMap.SimpleEntry<>("false", Lexer.TokType.Word));
        } else if (opr_name.equals("isempty")) {
            String key = node.getNth(0).getData().getKey();
            if (namespace.containsKey(key) && namespace.get(key).equals(""))
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
        } else if (Parser.isFunc(opr_name, _namespace) && Parser.getFuncNamespaceID(opr_name, _namespace) >= 0) {
            int id = Parser.getFuncNamespaceID(opr_name, _namespace);
            String list = _namespace.get(id).get(opr_name);
            HashMap<String, String> local_namespace = new HashMap<>();
            ArrayList<String> tmp = new ArrayList<>(Arrays.asList(list
                    .trim()
                    .split("\\s+")));
            tmp.replaceAll(String::trim);
            int cnt = 0, i = 0;
            for (String s : tmp) {
                if (s.equals("[")) cnt++;
                else if (s.equals("]")) {
                    if (--cnt == 0) break;
                } else if (cnt == 1) local_namespace.put(s, node.getNth(i++).getData().getKey());
            }
            _namespace.add(local_namespace);
//            System.out.println(local_namespace);

            cnt = 0;
            for (i = 0; i < list.length(); i++) {
                if (list.charAt(i) == ' ') continue;
                if (list.charAt(i) == '[') cnt++;
                if (list.charAt(i) == ']') cnt--;
                if (cnt == 0) break;
            }
            String opr_list = list.substring(i + 1);
            opr_list = opr_list.substring(opr_list.indexOf('[') + 1, opr_list.lastIndexOf(']'));
//            System.out.println(opr_list);

            List<AbstractMap.SimpleEntry<String, Lexer.TokType>> tokens = Lexer.parse(opr_list);
            List<src.mua.ASTree> trees = Parser.parse(tokens, _namespace);

            for (src.mua.ASTree tree : trees) {
                Executer.execute(tree, _namespace);
            }
            if (local_namespace.containsKey("__out")) {
                node.setData(new AbstractMap.SimpleEntry<>(local_namespace.get("__out"), Lexer.TokType.Word));
            }
            _namespace.removeLast();

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

package src.mua;

import java.util.*;

import src.mua.Lexer;
import src.mua.Executer;
import src.mua.Main;

public class Parser {
    static int i;

    public static List<ASTree> parse(List<AbstractMap.SimpleEntry<String, Lexer.TokType>> tokens, LinkedList<HashMap<String, String>> namespace) {
        List<ASTree> trees = new ArrayList<>();
        i = 0;

        while (i < tokens.size()) {
            ASTree tree = new ASTree();

            ASTreeNode node = tree.root;
            helper(tokens, node, namespace);
            if (tree.root.getData().getKey().equals("make")) {
                Executer.execute(tree, namespace);
            } else {
                trees.add(tree);
            }
        }

        return trees;
    }

    private static void helper(List<AbstractMap.SimpleEntry<String, Lexer.TokType>> tokens, ASTreeNode node, LinkedList<HashMap<String, String>> namespace) {
        if (i >= tokens.size()) return;
        AbstractMap.SimpleEntry<String, Lexer.TokType> tok = tokens.get(i++);
        node.setData(tok);
        Lexer.TokType type = tok.getValue();
        if ((type == Lexer.TokType.StateTok && !tok.getKey().equals("make") && !tok.getKey().equals("repeat")) ||
                (type == Lexer.TokType.ExpressTok && !tok.getKey().equals("read") && !tok.getKey().equals("readlist")) ||
                type == Lexer.TokType.Operator_1) {
            node.add(new ASTreeNode());
            helper(tokens, node.getNth(0), namespace);
        } else if (type == Lexer.TokType.Operator_2 || tok.getKey().equals("repeat")) {
            node.add(new ASTreeNode());
            node.add(new ASTreeNode());
            helper(tokens, node.getNth(0), namespace);
            helper(tokens, node.getNth(1), namespace);
        } else if (tok.getKey().equals("make")) {
            node.add(new ASTreeNode(tokens.get(i++)));
            node.add(new ASTreeNode());
            helper(tokens, node.getNth(1), namespace);
        } else if (getFuncNamespaceID(tok.getKey(), namespace) >= 0) {
            int id = getFuncNamespaceID(tok.getKey(), namespace);
            String list = namespace.get(id).get(tok.getKey());
            int size = cntFuncParamSize(list);
//            System.out.println(size);
            for (int i = 0; i < size; i++) {
                node.add(new ASTreeNode());
                helper(tokens, node.getNth(i), namespace);
            }
        } else {
            return;
        }
    }

    public static int getFuncNamespaceID(String func, LinkedList<HashMap<String, String>> _namespace) {
        Iterator<HashMap<String, String>> itr = _namespace.descendingIterator();
        int i = _namespace.size();
        while (itr.hasNext()) {
            HashMap<String, String> tmp = itr.next();
            i--;
            if (tmp.containsKey(func)) {
                return i;
            }
        }
        return -1;
    }

    private static int cntFuncParamSize(String list) {
        int cnt = 0, len = 0;
        ArrayList<String> tmp = new ArrayList<>(Arrays.asList(list
                .trim()
                .split("\\s+")));
        tmp.replaceAll(String::trim);
        for (String s : tmp) {
            if (s.equals("[")) cnt++;
            else if (s.equals("]")) {
                if (--cnt == 0) break;
            } else if (cnt == 1) len++;
        }
        return len;
    }
}


class ASTreeNode {
    private AbstractMap.SimpleEntry<String, Lexer.TokType> data;
    public LinkedList<ASTreeNode> childList;

    public ASTreeNode() {
        data = null;
        childList = new LinkedList<>();
    }

    public ASTreeNode(AbstractMap.SimpleEntry<String, Lexer.TokType> d) {
        this.data = d;
        childList = new LinkedList<>();
    }

    public AbstractMap.SimpleEntry<String, Lexer.TokType> getData() {
        return data;
    }

    public void setData(AbstractMap.SimpleEntry<String, Lexer.TokType> data) {
        this.data = data;
    }

    public ASTreeNode getNth(int n) {
        return childList.get(n);
    }

    public void add(ASTreeNode left) {
        childList.add(left);
    }
}

class ASTree {
    public ASTreeNode root;

    public ASTree() {
        root = new ASTreeNode();
    }

    public void printTree() {
        if (root == null) return;
        Queue<ASTreeNode> queue = new LinkedList<>();

        int current, next;
        queue.offer(root);
        current = 1;
        next = 0;
        while (!queue.isEmpty()) {
            ASTreeNode currentNode = queue.poll();
            System.out.printf("%-10s", currentNode.getData().getKey());
            current--;

            for (ASTreeNode node : currentNode.childList) {
                queue.offer(node);
                next++;
            }
            if (current == 0) {
                System.out.println();
                current = next;
                next = 0;
            }
        }
    }

}
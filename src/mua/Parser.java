package src.mua;

import java.util.*;

import src.mua.Lexer;

public class Parser {
    static int i;

    public static List<ASTree> parse(List<AbstractMap.SimpleEntry<String, Lexer.TokType>> tokens) {
        List<ASTree> trees = new ArrayList<>();
        i = 0;

        while (i < tokens.size()) {
            ASTree tree = new ASTree();

            ASTreeNode node = tree.root;
            helper(tokens, node);

            trees.add(tree);
        }

        return trees;
    }

    private static void helper(List<AbstractMap.SimpleEntry<String, Lexer.TokType>> tokens, ASTreeNode node) {
        if (i >= tokens.size()) return;
        AbstractMap.SimpleEntry<String, Lexer.TokType> tok = tokens.get(i++);
        node.setData(tok);
        Lexer.TokType type = tok.getValue();
        if ((type == Lexer.TokType.StateTok && !tok.getKey().equals("make") && !tok.getKey().equals("repeat")) ||
                (type == Lexer.TokType.ExpressTok && !tok.getKey().equals("read") && !tok.getKey().equals("readlist")) ||
                type == Lexer.TokType.Operator_1) {
            node.add(new ASTreeNode());
            helper(tokens, node.getNth(0));
        } else if (type == Lexer.TokType.Operator_2 || tok.getKey().equals("make") || tok.getKey().equals("repeat")) {
            node.add(new ASTreeNode());
            node.add(new ASTreeNode());
            helper(tokens, node.getNth(0));
            helper(tokens, node.getNth(1));
        } else {
            return;
        }
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
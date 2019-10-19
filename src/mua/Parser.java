package src.mua;

import java.util.*;

import src.mua.Lexer;

public class Parser {
    static int i;

    public static ASTree parse(List<AbstractMap.SimpleEntry<String, Lexer.TokType>> tokens) {
        ASTree tree = new ASTree();
        i = 0;
        ASTreeNode node = tree.root;
        helper(tokens, node);

        return tree;
    }

    private static void helper(List<AbstractMap.SimpleEntry<String, Lexer.TokType>> tokens, ASTreeNode node) {
        if (i >= tokens.size()) return;
        AbstractMap.SimpleEntry<String, Lexer.TokType> tok = tokens.get(i++);
        node.setData(tok);
        Lexer.TokType type = tok.getValue();
        if ((type == Lexer.TokType.StateTok && !tok.getKey().equals("make")) ||
                (type == Lexer.TokType.ExpressTok && !tok.getKey().equals("read")) ||
                type == Lexer.TokType.Operator_1) {
            node.setLeft(new ASTreeNode());
            node = node.getLeft();
            helper(tokens, node);
        } else if (type == Lexer.TokType.Operator_2 || tok.getKey().equals("make")) {
            node.setLeft(new ASTreeNode());
            node.setRight(new ASTreeNode());
            helper(tokens, node.getLeft());
            helper(tokens, node.getRight());
        } else {
            return;
        }
    }
}


class ASTreeNode {
    private AbstractMap.SimpleEntry<String, Lexer.TokType> data;
    private ASTreeNode left = null, right = null;

    public ASTreeNode() {
        data = null;
        left = null;
        right = null;
    }

    public ASTreeNode(AbstractMap.SimpleEntry<String, Lexer.TokType> d) {
        this.data = d;
        left = null;
        right = null;
    }

    public AbstractMap.SimpleEntry<String, Lexer.TokType> getData() {
        return data;
    }

    public void setData(AbstractMap.SimpleEntry<String, Lexer.TokType> data) {
        this.data = data;
    }

    public ASTreeNode getLeft() {
        return left;
    }

    public ASTreeNode getRight() {
        return right;
    }

    public void setLeft(ASTreeNode left) {
        this.left = left;
    }

    public void setRight(ASTreeNode right) {
        this.right = right;
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

            if (currentNode.getLeft() != null) {
                queue.offer(currentNode.getLeft());
                next++;
            }
            if (currentNode.getRight() != null) {
                queue.offer(currentNode.getRight());
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
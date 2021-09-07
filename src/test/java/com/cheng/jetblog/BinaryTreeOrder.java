package com.cheng.jetblog;

import lombok.Data;

import java.util.*;

/**
 * @author cheng
 * @since 2021/9/4 16:23
 **/
public class BinaryTreeOrder {

    @Data
    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int val) {
            this.val = val;
        }
    }


    public static List<Integer> preorderTraversal(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        if (root == null) {
            return result;
        }

        Deque<TreeNode> stack = new ArrayDeque<>();
        stack.push(root);
        while (!stack.isEmpty()) {
            TreeNode curr = stack.pop();
            result.add(curr.val);

            if (curr.right != null) {
                stack.push(curr.right);
            }
            if (curr.left != null) {
                stack.push(curr.left);
            }

        }
        return result;
    }

    public static List<Integer> inorderTraversal(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        if (root == null) {
            return result;
        }

        Deque<TreeNode> stack = new ArrayDeque<>();
        TreeNode curr = root;
        while (curr != null || !stack.isEmpty()) {

            if (curr != null) {
                stack.push(curr);
                curr = curr.left;
            } else {
                TreeNode t = stack.pop();
                result.add(t.val);
                curr = t.right;
            }
        }
        return result;
    }

    public static List<Integer> postorderTraversal(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        if (root == null) {
            return result;
        }

        Deque<TreeNode> stack = new ArrayDeque<>();
        stack.push(root);
        TreeNode prev = null;
        while (!stack.isEmpty()) {
            TreeNode curr = stack.peek();
            boolean noChild = curr.left == null && curr.right == null;
            boolean childVisited = false;
            if (prev != null && (curr.left == prev || curr.right == prev)) {
                childVisited = true;
            }

            if (noChild || childVisited) {
                result.add(curr.val);
                stack.pop();
                prev = curr;
            } else {
                if (curr.right != null) {
                    stack.push(curr.right);
                }
                if (curr.left != null) {
                    stack.push(curr.left);
                }
            }
        }
        return result;
    }

    public static List<Integer> postorderTraversal2(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        if (root == null) {
            return result;
        }

        Deque<TreeNode> stack = new ArrayDeque<>();
        stack.push(root);
        while (!stack.isEmpty()) {
            TreeNode curr = stack.pop();
            result.add(curr.val);
            if (curr.left != null) {
                stack.push(curr.left);
            }
            if (curr.right != null) {
                stack.push(curr.right);
            }
        }
        Collections.reverse(result);
        return result;
    }

    public static List<List<Integer>> levelOrder(TreeNode root) {
        List<List<Integer>> result = new ArrayList<>();
        if (root == null) {
            return result;
        }

        Queue<TreeNode> queue = new ArrayDeque<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            List<Integer> list = new ArrayList<>();
            for (int i = 0, size = queue.size(); i < size; i++) {
                TreeNode curr = queue.poll();
                assert curr != null;
                list.add(curr.val);
                if (curr.left != null) {
                    queue.offer(curr.left);
                }
                if (curr.right != null) {
                    queue.offer(curr.right);
                }
            }
            result.add(list);
        }
        return result;
    }

    static class Node {
        public int val;
        public Node left;
        public Node right;
        public Node next;

        public Node() {
        }

        public Node(int _val) {
            val = _val;
        }

        public Node(int _val, Node _left, Node _right, Node _next) {
            val = _val;
            left = _left;
            right = _right;
            next = _next;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "val=" + val +
                    ", left=" + left +
                    ", right=" + right +
                    ", next=" + next +
                    '}';
        }
    }

    public static Node connect(Node root) {
        Node levelFirst = root;
        while (levelFirst != null) {
            Node curr = levelFirst;
            while (curr != null) {
                if (curr.left != null) {
                    curr.left.next = curr.right;
                }
                if (curr.right != null && curr.next != null) {
                    curr.right.next = curr.next.left;
                }
                curr = curr.next;
            }
            levelFirst = levelFirst.left;
        }

        return root;
    }



    public static void main(String[] args) {
        TreeNode root = new TreeNode(4);
        root.left = new TreeNode(2);
        root.right = new TreeNode(6);
        root.left.left = new TreeNode(1);
        root.left.right = new TreeNode(3);
        root.right.left = new TreeNode(5);
        root.right.right = new TreeNode(7);

        List<Integer> preAns = BinaryTreeOrder.preorderTraversal(root);
        System.out.println("preAns = " + preAns);
        List<Integer> inAns = BinaryTreeOrder.inorderTraversal(root);
        System.out.println("inAns = " + inAns);
        List<Integer> postAns = BinaryTreeOrder.postorderTraversal(root);
        System.out.println("postAns = " + postAns);
        List<Integer> postAns2 = BinaryTreeOrder.postorderTraversal2(root);
        System.out.println("postAns2 = " + postAns2);

        TreeNode root2 = new TreeNode(3);
        root2.left = new TreeNode(9);
        root2.right = new TreeNode(20);
        root2.right.left = new TreeNode(15);
        root2.right.right = new TreeNode(7);

        List<List<Integer>> levelOrderAns = BinaryTreeOrder.levelOrder(root2);
        System.out.println("levelOrderAns = " + levelOrderAns);

        Node root3 = new Node(1);
        root3.left = new Node(2);
        root3.left.next = root3.right;
        root3.right = new Node(3);
        root3.left.left = new Node(4);
        root3.left.left.next = root3.left.right;
        root3.left.right = new Node(5);
        root3.right.left = new Node(6);
        root3.right.right = new Node(7);

        Node populatingNode = BinaryTreeOrder.connect(root3);
        System.out.println("populatingNode = " + populatingNode);
    }
}

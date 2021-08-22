package com.cheng.jetblog;

import lombok.Data;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author cheng
 * @since 2021/8/22 00:03
 **/
public class LeetCodeTest {

    @Data
    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode() {
        }

        TreeNode(int val) {
            this.val = val;
        }

        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }

    public List<List<Integer>> levelOrder(TreeNode root) {
        List<List<Integer>> result = new ArrayList<>();
        if (root == null) {
            return result;
        } else {
            Queue<TreeNode> queue = new LinkedList<>();
            queue.offer(root);
            while (!queue.isEmpty()) {
                List<Integer> cells = new ArrayList<>();
                for (int i = 0, size = queue.size(); i < size; i++) {
                    TreeNode cur = queue.poll();
                    assert cur != null;
                    cells.add(cur.val);
                    if (cur.left != null) {
                        queue.offer(cur.left);
                    }
                    if (cur.right != null) {
                        queue.offer(cur.right);
                    }
                }
                result.add(cells);
            }
        }
        return result;
    }

    public static void main(String[] args) {
        String[] s = {"1", "2"};
        System.out.println("a = " + Arrays.toString(
                Stream.of(s).map("TEST-"::concat).toArray()));

        LeetCodeTest l = new LeetCodeTest();
        TreeNode root = new TreeNode(3);
        root.left = new TreeNode(9);
        root.right = new TreeNode(20);
        root.right.left = new TreeNode(15);
        root.right.right = new TreeNode(17);
        l.levelOrder(root);
    }
}

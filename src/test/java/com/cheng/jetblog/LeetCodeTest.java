package com.cheng.jetblog;

import lombok.Data;
import org.springframework.util.StringUtils;

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

    private static Boolean checkValue(Object object) {
        return (Boolean) Optional.ofNullable(object)
                .filter((e) -> !(e instanceof String) || !e.equals(""))
                .orElse(false);
    }

    public static void main(String[] args) {

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("page", "");
        hashMap.put("size", "");
        hashMap.put("type", "A");
        hashMap.put("dept", null);

//        hashMap.values().removeIf(Objects::isNull);

        Map<String, String> hash = hashMap.entrySet().stream()
                .filter(e -> !StringUtils.isEmpty(e.getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));


        hash.forEach((k, v) -> {

        });

        System.out.println("hash = " + hash);

        String[] s = {"1", "2"};
        System.out.println("a = " + Arrays.toString(
                Stream.of(s).map("TEST-"::concat).toArray()));
        String J = "aA", S = "aAAbbbb";
        char[] cs = S.toCharArray();
        int cnt = 0;
        for (char value : cs) {
            if (J.indexOf(value) > -1) {
                cnt++;
            }
        }
        System.out.println("cnt = " + cnt);

        char[] cs1 = S.toCharArray();
        char[] js = J.toCharArray();
        HashMap<Character, Integer> map = new HashMap<>();

        for (char c : cs1) {
            if (map.containsKey(c)) {
                map.put(c, map.get(c) + 1);
            } else {
                map.put(c, 1);
            }
        }
        int r = 0;
        for (char j : js) {
            if (map.containsKey(j)) {
                r += map.get(j);
            }
        }

        System.out.println("r = " + r);


        System.out.println("map = " + map);


        LeetCodeTest l = new LeetCodeTest();
        TreeNode root = new TreeNode(3);
        root.left = new TreeNode(9);
        root.right = new TreeNode(20);
        root.right.left = new TreeNode(15);
        root.right.right = new TreeNode(17);
        l.levelOrder(root);
    }
}

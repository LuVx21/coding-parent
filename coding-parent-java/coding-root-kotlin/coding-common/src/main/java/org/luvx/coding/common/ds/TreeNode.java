package org.luvx.coding.common.ds;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TreeNode<T> {
    public T           val;
    public TreeNode<T> left;
    public TreeNode<T> right;

    public TreeNode(T x) {
        val = x;
    }

    public boolean isLeaf() {
        return left == null && right == null;
    }

    public static <T> TreeNode<T> of(T x, TreeNode<T>... lr) {
        final TreeNode<T> root = new TreeNode<>(x);
        if (lr == null || lr.length == 0) {
            return root;
        }
        root.left = lr[0];
        if (lr.length >= 2) {
            root.right = lr[1];
        }
        return root;
    }
}

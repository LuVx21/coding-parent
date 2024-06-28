package org.luvx.coding.common.ds;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BinaryTreeNode<T> {
    public T                 val;
    public BinaryTreeNode<T> left;
    public BinaryTreeNode<T> right;

    public BinaryTreeNode(T x) {
        val = x;
    }

    public boolean isLeaf() {
        return left == null && right == null;
    }

    public static <T> BinaryTreeNode<T> of(T x, BinaryTreeNode<T>... lr) {
        final BinaryTreeNode<T> root = new BinaryTreeNode<>(x);
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

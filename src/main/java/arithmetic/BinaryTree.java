package arithmetic;

import lombok.Data;

public class BinaryTree {





    @Data
    public static class TreeNode {
        private Integer value;

        private TreeNode left;

        private TreeNode right;

        public TreeNode() { }

        public TreeNode(int value) {
            this.value = value;
        }
    }



}

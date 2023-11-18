package arithmetic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class DepthFirstSearch {

    /**
     * 试求从根节点到最小的叶子节点的路径，路径由节点的值组成
     *
     * 二叉树也可以用数组来存储，
     * 给定一个数组，树的根节点的值存储在下标1，对于存储在下标N的节点，
     * 它的左子节点和右子节点分别存储在下标2N和2N+1，并且我们用值-1代表一个节点为空。
     *
     * 给定一个数组存储的二叉树，试求从根节点到最小的叶子节点的路径，路径由节点的值组成。
     * [3,5,7,-1,-1,2,4]-----[3,7,2]
     * [5,9,8,-1,-1,7,-1,-1,-1,-1,-1,6]---[5,8,7,6]
     *               5
     *            9    8
     *        -1 -1      7  -1
     *     -1 -1  -1 -1  6 -1
     *  题解
     *  1、DFS求叶子节点，然后对比叶子结点数据轮流替换直到最小值和该值在数组下表index
     *  2、顺延叶子节点路径向上添加节点
     */
    public static void arrayBinaryTreeMinRoad() {
        List<Integer> dataList = ArithmeticUtil.getSingleList("arrayBinaryTreeMinRoad");

        int minIndex = dfsGetMinIndex(dataList, 0);

        List<Integer> allNodes = new ArrayList<>();

        while (minIndex > 0) {
            allNodes.add(dataList.get(minIndex));
            minIndex = (minIndex-1) / 2;
        }
        allNodes.add(dataList.get(0));
        Collections.reverse(allNodes);

        StringBuilder sb = new StringBuilder().append("[");
        allNodes.forEach(x -> sb.append(x).append(","));
        sb.replace(sb.lastIndexOf(","), sb.length(), "]");
        System.out.println("array binaryTree MinRoad = "+ sb.toString());
    }

    // DFS 递归求最小节点Index，因为初始化index=0,所以需要 2*index + 1
    private static int dfsGetMinIndex(List<Integer> dataList, int index) {
        int allLen = dataList.size();
        if (isLeafNode(dataList, index)) {
            return index;
        }
        int leftMinIndex = dfsGetMinIndex(dataList, 2*index + 1);
        int rightMinIndex = dfsGetMinIndex(dataList, 2*index + 2);

        //如果只有右节点, 则返回
        if (leftMinIndex >= allLen || dataList.get(leftMinIndex) == -1) {
            return rightMinIndex;
        }
        //如果只有右节点, 则返回
        if (rightMinIndex >= allLen || dataList.get(rightMinIndex) == -1) {
            return leftMinIndex;
        }
        return (dataList.get(leftMinIndex) > dataList.get(rightMinIndex)) ? rightMinIndex : leftMinIndex;
    }


    private static boolean isLeafNode(List<Integer> dataList, int index) {
        int allLen = dataList.size();
        return (2*index + 1 >= allLen || dataList.get(2*index + 1) == -1)
                && (2*index + 2 >= allLen || dataList.get(2*index + 2) == -1);
    }



    /**
     * 杨辉三角
     * 每一步只能移动到下一行中相邻的结点上。
     * 相邻的结点 在这里指的是 下标 与 上一层结点下标 相同或者等于 上一层结点下标 + 1 的两个结点。
     * 也就是说，如果正位于当前行的下标 i ，那么下一步可以移动到下一行的下标 i 或 i + 1 。
     * triangle = [[2],[3,4],[6,5,7],[4,1,8,3]]
     * 自顶向下的最小路径和为 11（即，2 + 3 + 5 + 1 = 11）
     *
     * 使用一维数组解决不了，可以增加维度
     * dp[i][j] 表示达到第 i 行，第 j 列的最小路径，
     * 发现从上往下走路径太散，只适合于DGS递归
     * 从下往上走，可以得到当前行的每一列往上走求最小值时只有惟一的一条路劲。
     *       2
     *     3   4
     *   6   5   7
     * 4   1   8  3
     */
    public static void triangleMinSum( ) {
        List<List<Integer>> triangle = ArithmeticUtil.getDoubleList();

        int minRoadSum = dynamicTriangleMinSum(triangle);
        System.out.println("triangleMinSum result = " + minRoadSum);
        System.out.println("=|==|==|==|==|==|==|==|==|=");

        Integer[][] memory = new Integer[triangle.size() + 1][triangle.size() + 1];
        minRoadSum = dfsTriangleMinSum(triangle, 0, 0, memory);
        System.out.println("dfsTriangleMinSum result = " + minRoadSum);
    }

    // 动态规划 dynamicTriangleMinSum
    private static int dynamicTriangleMinSum(List<List<Integer>> triangle) {
        int[][] dp = new int[triangle.size() + 1][triangle.size() + 1];
        dp[0][0] = 0;

        // 从下往上递推
        for (int level = triangle.size() -1; level >= 0; level--) {
            int columnSize = triangle.get(level).size();

            for (int column = 0; column <= columnSize -1; column++) {
                int currentVal = triangle.get(level).get(column);
                dp[level][column] = Math.min(dp[level+1][column], dp[level+1][column+1]) + currentVal;
            }
        }
        return dp[0][0];
    }

    /**
     *       2
     *     3   4
     *   6   5   7
     * 4   1   8  3
     * DFS 从顶至底迭代，相当于二叉树生成器
     */
    private static Integer dfsTriangleMinSum(List<List<Integer>> triangle, int level, int column, Integer[][] memory) {
        if (level == triangle.size()) {
            return 0;
        }

        Integer existVal = memory[level][column];
        if (Objects.nonNull(existVal)) {
            return existVal;
        }

        // 求所有左节点路径最小和
        int leftVal = dfsTriangleMinSum(triangle, level + 1, column, memory);

        // 求所有右节点路径最小和
        int rightVal = dfsTriangleMinSum(triangle, level + 1, column + 1, memory);

        return memory[level][column] = Math.min(leftVal, rightVal) + triangle.get(level).get(column);
    }



}

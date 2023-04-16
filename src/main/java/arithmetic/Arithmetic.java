package arithmetic;

import com.google.common.collect.Lists;

import java.util.*;

public class Arithmetic {



    public static void main(String[] args) {
        //getCombinedMinInt();


        // 动态规划与DFS
        // bracketGenerate();
        // validateIntervalStr();
        // triangleMinSum();
        // stockOnceMaxProfit();
        // stockMaxProfitSum();
        // arrayBinaryTreeMinRoad();
        buildBlockMaxLevel();
    }

    /**
     *  求多个字符类型数
     *  前后排列组合后的最小值
     *
     */
    public static void getCombinedMinInt() {
       Scanner inScan = new Scanner(System.in);
        String dataStr = inScan.nextLine();
        inScan.close();

        String[] strArray = dataStr.substring(1, dataStr.length() - 1).split(",");
        // 两两排列后作比较
        Arrays.sort(strArray, (o1, o2) -> (o1+o2).compareTo(o2+o1));
        String minInt = String.join("", strArray);
        System.out.println("getCombinedMaxInt result = "+ minInt);
    }

    //-----------------------回溯方法-------------------------------//

    /**
     * 括号生成器
     */
    public static void bracketGenerate( ) {
        Scanner inScan = new Scanner(System.in);
        int bracketNum = inScan.nextInt();
        List<String> allBracket = Lists.newArrayList();
        StringBuilder sb = new StringBuilder();
        backTrace(allBracket, sb, 0, 0, bracketNum);
        System.out.println("all bracket is ==" + allBracket);
    }

    private static void backTrace(List<String> allBracket, StringBuilder sb, int left, int right, int maxNum) {
        if (sb.length() == 2 * maxNum) {
            allBracket.add(sb.toString());
            return;
        }
        if (left < maxNum) {
            sb.append("(");
            backTrace(allBracket, sb, left+1, right, maxNum);
            sb.deleteCharAt(sb.length() - 1);
        }
        if (right < left) {
            sb.append(")");
            backTrace(allBracket, sb, left, right+1, maxNum);
            sb.deleteCharAt(sb.length() - 1);
        }
    }

    /**
     * 校验交错字符串
     * 字符串交叉------使用方格子跳转路线
     * s1 = abcdefg
     * s2 = hijklmnopqrst
     * s3 = hijklmnopabcqrstdefg
     *
     * x1,  x2,  x3,  x4,  x5
     * x6,  x7,  x8,  x9,  x10
     * x11, x12, x13, x14, x15
     * x16, x17, x18, x19, x20
     */
    public static void validateIntervalStr() {
        Scanner inScan = new Scanner(System.in);
        String rowStr = inScan.nextLine();
        String columnStr = inScan.nextLine();
        String validateStr = inScan.nextLine();
        inScan.close();

        int rowLen = rowStr.length();
        int columnLen = columnStr.length();
        int validateLen = validateStr.length();

        boolean validateRet = true;
        if (validateLen != rowLen + columnLen) {
            System.out.println("validateStr length is not equals first add second");
            return;
        }

        boolean[][] dp = new boolean[rowLen + 1][columnLen + 1];
        dp[0][0] = true;

        //当s2为空时，判断s1能否交织出s3
        for (int i = 1; i <= rowStr.length(); i++) {
            dp[i][0] = rowStr.charAt(i - 1) == validateStr.charAt(i - 1) && dp[i - 1][0];
        }

        //当s1为空时，判断s2能否交织出s3
        for (int j = 1; j <= columnStr.length(); j++) {
            dp[0][j] = columnStr.charAt(j - 1) == validateStr.charAt(j - 1) && dp[0][j - 1];
        }

        for (int i = 1; i <= rowStr.length(); i++) {
            for (int j = 1; j <= columnStr.length(); j++) {

                char fRowChar = rowStr.charAt(i - 1);
                char fColChar = columnStr.charAt(j - 1);

                char validateChar = validateStr.charAt(i + j - 1);
                dp[i][j] = (fRowChar == validateChar && dp[i - 1][j]) || (fColChar == validateChar && dp[i][j - 1]);
                System.out.println("dp[" + i + "," + j +"] = " + dp[i][j]);
            }
        }
        validateRet = dp[rowLen][columnLen];
        System.out.println("validateIntervalStr result = " + validateRet);
    }


    /**
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
        List<List<Integer>> triangle = getDoubleList();
        
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

    // 获取矩阵列表
    private static List<List<Integer>> getDoubleList() {
        Scanner inScan  = new Scanner(System.in);
        String dataStr = inScan.nextLine();
        dataStr= dataStr.substring(2, dataStr.length() - 2);

        List<List<Integer>> doubleList = new ArrayList<>();
        String[] strArray = dataStr.split("],\\[");

        for (String s : strArray) {
            List<Integer> datas  = new ArrayList<>();
            Arrays.stream(s.split(",")).forEach(x -> datas.add(Integer.valueOf(x)));
            doubleList.add(datas);
        }
        return doubleList;
    }

    // 获取单链表
    private static List<Integer> getSingleList() {
        Scanner inScan  = new Scanner(System.in);
        String dataStr = inScan.nextLine();
        dataStr= dataStr.substring(1, dataStr.length() - 1);

        List<Integer> singleList = new ArrayList<>();
        Arrays.stream(dataStr.split(",")).forEach(x -> singleList.add(Integer.valueOf(x)));
        return singleList;
    }

    /**
     * 给定一个数组 prices ，它的第 i 个元素 prices[i] 表示一支给定股票第 i 天的价格。
     *
     * 你只能选择 某一天 买入这只股票，并选择在 未来的某一个不同的日子 卖出该股票。
     * 设计一个算法来计算你所能获取的最大利润。
     *
     * 返回你可以从这笔交易中获取的最大利润。如果你不能获取任何利润，返回 0 。
     *
     */
    public static void stockOnceMaxProfit() {
        List<Integer> dataList = getSingleList();
        int minPrice = dataList.get(0);
        int maxProfit = 0;
        for (int i = 0; i < dataList.size(); i++) {
            int currentVal = dataList.get(i);
            if (currentVal < minPrice) {
                minPrice = currentVal;
            }
            else if (currentVal > minPrice) {
                maxProfit = Math.max(currentVal - minPrice, maxProfit);
            }
        }
        System.out.println("stock Once MaxProfit result = "+ maxProfit);
    }

    /**
     * 整个流程股票可以多次购买，但是购买前必须要卖出手上的股票
     * 求最大的利益和
     *
     * 从头至尾，只要存在利益差，就应该购买和出售，这样利益才能最大化
     */
    public static void stockMaxProfitSum() {
        List<Integer> dataList = getSingleList();
        int maxProfit = 0;
        for (int i = 1; i < dataList.size(); i++) {
            Integer currentVal = dataList.get(i);
            Integer frontVal = dataList.get(i -1);
            if (currentVal > frontVal) {
                maxProfit += currentVal - frontVal;
            }
        }
        System.out.println("stockMaxProfitSum result = "+ maxProfit);
    }

    /**
     * 求带有手续费的最大股票利润和，可以多次购买，同时手上只能存有一股票
     * 列表最后一项作为手续费用
     *  ------贪心算法-----------
     *
     */
    public static void stockMaxProfitWithFee() {
        List<Integer> dataList = getSingleList();

        int fee = dataList.get(dataList.size() -1);
        int dataLen = dataList.size() - 1;

        int maxProfit = 0;
        int buy = dataList.get(0) + fee;
        for (int i = 0; i <= dataLen-1; i++) {
            Integer currentPrice = dataList.get(i);
            Integer currentBuy = currentPrice + fee;

            // 如果股票下跌，就重新购买，购买就需要重新算手续费用
            if (currentBuy < buy) {
                buy = currentBuy;
            }
            // 如果涨价了，就添加利润，
            else if (currentPrice > buy) {
                maxProfit = currentPrice - buy;
                buy = currentPrice; //方便后面有更高的价做移动。
            }
        }
        System.out.println("stockMaxProfitWithFee result = "+ maxProfit);
    }

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
        List<Integer> dataList = getSingleList();

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
     * 求搭建积木的最大层数
     * 给出一个列表如[[6,7],[5,4],[3,2]],表示木块的长和宽，
     * 当木块的长和宽不大于另个木块的长和宽时，就可以放在上面，此外数组还可以左右翻转。
     * 求最多能搭多少层。
     * 1、现将所有的积木长边对其，(也就是小数组内排序，可以使用长的放前面)，
     * 2、所有的积木数组按照长边降序，如果相同的是，使用短边排序
     * 动态规划
     * dp[i] 表示搭建第i层积木
     * [[5,4],[6,3],[6,7],[6,6],[4,6]]---4
     * [[5,6],[6,3],[6,7],[6,6],[4,6],[2,9]]---5
     */
    public static void buildBlockMaxLevel( ) {
        Scanner inScan = new Scanner(System.in);
        String inStr = inScan.nextLine();
        inScan.close();

        String[] strArray = inStr.substring(2, inStr.length() - 2).split("],\\[");
        int[][] dataArray = new int[strArray.length][2];

        for (int i = 0; i < strArray.length; i++) {
            String str = strArray[i];
            String[] s = str.split(",");
            int x = Integer.parseInt(s[0]);
            int y = Integer.parseInt(s[1]);
            dataArray[i][0] = Math.max(x, y);
            dataArray[i][1] = Math.min(x, y);
        }

        // 多维数组和数组比较，比较的是第二个维度
        Arrays.sort(dataArray, (o1, o2) -> (o1[0] == o2[0]) ? (o2[1] - o1[1]) : (o2[0] - o1[0]));

        StringBuilder sb = new StringBuilder().append("[");
        Arrays.stream(dataArray).forEach( array -> sb.append("(").append(array[0]).append(",").append(array[1]).append(")"));
        sb.append("]");
        System.out.println("build block after sorted result = "+ sb.toString());

        // 初始化每个积木可以使自己至少做一层
        int maxLevel = 1;
        int[] dp = new int[dataArray.length];
        Arrays.fill(dp, 1);

        // 使用快指针遍历，对比指针扫描，对比历史的长度是否都比当前的要长
        for (int fastIdx = 1; fastIdx < dataArray.length; fastIdx++) {
            for (int compareIdx = 0; compareIdx < fastIdx; compareIdx++) {

                if (dataArray[fastIdx][1] <= dataArray[compareIdx][1]) {
                    dp[fastIdx] = dp[compareIdx] + 1;
                }
            }
            maxLevel = Math.max(dp[fastIdx], maxLevel); // 扫描完成切换到当前的最大值
        }
        System.out.println("build block max level = " + maxLevel);
    }






}


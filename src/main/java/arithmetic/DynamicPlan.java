package arithmetic;

import java.util.Arrays;
import java.util.Scanner;

public class DynamicPlan {

    public static void main(String[] args) {
        validateIntervalStr();
    }

    /**
     * https://leetcode.cn/problems/interleaving-string/
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

    // 网格求最大正方形面积
    public static void getGridMaxSquare() {

    }





}

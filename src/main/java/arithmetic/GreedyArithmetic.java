package arithmetic;

import java.util.List;

public class GreedyArithmetic {


    /**
     * 给定一个数组 prices ，它的第 i 个元素 prices[i] 表示一支给定股票第 i 天的价格。
     * 你只能选择 某一天 买入这只股票，并选择在 未来的某一个不同的日子 卖出该股票。
     *
     * 求带有手续费的最大股票利润和，可以多次购买，同时手上只能存有一股票
     * 列表最后一项作为手续费用
     *  ------贪心算法-----------
     *
     */
    public static void stockMaxProfitWithFee() {
        List<Integer> dataList = ArithmeticUtil.getSingleList();

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






}

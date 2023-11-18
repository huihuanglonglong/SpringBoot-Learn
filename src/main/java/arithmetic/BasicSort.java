package arithmetic;

import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;

public class BasicSort {


    public static void main(String[] args) {
        //bubbleSort();
        //selectSort();
        //insertSort();
        // testQuickSort();
        shellSort();
        // heapSort();
    }

    /**
     * 冒泡排序的思想：
     * 从头至尾遍历，每一轮遍历过程中相邻数据两两交换，使得每一轮排序最大的排在后面
     * 第一轮最大的排在最后，第二轮次之。。。。。
     * 下一轮排序的递推公式：
     *  for (int i = 0; i < dataList.size(); i++) {
     *      for (int j = 0; j < dataList.size() -(i+1); j++)
     *
     * 0, -1, 5, 3, 12, 6, 2, -6, 22, 19
     *
     */
    public static void bubbleSort() {
        List<Integer> dataList = ArithmeticUtil.getSingleList("bubbleSort");
        for (int i = 0; i < dataList.size(); i++) {
            for (int j = 0; j < dataList.size() -(i+1); j++) {
                if (dataList.get(j) > dataList.get(j+1)) {
                    Integer tempVal = dataList.get(j);
                    dataList.set(j, dataList.get(j+1));
                    dataList.set(j+1, tempVal);
                }
            }
        }
        printCollectionByStringBuilder(dataList);
    }

    /**
     * 选择排序思想：
     * 遍历当前列表，从当前节点i之前的列表是有序的，之后的是无序的列表
     * 从无需的列表中选择一个比当前节点i小的数据进行替换，一直遍历下去
     * 最后一个节点就不需要再去选择了，已经默认最大。
     * 0, -1, 5, 3, 12, 6, 2, -6, 22, 19
     *
     */
    private static void selectSort( ) {
        List<Integer> dataList = ArithmeticUtil.getSingleList("selectSort");
        for (int i = 0; i < dataList.size() -1; i++) {
            Integer minVal = dataList.get(i);
            int minIdx = i;
            for (int j = i + 1; j < dataList.size(); j++) {
                if (minVal > dataList.get(j)) {
                    minIdx = j;
                }
            }
            if (minIdx != i) {
                Integer tempVale = dataList.get(i);
                dataList.set(i, dataList.get(minIdx));
                dataList.set(minIdx, tempVale);
            }
            printCollectionByStringBuilder(dataList);
        }
        printCollectionByStringBuilder(dataList);
    }

    /**
     * 插入排序的思想：
     * 遍历当前列表，位置在 i之前的数据也是有序列表，之后又是无序列表
     * 选取 i+1号数据，然后遍历前面有序列表，比i+1号数据大的依次往后移动
     * 直到位置匹配为止
     * 0, -1, 5, 3, 12, 6, 2, -6, 22, 19
     *
     */
    private static void insertSort() {
        List<Integer> dataList = ArithmeticUtil.getSingleList("insertSort");
        for (int i = 0; i < dataList.size() -1; i++) {
            Integer insertData = dataList.get(i + 1);
            int minIdx = i;
            while (minIdx >= 0 && (dataList.get(minIdx) > insertData)) {
                dataList.set(minIdx + 1, dataList.get(minIdx));
                minIdx--;
            }
            dataList.set(minIdx + 1, insertData);
            printCollectionByStringBuilder(dataList);
        }
        printCollectionByStringBuilder(dataList);
    }


    /**
     * 快速排序的思想：
     * 相当于双指针二分查找中间位置，使用递归方式针对每一个左右两侧的二分区间做排序
     * 1、每次递归针对此区间内，假设最左边为中间值，然后左右指针依次向中间遍历找到比假定中间数据大小反向的数据然后做数据交换
     * 2、当左右指针相等时，表示已找到真正的中点，然后将假定的中间比较值与中点值交换
     * 3、这样就可以达到一次递归，中间值左侧都是小值，右侧是大值。
     *
     * 0, -1, 5, 3, 12, 6, 2, -6, 22, 19
     *
     */
    private static void testQuickSort() {
        List<Integer> dataList = ArithmeticUtil.getSingleList("quickSort");
        quickSort(dataList, 0, dataList.size() -1);
        printCollectionByStringBuilder(dataList);
    }

    private static void quickSort(List<Integer> dataList, int left, int right) {
        if (left >= right) {
            return;
        }
        int middleIdx = findNextMiddleIdx(dataList, left, right);
        quickSort(dataList, left, middleIdx -1);
        quickSort(dataList, middleIdx +1, right);
        printCollectionByStringBuilder(dataList);
    }

    private static int findNextMiddleIdx(List<Integer> dataList, int left, int right) {
        int preMiddleIdx = left;
        Integer middleVal = dataList.get(preMiddleIdx); // 预定指最左边为中间位置，
        while (left < right) {
            while (left < right && dataList.get(left) < middleVal) {
                left++;
            }
            while (left < right && dataList.get(right) > middleVal) {
                right--;
            }
            if (left != right) {
                Integer tempVal = dataList.get(left);
                dataList.set(left, dataList.get(right));
                dataList.set(right, tempVal);
            }
        }
        // 此刻left == right找到了中间位置，需要交互真正的中间比较值，使得真正的中间值落于中间位置
        Integer tempVal = dataList.get(left);
        if (!tempVal.equals(middleVal)) {
            dataList.set(left, middleVal);
            dataList.set(preMiddleIdx, tempVal);
        }
        printCollectionByStringBuilder(dataList);
        return left;
    }

    private static void shellSort() {
        List<Integer> dataList = ArithmeticUtil.getSingleList("shellSort");
        int step = dataList.size() / 2;
        while (step != 0) {
            for (int startIdx = 0; startIdx <= step; startIdx++) {
                insertSortByStep(dataList, startIdx, step);
                System.out.println("------finished batch sort = " + startIdx + "---------");
            }
            step /= 2;
        }
    }

    // 插入法，将下一个步长点数据与 前一个步长点数据一次交换，直到步长点退回到0点
    private static void insertSortByStep(List<Integer> dataList, int startIdx, int step) {
        for (int insertIdx = startIdx + step; insertIdx < dataList.size(); insertIdx += step) {
            Integer insertVal = dataList.get(insertIdx);
            while (insertIdx > startIdx && insertVal < dataList.get(insertIdx - step)) {
                dataList.set(insertIdx, dataList.get(insertIdx - step));
                insertIdx -= step; // 目的是数据交换了之后插入点是新数据了，方便为下一步循环将新数据插入到前面去。
            }
            dataList.set(insertIdx, insertVal);
            printCollectionByStringBuilder(dataList);
        }
    }


    private static void heapSort() {
        List<Integer> dataList = ArithmeticUtil.getSingleList("headSort");

        int len = dataList.size();
        buildMaxHeap(dataList, len);

        for (int i = len - 1; i > 0; i--) {
            swap(dataList, 0, i);
            len--;
            heapModify(dataList, 0, len);
            printCollectionByStringBuilder(dataList);
        }
    }

    private static void buildMaxHeap(List<Integer> dataList, int len) {
        for (int i = (int) Math.floor(len / 2); i >= 0; i--) {
            heapModify(dataList, i, len);
        }
    }

    private static void heapModify(List<Integer> dataList, int i, int len) {
        int left = 2 * i + 1;
        int right = 2 * i + 2;
        int largest = i;

        if (left < len && dataList.get(left) > dataList.get(largest)) {
            largest = left;
        }

        if (right < len && dataList.get(right) > dataList.get(largest)) {
            largest = right;
        }

        if (largest != i) {
            swap(dataList, i, largest);
            heapModify(dataList, largest, len);
        }
    }

    private static void swap(List<Integer> dataList, int i, int j) {
        Integer tempVal = dataList.get(i);
        dataList.set(i, dataList.get(j));
        dataList.set(j, tempVal);
    }


    private static void printCollectionByStringBuilder(Collection<? extends Number> collection) {
        StringBuilder sb = new StringBuilder();
        collection.forEach(number -> sb.append(number).append(", "));
        sb.delete(sb.length() -2, sb.length());
        System.out.println(sb);
    }




}

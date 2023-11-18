package arithmetic;

import com.google.common.collect.Lists;

import java.util.*;

public class ArithmeticUtil {


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


    // 获取矩阵列表
    public static List<List<Integer>> getDoubleList() {
        Scanner inScan  = new Scanner(System.in);
        String dataStr = inScan.nextLine();
        inScan.close();
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

    // 获取单表
    public static List<Integer> getSingleList(String useName) {
        Scanner inScan  = new Scanner(System.in);
        System.out.println("getSingleList please input listData split by ', '; name = "+ useName);
        String dataStr = inScan.nextLine();
        inScan.close();

        List<Integer> singleList = new ArrayList<>();
        Arrays.stream(dataStr.split(", ")).forEach(x -> singleList.add(Integer.valueOf(x)));
        return singleList;
    }


    public static int[][] getGridByInScan() {
        Scanner inScan = new Scanner(System.in);
        System.out.println("getGridByInScan, please input grid lineNum......");

        int gridLine = inScan.nextInt();
        System.out.println("please input grid rowNum......");

        int gridRow = inScan.nextInt();
        System.out.println("please input grid dataLine, element split by ', '......");
        int[][] gridData = new int[gridLine][gridRow];

        for (int i = 0; i < gridLine; i++) {
            String[] inStr = inScan.nextLine().split(", ");
            if (inStr.length != gridRow) {
                System.out.println("current row num = " + inStr.length + ", but gridRow = " + gridRow);
                throw new RuntimeException("grid Row error");
            }
            for (int j = 0; j < gridRow; j++) {
                gridData[i][j] = Integer.parseInt(inStr[j]);
            }
        }
        inScan.close();
        return gridData;
    }





}


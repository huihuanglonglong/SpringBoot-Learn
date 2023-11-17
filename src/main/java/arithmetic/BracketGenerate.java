package arithmetic;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.Scanner;

public class BracketGenerate {

    public static void main(String[] args) {
        bracketGenerate();
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
        allBracket.forEach(System.out::println);
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

}

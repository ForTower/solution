package exam;

import javafx.beans.binding.When;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author lxr
 * @Date 2022/4/21 - 19:40
 */
/*
    题2：字符串重复统计
    All DNA is composed of a series of nucleotides abbreviated as A, C, G, and T,
            for example: "ACGAATTCCG". When studying DNA, it is sometimes useful to identify
    repeated sequences within the DNA. Write a function to find all the 10-letter-long
    sequences (substrings) that occur more than once in a DNA molecule. For example,
    given s = "AAAAACCCCCAAAAACCCCCCAAAAAGGGTTT",
    return: ["AAAAACCCCC", "CCCCCAAAAA"].
*/
public class AlgorithmSolution {
    /*
    暴力思路：
        （1）由于要截取字符串获得子串，且该子串的长度为10，可以知道这是一个滑动窗口的题目
        （2）因此可以枚举原有字符串长度为10的所有子串，并且将其保存在HashMap中，
            如果当前的子串在HashMap中已存在，则说明该子串至少重复两次
     */

    public ArrayList<String> getRepeatedStringByBf(String dnaSequeneces){
        // 返回重复子串的集合
        ArrayList<String> res = new ArrayList<>();
        // dna序列的长度小于等于10，说明肯定不存在长度为10的子串
        if (dnaSequeneces.length() <= 10){
            return res;
        }
        // 记录子串出现次数的集合
        Map<String, Integer> record = new HashMap<>();
        int left = 0;
        int right = 10;
        while (right <= dnaSequeneces.length()){
            // 得到子串
            String subString = dnaSequeneces.substring(left, right);
            record.put(subString, record.getOrDefault(subString, 0) + 1);
            if(record.get(subString) == 2){
                // 说明该子串重复出现多次
                res.add(subString);
            }
            // 移动窗口
            left ++;
            right ++;
        }
        return res;
    }
    /*
    思路二：
        由于DNA序列只包含A, C, G, T这四个字符，因此可以对这四个字符进行编码
          A = 00
          C = 01
          G = 10
          T = 11
        那么子串就可以转换成一个二进制位为20位的正整数
        接下来的思路和思路一基本一致：
     */
    // 用位运算进行滑动窗口的移动
    public ArrayList<String> getRepeatedStringByBitOperation(String dnaSequeneces){
        ArrayList<String> res = new ArrayList<>();
        if (dnaSequeneces.length() <= 10){
            return res;
        }
        // 对A, C, G, T进行编码
        Map<Character, Integer> encode = new HashMap<>();
        encode.put('A', 0);
        encode.put('C', 1);
        encode.put('G', 2);
        encode.put('T', 3);

        // dna字符序列
        char[] dnaCharSequeneces = dnaSequeneces.toCharArray();

        // 获取第一个子串编码对应的整数
        int encodeNum = 0;
        for(int i = 0; i < 10; i ++){
            // 按照编码规则一个字符对应两位， 因此需要左移两位
            encodeNum <<= 2;
            encodeNum |= encode.get(dnaCharSequeneces[i]);
        }
        // 记录子串出现的次数
        Map<Integer, Integer> recode = new HashMap<>();
        recode.put(encodeNum, 1);
        // 窗口的右端
        int right = 10;
        while (right < dnaCharSequeneces.length){

            //假设encodeNum = 1001 0101 1111 1101 0101
            // 此时需要将dnaCharSequences[right]字符添加入窗口
            encodeNum <<= 2;
            // 此时 encodeNum = 1001 0101 1111 1101 0101 00
            // 假设dnaCharSequences[right]对应的编码为11
            encodeNum |= encode.get(dnaCharSequeneces[right]);
            // 此时 encodeNum = 1001 0101 1111 1101 0101 11
            // 这个时候需要将窗口左端的字符移除，使encodeNum = 01 0101 1111 1101 0101 11，也就是将最左端的10变成00
            // 1 << 20  ===> 1000 0000 0000 0000 0000
            // (1 << 20) - 1  ====> 1111 1111 1111 1111 1111

            // encodeNum           10 0101 0111 1111 0101 0111
            // &
            //(1 << 20) - 1        00 1111 1111 1111 1111 1111
            //                        0101 0111 1111 0101 0111
            // 通过该算法就可以将窗口左端的字符移除掉
            encodeNum &= (1 << 20) - 1;

            recode.put(encodeNum, recode.getOrDefault(encodeNum, 0) + 1);
            if (recode.get(encodeNum) == 2){
                res.add(dnaSequeneces.substring(right - 9, right + 1));
            }
            // 移动窗口
            right ++;
        }
        return res;
    }
}

package com.mommoo.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class StringUtils {
    private StringUtils() {
    }

    public static final int indexOfAny(String string, String[] keys) {
        return indexOfAny(string, keys, 0);
    }

    public static final int lastIndexOfAny(String string, String[] keys){
        return lastIndexOfAny(string, keys, string.length());
    }

    public static final int indexOfAny(String string, String[] keys, int fromIndex) {

        List<Integer> indexList =
                Arrays.stream(keys)
                        .map(key -> string.indexOf(key, fromIndex))
                        .filter(index -> index != -1)
                        .sorted()
                        .collect(Collectors.toList());

        return indexList.size() == 0 ? -1 : indexList.get(0);
    }

    public static final int lastIndexOfAny(String string, String[] keys, int fromIndex) {
        List<Integer> indexList =
                Arrays.stream(keys)
                        .map(key -> string.lastIndexOf(key, fromIndex))
                        .filter(index -> index != -1)
                        .sorted()
                        .collect(Collectors.toList());

        return indexList.size() == 0 ? -1 : indexList.get(indexList.size() - 1);
    }

    public static final int[] indexAndKeyOfAny(String string, String[] keys, int fromIndex){
        int index = indexOfAny(string, keys, fromIndex);

        if (index != -1){
            for (int keyIndex = 0 , size = keys.length ; keyIndex < size ; keyIndex++){
                String key = keys[keyIndex];
                if(key.equals(string.substring(index, index + key.length()))){
                    return new int[]{index, keyIndex};
                }
            }
        }

        return new int[]{-1, -1};

    }

    public static final List<String> splitOfAny(String string, String[] keys){
        List<String> resultList = new ArrayList<>();

        int fromIndex = 0;
        while (true){
            int[] indexAndKey = indexAndKeyOfAny(string, keys, fromIndex);
            int index = indexAndKey[0];
            int keyIndex = indexAndKey[1];

            if (index == -1) {
                resultList.add(string.substring(fromIndex, string.length()));
                break;
            }

            resultList.add(string.substring(fromIndex, index));

            fromIndex = index + keys[keyIndex].length();
        }

        return resultList;
    }

    public static boolean isNumber(String string){
        try{
            Integer.parseInt(string);
            return true;
        }catch(Exception e){
            return false;
        }
    }

    /**
     * ASCII Table 0 ~ 9
     *            48 ~ 57
     */
    public static boolean isNumberChar(char c){
        int asciiCode = (int)c;
        return asciiCode <= 57 && asciiCode >= 48;
    }


    /**
     * ASCII Table's special characters
     * (space)                             range : 32
     * ! " # $ % & ' ( ) * + , - . /       range : 33 ~ 47
     * : ; < = > ? @                       range : 58 ~ 64
     * [ \ ] ^ _ `                         range : 91 ~ 96
     * { | } ~                             range : 123 ~ 126
     *
     */
    public static boolean isSpecialChar(char c){
        int asciiCode = (int)c;
        return  (32 <= asciiCode && asciiCode <= 47) ||
                (58 <= asciiCode && asciiCode <= 64) ||
                (91 <= asciiCode && asciiCode <= 96) ||
                (123 <= asciiCode && asciiCode <= 126);
    }

    /**
     * ASCII Table's hex color characters
     * A ~ F                             range : 65 ~ 70
     * a ~ f                             range : 97 ~ 102
     */
    public static boolean isHexColorChar(char c){
        int asciiCode = (int)c;
        return isNumberChar(c) ||
                (65 <= asciiCode && asciiCode <= 70) ||
                (97 <= asciiCode && asciiCode <= 102);
    }

    public static void main(String[] args) {
        System.out.println("20170131".matches("2017.?1.?1"));
        System.out.println("TODO_20179111".matches("TODO_2017.1.1"));
    }
}

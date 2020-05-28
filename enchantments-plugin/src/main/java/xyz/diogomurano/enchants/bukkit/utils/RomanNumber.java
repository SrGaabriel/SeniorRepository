package xyz.diogomurano.enchants.bukkit.utils;

import java.util.TreeMap;

public class RomanNumber {

    private final static TreeMap<Integer, String> map = new TreeMap<>();

    static {
        map.put(1000, "M");
        map.put(900, "CM");
        map.put(500, "D");
        map.put(400, "CD");
        map.put(100, "C");
        map.put(90, "XC");
        map.put(50, "L");
        map.put(40, "XL");
        map.put(10, "X");
        map.put(9, "IX");
        map.put(5, "V");
        map.put(4, "IV");
        map.put(1, "I");
    }

    public static String toRoman(int number) {
        int l = map.floorKey(number);
        if (number == l) {
            return map.get(number);
        }
        return map.get(l) + toRoman(number - l);
    }

    public static int romanConvert(String roman) {
        int decimal = 0;
        String romanNumeral = roman.toUpperCase();
        for (int x = 0; x < romanNumeral.length(); x++) {
            char convertToDecimal = roman.charAt(x);
            switch (convertToDecimal) {
                case 'M':
                    decimal += 1000;
                    break;
                case 'D':
                    decimal += 500;
                    break;
                case 'C':
                    decimal += 100;
                    break;
                case 'L':
                    decimal += 50;
                    break;
                case 'X':
                    decimal += 10;
                    break;
                case 'V':
                    decimal += 5;
                    break;
                case 'I':
                    decimal += 1;
                    break;
            }
        }
        if (romanNumeral.contains("IV")) {
            decimal -= 2;
        }
        if (romanNumeral.contains("IX")) {
            decimal -= 2;
        }
        if (romanNumeral.contains("XL")) {
            decimal -= 10;
        }
        if (romanNumeral.contains("XC")) {
            decimal -= 10;
        }
        if (romanNumeral.contains("CD")) {
            decimal -= 100;
        }
        if (romanNumeral.contains("CM")) {
            decimal -= 100;
        }
        return decimal;
    }

}
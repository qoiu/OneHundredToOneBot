package com.github.qoiu.main.bot;

import org.junit.Test;

public class MyComparator {

    @Test
    public void compare(){
        boolean c = comparator( "молитва ","молитва");
        System.out.println(" - " + c);
    }

    private boolean comparator(String first, String second){
        char[] charsFirst;
        char[] charsSecond;
        first = first.trim().toLowerCase();
        second = second.trim().toLowerCase();
        if(first.length()>=second.length()){
            charsFirst = first.toLowerCase().toCharArray();
            charsSecond = second.toLowerCase().toCharArray();
        }else {
            charsFirst = second.toLowerCase().toCharArray();
            charsSecond = first.toLowerCase().toCharArray();
        }
        int value=0;
        for (int i = 0; i < charsFirst.length ; i++) {
            int minDiff=15;
            for (int j = 0; j < charsSecond.length ; j++) {
                if(charsFirst[i]==charsSecond[j]){
                    minDiff=Math.min(Math.abs(i-j),minDiff);
                }
            }
            value+=minDiff;
        }
        return value<20;


    }
}

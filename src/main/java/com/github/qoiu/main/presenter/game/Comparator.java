package com.github.qoiu.main.presenter.game;

public interface Comparator {
    boolean compare(String actual, String expected);

    class Base implements Comparator{
        private final int MIN_SAME_SYMBOL_CHECK = 3;

        public boolean compare(String actual, String expected) {
            actual = actual.trim().toLowerCase().replace("ё", "е");
            expected = expected.trim().toLowerCase().replace("ё", "е");
            if (actual.contains(expected) && actual.length() >= MIN_SAME_SYMBOL_CHECK)
                return true;
            if (expected.contains(actual) && actual.length() >= MIN_SAME_SYMBOL_CHECK)
                return true;
            return testCompare(actual, expected);
        }

        boolean testCompare(String actual, String expected) {
            final int DIFFERENCE_VALUE = 20;
            char[] charsFirst;
            char[] charsSecond;
            if (actual.length() >= expected.length()) {
                charsFirst = actual.toLowerCase().toCharArray();
                charsSecond = expected.toLowerCase().toCharArray();
            } else {
                charsFirst = expected.toLowerCase().toCharArray();
                charsSecond = actual.toLowerCase().toCharArray();
            }
            int value = 0;
            for (int i = 0; i < charsFirst.length; i++) {
                int minDiff = 15;
                for (int j = 0; j < charsSecond.length; j++) {
                    if (charsFirst[i] == charsSecond[j]) {
                        minDiff = Math.min(Math.abs(i - j), minDiff);
                    }
                }
                value += minDiff;
            }
            return value < DIFFERENCE_VALUE;
        }
    }
}

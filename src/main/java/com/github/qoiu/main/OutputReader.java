package com.github.qoiu.main;

import java.io.FileReader;
import java.io.IOException;

public class OutputReader implements Read<String> {
    @Override
    public String read() {
        try {
            FileReader reader = new FileReader("Token.txt");
            int c;
            StringBuilder str = new StringBuilder();
            while ((c = reader.read()) != -1) {
                str.append((char) c);
            }
            reader.close();
            return str.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}

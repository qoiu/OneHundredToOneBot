package com.github.qoiu.main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InputVocReader implements Read<List<Question>> {

    private String questionStart;
    private String endWord;

    InputVocReader(String questionStart, String endWord) {
        this.questionStart = questionStart;
        this.endWord = endWord;
    }

    @Override
    public List<Question> read() {
        try {
            FileReader fileReader = new FileReader("voc.txt");
            BufferedReader reader = new BufferedReader(fileReader);
            String c;
            Question question = null;
            List<Question> list = new ArrayList<>();

            while (!(c = reader.readLine()).equals("end")) {
                if (c.startsWith(questionStart)) {
                    if (question != null) list.add(question);
                    question = new Question(replace(c));
                } else {
                    if (question != null && !c.equals("")) question.new Answer(c, 80 - question.getAnswers().size() * 10);
                }
            }
            reader.close();
            fileReader.close();
            return list;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String replace(String text){
        String result = text.replace(questionStart, "");
        return result.trim();
    }

}


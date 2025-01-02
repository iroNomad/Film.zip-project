package com.crazy.filmzip.util;

import java.util.List;

public class ForbiddenWordFilter {
    private static final List<String> FORBIDDEN_WORDS = List.of("개새끼", "씨발", "병신", "죽어", "미친", "썅", "지랄");

    public static String filterForbiddenWords(String input){
        if(input == null || input.isEmpty()){
            return input;
        }

        String result = input;
        for(String word : FORBIDDEN_WORDS){
            result = result.replaceAll("(?i)" + word, "*".repeat(word.length()));
        }
        return result;
    }
}

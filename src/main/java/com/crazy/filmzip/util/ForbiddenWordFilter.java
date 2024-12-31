package com.crazy.filmzip.util;

import java.util.List;

public class ForbiddenWordFilter {
    private static final List<String> FORBIDDEN_WORDS = List.of("개새끼", "씨발", "병신", "광견병", "죽어", "미친");

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

package com.study.mandarin.lang.utils;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

public enum Tone {

    FIRST('\u0304'),
    SECOND('\u0301'),
    THIRD('\u030C'),
    FOURTH('\u0300'),
    NEUTRAL(null);

    private final Character combiningMark;

    Tone(Character combiningMark) {
        this.combiningMark = combiningMark;
    }

    public boolean matches(String input) {
        if (combiningMark == null) {
            return false;
        }

        String normalized =
                Normalizer.normalize(input, Normalizer.Form.NFD);

        return normalized.indexOf(combiningMark) >= 0;
    }

    public static Tone from(String input) {
        for(Tone tone : values()){
            if (tone.matches(input)){
                return tone;
            }
        }
        return NEUTRAL;
    }

    public static List<Tone> extractAll(String input) {
        if (input == null || input.isEmpty()) return List.of();

        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        List<Tone> result = new ArrayList<>();

        boolean wordHasTone = false;
        boolean hasAnyTonesInPhrase = false;
        boolean inWord = false;

        for (int i = 0; i < normalized.length(); i++) {
            char c = normalized.charAt(i);

            if (Character.isWhitespace(c)) {
                // End of a word: if it had text but no tones, it's NEUTRAL
                if (inWord && !wordHasTone) {
                    result.add(NEUTRAL);
                }
                // Reset trackers for the next word
                wordHasTone = false;
                inWord = false;
            } else if (Character.isLetter(c) || c >= 0x0300 && c <= 0x036F) {
                // We are inside a syllable/word
                inWord = true;

                for (Tone tone : values()) {
                    if (tone.combiningMark != null && tone.combiningMark == c) {
                        result.add(tone);
                        wordHasTone = true;
                        hasAnyTonesInPhrase = true;
                        break;
                    }
                }
            }
        }

        if (inWord && !wordHasTone) {
            result.add(NEUTRAL);
        }

        if (!result.isEmpty() && !hasAnyTonesInPhrase) {
            return List.of(NEUTRAL);
        }

        return result;
    }
}
package com.study.mandarin.lang.vocab.service;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(name = "tts.enabled", havingValue = "false", matchIfMissing = true)
public class NoOpTtsService implements TtsService {
    @Override
    public String getAudioUrl(String character, String pinyin) {
        return "";
    }
}

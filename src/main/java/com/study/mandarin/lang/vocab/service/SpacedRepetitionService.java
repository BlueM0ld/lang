package com.study.mandarin.lang.vocab.service;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.concurrent.ThreadLocalRandom;


@Service
public class SpacedRepetitionService {

    public LocalDate calculateNextReviewDate(int streak, boolean success) {
        LocalDate now = LocalDate.now();

        if (success) {
            int interval = (int) Math.pow(2, streak);
            double direction = ThreadLocalRandom.current().nextDouble(-1.0, 1.0);
            double fuzz = direction * Math.max(1.0, interval * 0.1);

            int days = (int) Math.round(interval + fuzz);

            return now.plusDays(days);
        } else {
            return now.plusDays(1);
        }
    }

    public int calculateNewStreak(int streak, boolean success) {
        return success? streak+1: 0;
    }
}
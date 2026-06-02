package com.study.mandarin.lang.vocab.service;

import com.study.mandarin.lang.vocab.dto.Memory;
import com.study.mandarin.lang.vocab.dto.QualityOfRecall;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Implements a simplified version of a spaced repetition algorithm inspired by SM-2.
 * The goal is to determine:
 * 1. How well the user remembered an item (Memory state update)
 * 2. When the item should be reviewed next (review scheduling)
 * The system adapts review intervals based on recall quality:
 * - Successful recall --> increases repetition count and extends interval
 * - Failed recall --> resets repetition count and schedules review for next day
 *
 */
@Service
public class SpacedRepetitionService {

    /*
        if the quality of the recall is at a minimum 3 (Correct, but difficult) we can extend the time to recall
        else considered a retry and reset to be reviewed tomorrow
     */
    public LocalDate calculateNextReviewDate(Memory updatedMemory, QualityOfRecall quality) {
        LocalDate now = LocalDate.now();
        if (!quality.isSuccessfulRecall()) {
            return now.plusDays(1);
        }
        int days = applyFuzz(updatedMemory.getLastInterval());
        return now.plusDays(days);
    }

    /*
        generates new mem object via composition on recalculation user recall
     */
     Memory calculateUpdatedMemory(Memory memory, QualityOfRecall quality) {
        int repetitions = calculateRepetitions(memory, quality);
        double easeFactor = calculateEaseFactor(memory.getEaseFactor(), quality);
        int interval = calculateInterval(repetitions, easeFactor, memory.getLastInterval());
        return new Memory(repetitions,easeFactor,interval);
    }

    /*
       apply fuzz adds randomness
       so we can get variations between words that are not correct aren't grouped together
       on next recall
     */
    private int applyFuzz(int interval) {
        double direction = ThreadLocalRandom.current().nextDouble(-1.0, 1.0);
        double fuzz = direction * Math.max(1.0, interval * 0.1);

        return Math.max(1, (int) Math.round(interval + fuzz));
    }

    /*
     * the time between seeing the same work on success or failure to recall.
     * If we successfully recall extend by 1,6 ... n if we fail recall then will reset to 0
     * so we review the next day.
     */
    private int calculateInterval(int repetitions, double easeFactor, int interval){
        if (repetitions == 0) {
            return 1;
        }
        if (repetitions == 1) {
            return 6;
        }

        return (int) Math.round(interval * easeFactor);
    }


    /*
     * How many times we have seen this word, if we have recalled correctly we increase else we set it to 0
     * why? negation will cause the interval to be less but not the next day. If we fail recall we should
     * review it the next day
     */
    private int calculateRepetitions(Memory memory, QualityOfRecall qualityOfRecall) {
        return qualityOfRecall.isSuccessfulRecall() ? memory.getRepetitions()+1: 0;
    }

    /*
     Ease Factor is by quote "the easiness of memorizing and retaining a given item in memory"
     In short a heuristic to determine where to shift the new review date
     */
    private double calculateEaseFactor(double easeFactor, QualityOfRecall qualityOfRecall){
        double newEf = easeFactor +
                (0.1 - (5 - qualityOfRecall.getScore()) * (0.08 + (5 - qualityOfRecall.getScore()) * 0.02));
        return Math.max(1.3, newEf);
    }
}
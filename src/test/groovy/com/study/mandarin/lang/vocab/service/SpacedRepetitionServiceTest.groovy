package com.study.mandarin.lang.vocab.service

import com.study.mandarin.lang.vocab.dto.Memory
import com.study.mandarin.lang.vocab.dto.QualityOfRecall
import spock.lang.Specification
import spock.lang.Subject

import java.time.LocalDate

class SpacedRepetitionServiceTest extends Specification {

    @Subject
    def service = new SpacedRepetitionService()

    private static Memory mem(int reps = 0, double ef = 2.5, int prev = 0) {
        new Memory(repetitions: reps, easeFactor: ef, lastInterval: prev)
    }

    def "calculateRepetitions increments on successful recall"() {
        expect:
        service.calculateRepetitions(mem(5), QualityOfRecall.FIVE) == 6
        service.calculateRepetitions(mem(0), QualityOfRecall.THREE) == 1
    }

    def "calculateRepetitions resets on failure"() {
        expect:
        service.calculateRepetitions(mem(10), QualityOfRecall.ZERO) == 0
        service.calculateRepetitions(mem(3), QualityOfRecall.ONE) == 0
    }

    def "easeFactor never drops below 1.3"() {
        expect:
        service.calculateEaseFactor(1.3, QualityOfRecall.ZERO) == 1.3
    }

    def "easeFactor increases on good recall"() {
        expect:
        service.calculateEaseFactor(2.5, QualityOfRecall.FIVE) > 2.5
    }

    def "easeFactor decreases on poor recall"() {
        expect:
        service.calculateEaseFactor(2.5, QualityOfRecall.ZERO) < 2.5
    }


    def "interval seeds follow rules (1 then 6)"() {
        expect:
        service.calculateInterval(0, 2.5, 0) == 1
        service.calculateInterval(1, 2.5, 1) == 6
    }

    def "interval grows using ease factor after second repetition"() {
        given:
        def prev = 6

        when:
        def next = service.calculateInterval(2, 2.0, prev)

        then:
        next == Math.round(prev * 2.0)
    }

    def "interval increases as repetitions progress (sanity check)"() {
        given:
        def i1 = service.calculateInterval(2, 2.5, 6)
        def i2 = service.calculateInterval(3, 2.5, i1)

        expect:
        i2 > i1
    }

    def "failure always schedules next day"() {
        given:
        def memory = mem(5)

        when:
        def result = service.calculateNextReviewDate(memory, QualityOfRecall.ZERO)

        then:
        result == LocalDate.now().plusDays(1)
    }

    def "successful recall returns at least 1 day (due to fuzz)"() {
        given:
        def memory = mem(0)

        when:
        def result = service.calculateNextReviewDate(memory, QualityOfRecall.FIVE)

        then:
        result.isAfter(LocalDate.now().minusDays(1))
    }

    def "successful recall result is not wildly off interval (fuzz bounded sanity check)"() {
        given:
        def memory = mem(0)

        when:
        def result = service.calculateNextReviewDate(memory, QualityOfRecall.FIVE)

        then:
        def interval = service.calculateInterval(
                service.calculateRepetitions(memory, QualityOfRecall.FIVE),
                service.calculateEaseFactor(memory.easeFactor, QualityOfRecall.FIVE),
                memory.getLastInterval()
        )

        def min = LocalDate.now().plusDays(1)
        def max = LocalDate.now().plusDays(Math.max(1, (int)(interval * 1.2) + 1))

        result.isAfter(min.minusDays(1))
        result.isBefore(max.plusDays(1))
    }
}
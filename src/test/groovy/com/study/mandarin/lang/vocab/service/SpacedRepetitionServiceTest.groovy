package com.study.mandarin.lang.vocab.service

import spock.lang.Specification

import java.time.LocalDate

class SpacedRepetitionServiceTest extends Specification {

    def service = new SpacedRepetitionService()

    def "calculateNewStreak increments when success is true"() {
        expect:
        service.calculateNewStreak(3, true) == 4
    }

    def "calculateNewStreak resets to 0 when success is false"() {
        expect:
        service.calculateNewStreak(5, false) == 0
    }

    def "calculateNextReviewDate returns tomorrow when failure"() {
        given:
        def today = LocalDate.now()

        when:
        def result = service.calculateNextReviewDate(5, false)

        then:
        result == today.plusDays(1)
    }

    def "calculateNextReviewDate returns future date when success (non-deterministic)"() {
        given:
        def today = LocalDate.now()

        when:
        def result = service.calculateNextReviewDate(2, true)

        then:
        result.isAfter(today)
    }
}
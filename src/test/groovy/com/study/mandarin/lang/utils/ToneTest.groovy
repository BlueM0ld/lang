package com.study.mandarin.lang.utils

import spock.lang.Specification

class ToneTest extends Specification {

    def "should detect tones in single syllable (decomposed form)"() {
        expect:
        Tone.extractAll("a\u0304") == [Tone.FIRST]   // ā
        Tone.extractAll("a\u0301") == [Tone.SECOND]  // á
        Tone.extractAll("a\u030C") == [Tone.THIRD]   // ǎ
        Tone.extractAll("a\u0300") == [Tone.FOURTH]  // à
    }

    def "should detect tones in single syllable (precomposed Unicode form)"() {
        expect:
        Tone.extractAll("ā") == [Tone.FIRST]
        Tone.extractAll("á") == [Tone.SECOND]
        Tone.extractAll("ǎ") == [Tone.THIRD]
        Tone.extractAll("à") == [Tone.FOURTH]
    }

    def "should detect tones in real Pinyin word ni"() {
        expect:
        Tone.extractAll("nǐ") == [Tone.THIRD]
        Tone.extractAll("ní") == [Tone.SECOND]
        Tone.extractAll("nī") == [Tone.FIRST]
        Tone.extractAll("nì") == [Tone.FOURTH]
    }

    def "should detect tones in multi-syllable phrase ni hao"() {
        expect:
        Tone.extractAll("nǐ hǎo") == [Tone.THIRD, Tone.THIRD]
    }

    def "should detect tones in longer phrase with multiple syllables"() {
        expect:
        Tone.extractAll("nǐ hǎo ma") == [Tone.THIRD, Tone.THIRD, Tone.NEUTRAL]
    }

    def "should return NEUTRAL when no tone marks exist"() {
        expect:
        Tone.extractAll("ni hao") == [Tone.NEUTRAL]
    }

    def "should handle mixed spacing and punctuation"() {
        expect:
        Tone.extractAll("nǐ, hǎo!") == [Tone.THIRD, Tone.THIRD]
    }

    def "should handle empty string safely"() {
        expect:
        Tone.extractAll("") == []
    }

    def "should be robust to different Unicode representations of same syllable"() {
        expect:
        Tone.extractAll("a\u0301") == Tone.extractAll("á")
    }

    def "should extract multiple tones including mixed accents"() {
        expect:
        Tone.extractAll("a\u0301a\u0300") == [Tone.SECOND, Tone.FOURTH]
    }
}
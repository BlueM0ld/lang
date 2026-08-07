package com.study.mandarin.lang.drill.dto.questions;

public sealed interface DrillQuestion permits FreeRecallQuestion, ListeningQuestion, ReadingQuestion, RecognitionQuestion, ShadowingQuestion, SpeakingQuestion, ToneQuestion, WritingQuestion {
}

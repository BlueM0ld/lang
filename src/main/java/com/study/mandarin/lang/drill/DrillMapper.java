package com.study.mandarin.lang.drill;

import com.study.mandarin.lang.drill.dto.options.*;
import com.study.mandarin.lang.drill.dto.questions.*;
import com.study.mandarin.lang.vocab.dto.VocabItemDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DrillMapper {

    FreeRecallQuestion toDrillQuestion(FreeRecallOption option);
    ListeningQuestion toDrillQuestion(ListeningOption option);
    ReadingQuestion toDrillQuestion(ReadingOption option);
    ShadowingQuestion toDrillQuestion(ShadowingOption option);
    RecognitionQuestion toDrillQuestion(RecognitionOption option);
    SpeakingQuestion toDrillQuestion(SpeakingOption option);
    ToneQuestion toDrillQuestion(ToneOption option);
    WritingQuestion toDrillQuestion(WritingOption option);

    FreeRecallOption toFreeRecallOption(VocabItemDTO source);
    ListeningOption toListeningOption(VocabItemDTO source);
    ReadingOption toReadingOption(VocabItemDTO source);
    RecognitionOption toRecognitionOption(VocabItemDTO source);
    ShadowingOption toShadowingOption(VocabItemDTO source);
    SpeakingOption toSpeakingOption(VocabItemDTO source);
    ToneOption toToneOption(VocabItemDTO source);
    WritingOption toWritingOption(VocabItemDTO source);

}
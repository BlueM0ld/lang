package com.study.mandarin.lang.drill.dto;

import com.study.mandarin.lang.vocab.dto.VocabItemDTO;
import lombok.Builder;
import lombok.Data;
import java.util.List;

@Builder
@Data
public class DrillDto {
    DrillQuestionDto vocabQuestion;
    List<DrillOptionDto> vocabItemOptions;
}

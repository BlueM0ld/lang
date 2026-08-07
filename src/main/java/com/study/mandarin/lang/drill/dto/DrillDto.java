package com.study.mandarin.lang.drill.dto;

import com.study.mandarin.lang.drill.dto.options.DrillOption;
import com.study.mandarin.lang.drill.dto.questions.DrillQuestion;
import lombok.Builder;
import lombok.Data;
import java.util.List;

@Builder
@Data
public class DrillDto {
    DrillQuestion vocabQuestion;
    List<DrillOption> vocabItemOptions;
}

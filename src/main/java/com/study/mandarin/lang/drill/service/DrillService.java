package com.study.mandarin.lang.drill.service;

import com.study.mandarin.lang.drill.DrillMapper;
import com.study.mandarin.lang.drill.dto.DrillType;
import com.study.mandarin.lang.drill.dto.DrillDto;
import com.study.mandarin.lang.drill.dto.DrillResultRequest;
import com.study.mandarin.lang.drill.dto.options.DrillOption;
import com.study.mandarin.lang.drill.dto.options.RecognitionOption;
import com.study.mandarin.lang.vocab.dto.QualityOfRecall;
import com.study.mandarin.lang.vocab.service.VocabService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DrillService {

    private final VocabService vocabService;
    private final DrillMapper drillMapper;

    private static final int NUMBER_OF_RANDOM_VOCAB = 10;
    private static final int NUMBER_OF_OPTIONS = 5;
    private static final int DRILL_QUESTIONS=5;

    public List<DrillDto> getDrill(DrillType drillType) {
        switch (drillType){
            case FREE_RECALL, READING, LISTENING, SHADOWING, SPEAKING, TONE_PAIR, WRITING -> {
                return  List.of();
            }
            case RECOGNITION -> {
                return getRecognitionDrill();
            }
        }
        return List.of();
    }


    public List<DrillDto> getRecognitionDrill() {

        List<RecognitionOption> vocabItems =
                vocabService.getRandomVocabList(NUMBER_OF_RANDOM_VOCAB).stream().map(drillMapper::toRecognitionOption).toList();

        return vocabItems.stream()
                .map(question -> createDrill(question, vocabItems))
                .limit(DRILL_QUESTIONS)
                .toList();
    }

    private DrillDto createDrill(
            RecognitionOption question,
            List<RecognitionOption> allItems
    ) {

        List<RecognitionOption> shuffled = new ArrayList<>(allItems);
        Collections.shuffle(shuffled);

        List<DrillOption> options = shuffled.stream()
                .filter(item -> !item.character().equals(question.character()))
                .limit(NUMBER_OF_OPTIONS - 1)
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

        options.add(question);

        Collections.shuffle(options);

        return DrillDto.builder()
                .vocabQuestion(drillMapper.toDrillQuestion(question))
                .vocabItemOptions(options)
                .build();
    }

    public String postDrillVerification(DrillResultRequest request) {
        var id = request.vocabId();
        DrillType drillType = request.drillType();
        QualityOfRecall qualityOfRecall = request.qualityOfRecall();

        return vocabService.recordDrillResult(id, drillType,qualityOfRecall);
    }

}

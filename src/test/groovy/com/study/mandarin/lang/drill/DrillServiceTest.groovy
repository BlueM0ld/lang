package com.study.mandarin.lang.drill

import com.study.mandarin.lang.drill.dto.*
import com.study.mandarin.lang.vocab.dto.QualityOfRecall
import com.study.mandarin.lang.vocab.dto.VocabItemDTO
import com.study.mandarin.lang.vocab.service.VocabService
import org.mapstruct.factory.Mappers
import spock.lang.Specification

class DrillServiceTest extends Specification {

    def vocabService = Mock(VocabService)
    DrillMapper drillMapper = Mappers.getMapper(DrillMapper)

    def service = new DrillService(vocabService, drillMapper as DrillMapper)

    def "getDrill returns 5 questions each with 5 options"() {
        given:
        vocabService.getRandomVocabList(10) >> (1..10).collect {
            new VocabItemDTO("id$it", "char$it", "pin$it", "mean$it")
        }

        when:
        def result = service.getDrill()

        then:
        result.size() == 5
        result.every { it.vocabItemOptions.size() == 5 }
        def allOptionSets = result.collect { drill ->
            drill.vocabItemOptions.collect { it.character() }.toSet()
        }
        allOptionSets.unique().size() > 1
    }

    def "postDrillVerification calls vocabService and returns id"() {
        given:
        VocabItemDTO vocabItemDTO = new VocabItemDTO("id","character","pinyin", "meaning");
        def request = new DrillResultRequest("id", QualityOfRecall.ZERO)

        when:
        def result = service.postDrillVerification(request)

        then:
        1 * vocabService.recordDrillResult("id", QualityOfRecall.ZERO)
    }
}
package com.study.mandarin.lang.vocab.service

import com.study.mandarin.lang.exception.VocabItemNotFoundException
import com.study.mandarin.lang.vocab.VocabMapper
import com.study.mandarin.lang.vocab.VocabMapperImpl
import com.study.mandarin.lang.vocab.VocabRepository
import com.study.mandarin.lang.vocab.dto.AddVocab
import com.study.mandarin.lang.vocab.dto.Memory
import com.study.mandarin.lang.vocab.dto.QualityOfRecall
import com.study.mandarin.lang.vocab.dto.UpdateVocab
import com.study.mandarin.lang.vocab.dto.VocabItemDTO
import com.study.mandarin.lang.vocab.model.VocabItem
import spock.lang.Specification

import java.time.LocalDate


class VocabServiceTest extends Specification {

    VocabRepository vocabRepository = Mock()
    SpacedRepetitionService spacedRepetitionService = Mock()

    VocabMapper mapper = new VocabMapperImpl()

    VocabService service = new VocabService(
            vocabRepository,
            spacedRepetitionService,
            mapper
    )

    def "should return all vocab items"() {
        given:
        def vocabItem = new VocabItem(
                id: "1",
                character: "你",
                pinyin: "nǐ",
                meaning: "you",
                confidenceScore: 5
        )

        vocabRepository.findAll() >> [vocabItem]

        when:
        List<VocabItemDTO> result = service.getAllVocab()

        then:
        result.size() == 1
        result[0].character() == "你"
        result[0].pinyin() == "nǐ"
        result[0].meaning() == "you"
        result[0].confidenceScore() == 50  //confidence is a percentage

    }

    def "should return random vocab list"() {
        given:
        def vocabItem = new VocabItem(
                id: "1",
                character: "好",
                pinyin: "hǎo",
                meaning: "good",
                confidenceScore: 3
        )

        vocabRepository.getRandomSample(5) >> [vocabItem]

        when:
        List<VocabItemDTO> result = service.getRandomVocabList(5)

        then:
        result.size() == 1
        result[0].character() == "好"
        result[0].meaning() == "good"
        result[0].confidenceScore() == 30  //confidence is a percentage
    }

    def "should get vocab item by id"() {
        given:
        def vocabItem = new VocabItem(
                id: "123",
                character: "学",
                confidenceScore: 2
        )

        vocabRepository.findById("123") >> Optional.of(vocabItem)

        when:
        VocabItem result = service.getVocabItem("123")

        then:
        result.id == "123"
        result.character == "学"
    }

    def "should throw exception when vocab item not found by id"() {
        given:
        vocabRepository.findById("123") >> Optional.empty()

        when:
        service.getVocabItem("123")

        then:
        thrown(VocabItemNotFoundException)
    }

    def "should add vocab item"() {
        given:
        def addVocab = new AddVocab(
                "谢谢",
                "xiè xie",
                "thank you"
        )

        def savedItem = new VocabItem(
                id: "abc123",
                character: "谢谢",
                pinyin: "xiè xie",
                meaning: "thank you",
                confidenceScore: 0
        )

        when:
        String result = service.addVocab(addVocab)

        then:
        result == "abc123"

        1 * vocabRepository.save({
            it.character == "谢谢" &&
                    it.pinyin == "xiè xie" &&
                    it.meaning == "thank you"
        }) >> savedItem
    }

    def "should update vocab item"() {
        given:
        def updateVocab = new UpdateVocab(
                "123",
                "再见",
                "zài jiàn",
                "goodbye"
        )

        when:
        String result = service.updateVocab(updateVocab)

        then:
        1 * vocabRepository.updateVocab(updateVocab)
        result == "123"
    }

    def "should delete vocab item"() {
        when:
        String result = service.deleteVocab("123")

        then:
        1 * vocabRepository.deleteById("123")
        result == "123"
    }

    def "should silently succeed when deleting non existent vocab item"() {
        when:
        String result = service.deleteVocab("missing-id")

        then:
        1 * vocabRepository.deleteById("missing-id")
        result == "missing-id"
    }


    def "should record drill result and persist updated memory"() {
        given:
        def dto = new VocabItemDTO("你", "nǐ", "you", 1)

        def item = new VocabItem(
                id: "1",
                character: "你",
                meaning: "you",
                memory: Memory.initial(),
                confidenceScore: 1
        )

        vocabRepository.findByCharacterAndMeaning("你", "you") >>
                Optional.of(item)

        spacedRepetitionService.calculateUpdatedMemory(_ as Memory, _ as QualityOfRecall) >>
                new Memory(1, 2.5, 3)

        spacedRepetitionService.calculateNextReviewDate(_ as Memory, _ as QualityOfRecall) >>
                LocalDate.now().plusDays(1)

        when:
        service.recordDrillResult(dto, QualityOfRecall.ZERO)

        then:
        1 * vocabRepository.updateVocabMemory(
                "1",
                {it.repetitions==1 && it.easeFactor==2.5 && it.lastInterval==3},
                { it instanceof LocalDate }
        )
    }

    def "should throw exception when recording drill result for missing vocab"() {
        given:
        def dto = new VocabItemDTO(
                "你",
                "nǐ",
                "you",
                0
        )


        vocabRepository.findByCharacterAndMeaning("你", "you") >>
                Optional.empty()

        when:
        service.recordDrillResult(dto, QualityOfRecall.ZERO)

        then:
        thrown(VocabItemNotFoundException)
    }

    def "should get vocab with filters"() {
        given:
        def vocabItem = new VocabItem(
                id: "1",
                character: "水",
                pinyin: "shuǐ",
                meaning: "water",
                confidenceScore: 4
        )

        vocabRepository.findWithFilters("water", true) >> [vocabItem]

        when:
        List<VocabItemDTO> result = service.getVocab("water", true)

        then:
        result.size() == 1
        result[0].character() == "水"
        result[0].meaning() == "water"
        result[0].confidenceScore() == 40  //confidence is a percentage

    }

    def "should return empty list when no vocab matches filters"() {
        given:
        vocabRepository.findWithFilters("xyz", false) >> []

        when:
        List<VocabItemDTO> result = service.getVocab("xyz", false)

        then:
        result.isEmpty()
    }
}
package com.study.mandarin.lang.vocab.service

import com.study.mandarin.lang.exception.VocabItemNotFoundException
import com.study.mandarin.lang.vocab.dto.VocabMemory
import com.study.mandarin.lang.vocab.persistence.adapter.VocabMapper
import com.study.mandarin.lang.vocab.persistence.VocabRepository
import com.study.mandarin.lang.vocab.dto.AddVocab
import com.study.mandarin.lang.vocab.dto.Memory
import com.study.mandarin.lang.vocab.dto.QualityOfRecall
import com.study.mandarin.lang.vocab.dto.UpdateVocab
import com.study.mandarin.lang.vocab.dto.VocabItemDTO
import com.study.mandarin.lang.vocab.model.VocabItem
import org.mapstruct.factory.Mappers
import spock.lang.Specification

import java.time.LocalDate


class VocabServiceTest extends Specification {

    VocabRepository vocabRepository = Mock()
    SpacedRepetitionService spacedRepetitionService = Mock()

    VocabMapper mapper = Mappers.getMapper(VocabMapper)

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
        )

        vocabRepository.findAll() >> [vocabItem]

        when:
        List<VocabItemDTO> result = service.getAllVocab()

        then:
        result.size() == 1
        result[0].character() == "你"
        result[0].pinyin() == "nǐ"
        result[0].meaning() == "you"

    }

    def "should return random vocab list"() {
        given:
        def vocabItem = new VocabItem(
                id: "1",
                character: "好",
                pinyin: "hǎo",
                meaning: "good",
        )

        vocabRepository.getRandomSample(5) >> [vocabItem]

        when:
        List<VocabItemDTO> result = service.getRandomVocabList(5)

        then:
        result.size() == 1
        result[0].character() == "好"
        result[0].meaning() == "good"
    }

    def "should get vocab item by id"() {
        given:
        def vocabItem = new VocabItem(
                id: "123",
                character: "学",
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
        def memoryModal = "read"
        def dto = new VocabItemDTO("1","你", "nǐ", "you")


        def item = new VocabItem(
                id: "232425",
                vocabId: "1",
                character: "你",
                pinyin: "nǐ",
                meaning: "you",
                vocabMemory: VocabMemory.initial(),
                nextReviewDate: LocalDate.now(),
                available: true
        )

        vocabRepository.findByVocabId("1") >>
                Optional.of(item)

        spacedRepetitionService.calculateUpdatedMemory(_, _) >>
                new Memory(1, 2.5, 3)

        spacedRepetitionService.calculateNextReviewDate(_, _) >>
                LocalDate.now().plusDays(1)

        when:
        service.recordDrillResult("1", memoryModal, QualityOfRecall.ZERO)

        then:
        1 * vocabRepository.updateVocabMemory(
                "1",
                { it.repetitions == 1 && it.easeFactor == 2.5 && it.lastInterval == 3 },
                _ as LocalDate
        )
    }

    def "should throw exception when recording drill result for missing vocab"() {
        given:
        def memoryModal = "read"
        def dto = new VocabItemDTO(
                "1",
                "你",
                "nǐ",
                "you"
        )


        vocabRepository.findByVocabId("1") >>
                Optional.empty()

        when:
        service.recordDrillResult("1", memoryModal, QualityOfRecall.ZERO)

        then:
        thrown(VocabItemNotFoundException)
    }

    def "should get vocab with filters"() {
        given:
        def vocabItem = new VocabItem(
                id: "1",
                character: "水",
                pinyin: "shuǐ",
                meaning: "water"
        )

        vocabRepository.findWithFilters("water", true) >> [vocabItem]

        when:
        List<VocabItemDTO> result = service.getVocab("water", true)

        then:
        result.size() == 1
        result[0].character() == "水"
        result[0].meaning() == "water"

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
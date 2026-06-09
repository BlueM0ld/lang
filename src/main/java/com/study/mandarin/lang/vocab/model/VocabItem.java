package com.study.mandarin.lang.vocab.model;

import com.study.mandarin.lang.vocab.dto.Memory;
import com.study.mandarin.lang.vocab.dto.VocabMemory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@Document(collection = "VocabItem")
@CompoundIndex(def = "{'character': 1, 'meaning': 1}", unique = true)
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
public class VocabItem {

    @Id
    private String id;

    private String vocabId;
    private String character;
    private String pinyin;
    private String meaning;
    private String tonePair;
    private VocabMemory vocabMemory;

    @Indexed
    private LocalDate nextReviewDate;
    // TODO: Controls staged vocabulary introduction.
    // false = not yet unlocked for study.
    // Unlock ordering will be driven by semantic similarity via LLM.
    // Currently all words added as available = true pending implementation.
    @Indexed
    private boolean available;
}

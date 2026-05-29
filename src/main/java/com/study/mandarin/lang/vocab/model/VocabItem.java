package com.study.mandarin.lang.vocab.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@Document(collection = "VocabItem")
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
public class VocabItem {

    @Id
    private String id;
    private String character;
    private String pinyin;
    private String meaning;
    private int confidenceScore;
    private int streak;
    private LocalDate nextReviewDate;
    private boolean available;
}

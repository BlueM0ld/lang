package com.study.mandarin.lang.vocab;

import com.study.mandarin.lang.vocab.dto.UpdateVocab;
import com.study.mandarin.lang.vocab.dto.VocabItemDTO;
import com.study.mandarin.lang.vocab.model.VocabItem;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Repository
@RequiredArgsConstructor
public class CustomVocabRepositoryImpl implements CustomVocabRepository{


    private final MongoOperations mongoOperations;

    @Override
    public List<VocabItem> getRandomSample(int size) {
        LocalDate now = LocalDate.now();

        var aggregation = Aggregation.newAggregation(
                match(Criteria.where("available").eq(true)
                        .and("nextReviewDate").lte(now)),
                sort(Sort.Direction.DESC, "nextReviewDate"),
                sample(size)
        );

        AggregationResults<VocabItem> items = mongoOperations.aggregate(aggregation, "VocabItem", VocabItem.class);
        return items.getMappedResults();
    }

    @Override
    public void updateConfidence(VocabItem vocabItem, boolean success) {

        LocalDate newDate = updateNextReviewDate(
                vocabItem.getNextReviewDate(),
                success
        );

        var current = vocabItem.getConfidenceScore();
        var delta = success? 2 :-2;
        var  newConfidenceScore = Math.max(0, Math.min(20, current + delta));
        Query query = new Query(
                Criteria.where("_id").is(vocabItem.getId())
        );

        Update update = new Update()
                .set("confidenceScore", newConfidenceScore)
                .set("nextReviewDate", newDate);

        mongoOperations.updateFirst(query, update, VocabItem.class);
    }

    @Override
    public void updateVocab(UpdateVocab vocab) {
        Query query = new Query(
                Criteria.where("_id").is(vocab.id())
        );

        Update update = new Update()
                .set("character", vocab.character())
                .set("pinyin",vocab.pinyin())
                .set("meaning",vocab.meaning());

        mongoOperations.updateFirst(query, update, VocabItem.class);


    }

    @Override
    public List<VocabItem> findWithFilters(String search, boolean dueOnly) {

        Query query = new Query();

        if (search != null && !search.isBlank()) {

            Criteria searchCriteria = new Criteria().orOperator(
                    Criteria.where("character").regex(search, "i"),
                    Criteria.where("meaning").regex(search, "i")
            );

            query.addCriteria(searchCriteria);
        }

        // Only items due today or earlier
        if (dueOnly) {

            query.addCriteria(
                    Criteria.where("nextReviewDate")
                            .lte(LocalDate.now())
            );
        }

        return mongoOperations.find(query, VocabItem.class);
    }


    public LocalDate updateNextReviewDate(LocalDate currentNextReviewDate, boolean success) {

        LocalDate now = LocalDate.now();

        if (success) {
            return currentNextReviewDate.plusDays(2);
        } else {
            LocalDate minus = currentNextReviewDate.minusDays(2);
            return minus.isBefore(now)
                    ? now.plusDays(1)
                    : minus;
        }
    }
}

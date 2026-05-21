package com.study.mandarin.lang.vocab;

import com.study.mandarin.lang.vocab.model.VocabItem;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CustomVocabRepositoryImpl implements CustomVocabRepository{


    private final MongoOperations mongoOperations;

    @Override
    public List<VocabItem> getRandomSample(int size) {

        var aggregation = Aggregation.newAggregation(
                Aggregation.sample(size)
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

        Query query = new Query(
                Criteria.where("_id").is(vocabItem.getId())
        );

        Update update = new Update()
                .inc("confidenceScore", success ? 2 : -2)
                .set("nextReviewDate", newDate);

        mongoOperations.updateFirst(query, update, VocabItem.class);
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

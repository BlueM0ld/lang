package com.study.mandarin.lang.vocab;

import com.study.mandarin.lang.vocab.dto.UpdateVocab;
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

    @Override
    public void updateScheduling(VocabItem vocabItem, LocalDate nextReviewDate, int newStreak) {
        Query query = new Query(
                Criteria.where("_id").is(vocabItem.getId())
        );

        Update update = new Update()
                .set("streak", newStreak)
                        .set("nextReviewDate", nextReviewDate);

        mongoOperations.updateFirst(query, update, VocabItem.class);
    }

}

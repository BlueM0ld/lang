package com.study.mandarin.lang.vocab;


import com.study.mandarin.lang.vocab.model.VocabItem;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface VocabRepository extends MongoRepository<VocabItem,String >, CustomVocabRepository {

    List<VocabItem> findByAvailableTrue();
}

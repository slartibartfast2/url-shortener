package ea.slartibartfast2.urlshortener.service;

import ea.slartibartfast2.urlshortener.model.entity.CustomSequence;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@RequiredArgsConstructor
@Service
public class CustomSequenceService {

    private final MongoOperations mongoOperations;

    public long getNextValue(String sequenceName) {
        CustomSequence sequence = mongoOperations.findAndModify(
                query(where("_id").is(sequenceName)),
                new Update().inc("seq", 1),
                options().returnNew(true),
                CustomSequence.class);
        if (sequence == null) {
            sequence = new CustomSequence();
            sequence.setId(sequenceName);
            sequence.setSeq(100000);
            mongoOperations.insert(sequence);
        }
        return sequence.getSeq();
    }

}

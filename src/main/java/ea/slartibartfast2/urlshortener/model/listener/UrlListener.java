package ea.slartibartfast2.urlshortener.model.listener;

import ea.slartibartfast2.urlshortener.model.entity.Url;
import ea.slartibartfast2.urlshortener.service.CustomSequenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UrlListener extends AbstractMongoEventListener<Url> {

    private final CustomSequenceService customSequenceService;
    private static final String URL_SEQUENCE_NAME = "urlSequence";

    @Override
    public void onBeforeConvert(final BeforeConvertEvent<Url> event) {
        if (event.getSource().getId() == null) {
            event.getSource().setId(customSequenceService.getNextValue(URL_SEQUENCE_NAME));
        }
    }

}

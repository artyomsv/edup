package lv.company.edup.infrastructure.configuration;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

public class ApplicationProperties extends ResourceBundle {

    @Inject
    public ApplicationProperties(@Bundle("application") Instance<ResourceProducer> instance) {
        super(instance.get());
    }

}

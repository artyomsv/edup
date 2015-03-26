package lv.company.edup.infrastructure.configuration;

import javax.enterprise.context.ApplicationScoped;
import java.util.Collection;
import java.util.Collections;

@Bundle("application")
@ApplicationScoped
class ApplicationResourceProducer implements ResourceProducer {

    @Override
    public Collection<String> getResources() {
        return Collections.singleton("application.properties");
    }

}

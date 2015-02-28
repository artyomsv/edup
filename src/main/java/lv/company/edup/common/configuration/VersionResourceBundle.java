package lv.company.edup.common.configuration;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

@ApplicationScoped
@Bundle("version")
public class VersionResourceBundle extends ResourceBundle {


    public VersionResourceBundle() {
    }

    @Inject
    public VersionResourceBundle(@Bundle("version") Instance<ResourceProducer> instance) {
        super(instance.get());
    }

}

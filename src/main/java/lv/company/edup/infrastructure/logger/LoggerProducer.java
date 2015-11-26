package lv.company.edup.infrastructure.logger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

@ApplicationScoped
public class LoggerProducer {

    @Produces
    public Logger getLogger(InjectionPoint point) {
        return LogManager.getLogger(point != null ? point.getMember().getDeclaringClass().getSimpleName() : "Unknown");
    }

}

package lv.company.edup.infrastructure.time;

import org.apache.logging.log4j.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import java.util.TimeZone;

@Singleton
@Startup
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class AppTimeZone {

    public static final String EUROPE_RIGA = "Europe/Riga";
    public static final TimeZone TIME_ZONE = TimeZone.getTimeZone(EUROPE_RIGA);

    @Inject Logger logger;

    @PostConstruct
    public void init() {
        logger.info("Init application time zone");
        TimeZone.setDefault(TimeZone.getTimeZone(EUROPE_RIGA));
        logger.info("Time zone is set to {}", TimeZone.getDefault().getID());
    }

}

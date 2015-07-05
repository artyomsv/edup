package lv.company.edup.infrastructure.templates.impl.templates;

import lv.company.edup.infrastructure.templates.api.ContextCreator;
import lv.company.edup.infrastructure.templates.impl.templates.dto.FakturaData;
import lv.company.edup.infrastructure.utils.ApplicationUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import javax.enterprise.context.ApplicationScoped;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class FakturaContextCreator implements ContextCreator<FakturaData> {

    @Override
    public Map<String, Object> create(FakturaData from) {
        Map<String, Object> map = new HashMap<>();

        map.put("PaymentDate", DateFormatUtils.format(new Date(), "yyyy/dd/MM"));
        map.put("PaymentDescription", ApplicationUtils.getValue(from.getPaymentDescription()));

        Long paymentVat = Math.round(from.getPaymentTotal() * 0.21);
        Long paymentWithoutVat = from.getPaymentTotal() - paymentVat;

        map.put("PaymentWithoutVat", ApplicationUtils.getAmountValue(paymentWithoutVat));
        map.put("PaymentVat", ApplicationUtils.getAmountValue(paymentVat));
        map.put("PaymentTotal", ApplicationUtils.getAmountValue(from.getPaymentTotal()));
        map.put("PaymentTotalInWords", ApplicationUtils.formatAmount(from.getPaymentTotal()));
        map.put("PaymentId", ApplicationUtils.getValue(from.getPaymentId()));


        return map;
    }

}

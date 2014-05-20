package org.asuki.model.jaxb;

import static org.joda.time.DateTimeZone.UTC;

import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.joda.time.format.ISODateTimeFormat;

public class IsoDateAdapter extends XmlAdapter<String, Date> {

    @Override
    public Date unmarshal(String v) throws Exception {
        return new Date(ISODateTimeFormat.dateTimeNoMillis().withZone(UTC)
                .parseMillis(v));
    }

    @Override
    public String marshal(Date v) throws Exception {
        return ISODateTimeFormat.dateTimeNoMillis().withZone(UTC)
                .print(v.getTime());
    }

}

package com.restapi.demo.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author G.Nikolov on 19/10/18
 * @project rest-service-basic
 */
public class ZonedDateTimeSerializer extends JsonSerializer<ZonedDateTime>
{
    @Override
    public void serialize(ZonedDateTime localDateTime,
                          JsonGenerator jsonGenerator,
                          SerializerProvider serializerProvider) throws IOException
    {
        DateTimeFormatter fmt = DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneOffset.UTC);
        jsonGenerator.writeString(fmt.format(localDateTime));
    }
}

package com.restapi.demo.test.utils;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.io.ByteArrayOutputStream;

/**
 * @author G.Nikolov on 08/10/18
 * @project rest-service-basic
 */
public final class TestUtils {

    public static String creatStringOfLength(int l)
    {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < l; i++ )
        {
            sb.append("-");
        }

        return sb.toString();
    }

    public static ZonedDateTime dateTimeParser(String dateNtime)
    {
        return ZonedDateTime.parse(dateNtime, DateTimeFormatter.ISO_ZONED_DATE_TIME);
    }

    public static Object deepCopy(Object object)
    {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            return ois.readObject();
        }
        catch (Exception e) {
            //TODO LOG THIS !
            e.printStackTrace();
            return null;
        }
    }
}

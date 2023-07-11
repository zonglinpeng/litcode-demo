package com.bamboovir.litcode.util;

import io.vertx.sqlclient.RowSet;

import java.util.ArrayList;
import java.util.List;

public class Transform {

    public static <T> List<T> rowSetToList(RowSet<T> in) {
        List<T> rst = new ArrayList<T>();
        in.iterator().forEachRemaining(rst::add);
        return rst;
    }
}

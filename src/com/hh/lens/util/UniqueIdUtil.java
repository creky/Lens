package com.hh.lens.util;

import com.hh.lens.AppContext;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.SqlInfo;
import com.lidroid.xutils.db.table.DbModel;
import com.lidroid.xutils.db.table.Table;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hh on 2014/9/21 0021.
 */
public class UniqueIdUtil {
    private static Map<String, Integer> idMap = new HashMap<String, Integer>();

    public static synchronized <T> Integer genId(Class<T> clazz) throws Exception {
        Integer mx = idMap.get(clazz.getName());
        if (mx != null) {
            idMap.put(clazz.getName(), ++mx);
            return mx;
        } else {
            DbUtils db = AppContext.db;
            Table table = Table.get(db, clazz);
            String sql = "select max(" + table.id.getColumnName() + ") MX from " + table.tableName;
            DbModel dbModel = db.findDbModelFirst(new SqlInfo(sql));
            mx = dbModel.getInt("MX");
            idMap.put(clazz.getName(), ++mx);
            return mx;
        }
    }
}

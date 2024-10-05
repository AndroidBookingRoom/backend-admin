package com.example.backend.common;

import org.hibernate.Session;
import org.hibernate.query.NativeQuery;

import java.util.List;

public interface VfData {
    public <T> DataTableResults<T>
    findPaginationQuery(String nativeQuery, String nativeQueryCount,
                        String orderBy, List<Object> paramList, Class obj);

    /**
     *
     * @param nativeQuery
     * @param orderBy
     * @param paramList
     * @param obj
     * @return
     * @param <T>
     */
    public <T> DataTableResults<T> findPaginationQuery(String nativeQuery, String orderBy, List<Object> paramList, Class obj);
//    public <T> DataTableResults<T> findPaginationQuery(String nativeQuery, String orderBy, Map<String, String> paramList, Class obj);


    /**
     * Get session.
     *
     */
    public Session getSession();

    public NativeQuery createNativeQuery(String sql);
    /**
     * ham set result transformer cua cau query
     *
     * @param query
     *            cau query
     * @param obj
     *            doi tuong
     */
    public void setResultTransformer(NativeQuery query, Class obj);

    /**
     * Get list alias column.
     *
     * @param query
     * @return
     */
    public List<String> getReturnAliasColumns(NativeQuery query);
}

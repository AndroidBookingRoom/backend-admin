package com.example.backend.common;

import org.hibernate.Session;
import org.hibernate.query.NativeQuery;

import java.util.List;

public interface VfPagination {
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

    public <T> PaginationResult<T> findPaginationQuery(String nativeQuery, String orderBy, List<Object> paramList, Class obj);

}

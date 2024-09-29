package com.example.backend.common.config;

import com.example.backend.common.CommonUtils;
import com.example.backend.domain.SearchParams;
import com.google.gson.Gson;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.descriptor.java.*;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class VfDataIml implements VfData {

    @PersistenceContext
    private final EntityManager entityManager;

    private final HttpServletRequest req;

    @Override
    public Session getSession() {
        return entityManager.unwrap(Session.class);
    }

    @Override
    public NativeQuery createSQLQuery(String sql) {
//        return getSession().createSQLQuery(sql);
        return getSession().createNativeQuery(sql);
    }

    @Override
    public void setResultTransformer(NativeQuery query, Class obj) {
        Field[] fileds = obj.getDeclaredFields();
        Map<String, String> mapFileds = new HashMap();
        for (Field filed : fileds) {
            mapFileds.put(filed.getName(), filed.getGenericType().toString());
        }
        List<String> aliasColumns = getReturnAliasColumns(query);
        for (String aliasColumn : aliasColumns) {
            String dataType = mapFileds.get(aliasColumn);
            if (CommonUtils.isEmpty(dataType)) {
                log.debug(aliasColumn + " is not defined");
            } else {
                JavaType hbmType = switch (dataType) {
                    case "class java.lang.Long" -> LongJavaType.INSTANCE;
                    case "class java.lang.Integer" -> IntegerJavaType.INSTANCE;
                    case "class java.lang.Double" -> DoubleJavaType.INSTANCE;
                    case "class java.lang.String" -> StringJavaType.INSTANCE;
                    case "class java.lang.Boolean" -> BooleanJavaType.INSTANCE;
                    case "class java.util.Date" -> TimeZoneJavaType.INSTANCE;
                    default -> null;
                };
                if (CommonUtils.isEmpty(hbmType)) {
                    log.debug(dataType + " is not supported");
                } else {
                    query.addScalar(aliasColumn, hbmType.getClass());
                }
            }
        }
        query.setResultTransformer(Transformers.aliasToBean(obj));

    }

    @Override
    public List<String> getReturnAliasColumns(NativeQuery query) {
        List<String> aliasColumns = new ArrayList();
        String sqlQuery = query.getQueryString();
        sqlQuery = sqlQuery.replace("\n", " ");
        sqlQuery = sqlQuery.replace("\t", " ");
        int numOfRightPythis = 0;
        int startPythis = -1;
        int endPythis = 0;
        boolean hasRightPythis = true;
        while (hasRightPythis) {
            char[] arrStr = sqlQuery.toCharArray();
            hasRightPythis = false;
            int idx = 0;
            for (char c : arrStr) {
                if (idx > startPythis) {
                    if ("(".equalsIgnoreCase(String.valueOf(c))) {
                        if (numOfRightPythis == 0) {
                            startPythis = idx;
                        }
                        numOfRightPythis++;
                    } else if (")".equalsIgnoreCase(String.valueOf(c))) {
                        if (numOfRightPythis > 0) {
                            numOfRightPythis--;
                            if (numOfRightPythis == 0) {
                                endPythis = idx;
                                break;
                            }
                        }
                    }
                }
                idx++;
            }
            if (endPythis > 0) {
                sqlQuery = sqlQuery.substring(0, startPythis) + " # " + sqlQuery.substring(endPythis + 1);
                hasRightPythis = true;
                endPythis = 0;
            }
        }
        String arrStr[] = sqlQuery.substring(0, sqlQuery.toUpperCase().indexOf(" FROM ")).split(",");
        for (String str : arrStr) {
            String[] temp = str.trim().split(" ");
            String alias = temp[temp.length - 1].trim();
            if (alias.contains(".")) {
                alias = alias.substring(alias.lastIndexOf(".") + 1).trim();
            }
            if (alias.contains(",")) {
                alias = alias.substring(alias.lastIndexOf(",") + 1).trim();
            }
            if (alias.contains("`")) {
                alias = alias.replace("`", "");
            }
            if (!aliasColumns.contains(alias)) {
                aliasColumns.add(alias);
            }
        }
        return aliasColumns;
    }

    @Override
    public <T> DataTableResults<T> findPaginationQuery(String nativeQuery, String nativeQueryCount, String orderBy, List<Object> paramList, Class obj) {
        return null;
    }

    @Override
    public <T> DataTableResults<T> findPaginationQuery(String nativeQuery, String orderBy,
                                                       List<Object> paramList, Class obj) {
        return findPagination(nativeQuery, orderBy, paramList, obj, 5);
    }

    @Override
    public List<Object> findAllByQuery(String nativeQuery, Class obj) {
        NativeQuery query = createSQLQuery(nativeQuery);
        setResultTransformer(query, obj);
//        Session session = entityManager.unwrap(Session.class);
//        Query query = session.createQuery(nativeQuery)
//                .setResultTransformer(Transformers.aliasToBean(obj));
        return query.getResultList();
    }


    private <T> DataTableResults<T> findPagination(String nativeQuery, String orderBy,
                                                   List<Object> paramList, Class obj, int limit) {
        String _search = req.getParameter("_search");
        SearchParams searchParams = new SearchParams();
        if (!CommonUtils.isNullOrEmpty(_search)) {
            searchParams = new Gson().fromJson(_search, SearchParams.class);
        }
        String paginatedQuery = CommonUtils.buildPaginatedQuery(nativeQuery, orderBy, searchParams);
        String countStrQuery = CommonUtils.buildCountQuery(nativeQuery);
        NativeQuery query = createSQLQuery(paginatedQuery);
        setResultTransformer(query, obj);
        // pagination
        query.setFirstResult(CommonUtils.NVL(searchParams.getFirst()));
        query.setMaxResults(CommonUtils.NVL(searchParams.getRows(), limit));
        NativeQuery countQuery = createSQLQuery(countStrQuery);
        if (!CommonUtils.isNullOrEmpty(paramList)) {
            int paramSize = paramList.size();
            for (int i = 0; i < paramSize; i++) {
                countQuery.setParameter(i + 1, paramList.get(i));
                query.setParameter(i + 1, paramList.get(i));
            }
        }
        List<T> userList = query.list();
        Object totalRecords = countQuery.uniqueResult();

        DataTableResults<T> dataTableResult = new DataTableResults<T>();
        dataTableResult.setData(userList);
        if (!CommonUtils.isEmpty(userList)) {
            dataTableResult.setRecordsTotal(String.valueOf(totalRecords));
            dataTableResult.setRecordsFiltered(String.valueOf(totalRecords));
            dataTableResult.setFirst(String.valueOf(CommonUtils.NVL(searchParams.getFirst())));
        } else {
            dataTableResult.setRecordsFiltered("0");
            dataTableResult.setRecordsTotal("0");
        }

        return dataTableResult;
    }

}

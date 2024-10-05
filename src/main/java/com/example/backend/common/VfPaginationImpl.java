package com.example.backend.common;

import com.example.backend.domain.SearchParams;
import com.google.gson.Gson;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class VfPaginationImpl implements VfPagination{
    @PersistenceContext
    final EntityManager entityManager;

    final HttpServletRequest req;
    @Override
    public Session getSession() {
        return entityManager.unwrap(Session.class);
    }

    @Override
    public NativeQuery createNativeQuery(String sql) {
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
            if (dataType == null) {
                log.debug(aliasColumn + " is not defined");
            } else {
                Class hbmType = null;
                if ("class java.lang.Long".equals(dataType)) {
                    hbmType = Long.class;
                } else if ("class java.lang.Integer".equals(dataType)) {
                    hbmType = Integer.class;
                } else if ("class java.lang.Double".equals(dataType)) {
                    hbmType = Double.class;
                } else if ("class java.lang.String".equals(dataType)) {
                    hbmType = String.class;
                } else if ("class java.lang.Boolean".equals(dataType)) {
                    hbmType = Boolean.class;
                } else if ("class java.util.Date".equals(dataType)) {
                    hbmType = LocalDateTime.class;
                } else if ("class java.time.LocalDateTime".equals(dataType)) {
                    hbmType = LocalDateTime.class;
                }
                if (CommonUtils.isEmpty(hbmType)) {
                    log.debug(dataType + " is not supported");
                } else {
                    query.addScalar(aliasColumn, hbmType);
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
    public <T> PaginationResult<T> findPaginationQuery(String nativeQuery, String orderBy, List<Object> paramList, Class obj) {
        return findPagination(nativeQuery, orderBy, paramList, obj, 10);
    }

    private <T> PaginationResult<T> findPagination(String nativeQuery, String orderBy,
                                                   List<Object> paramList, Class obj, int limit){
        String _search = req.getParameter("_search");
        SearchParams searchParams = new SearchParams();
        if (!CommonUtils.isNullOrEmpty(_search)) {
            searchParams = new Gson().fromJson(_search, SearchParams.class);
        }
        String paginatedQuery = CommonUtils.buildPaginatedQuery(nativeQuery, orderBy, searchParams);
        String countStrQuery = CommonUtils.buildCountQuery(nativeQuery);
        NativeQuery query = createNativeQuery(paginatedQuery);
        setResultTransformer(query, obj);
        // pagination
        query.setFirstResult(CommonUtils.NVL(searchParams.getFirst()));
        query.setMaxResults(CommonUtils.NVL(searchParams.getRows(), limit));
        NativeQuery countQuery = createNativeQuery(countStrQuery);
        if (!CommonUtils.isNullOrEmpty(paramList)) {
            int paramSize = paramList.size();
            for (int i = 0; i < paramSize; i++) {
                countQuery.setParameter(i + 1, paramList.get(i));
                query.setParameter(i + 1, paramList.get(i));
            }
        }
        List<T> resultsLst = query.list();
        Integer totalRecords = (Integer) countQuery.uniqueResult();
        PaginationResult<T> paginationResult = new PaginationResult<T>();
        paginationResult.setData(resultsLst);

        if (!CommonUtils.isNullOrEmpty(resultsLst)) {
            paginationResult.setTotalRecords(CommonUtils.NVL(totalRecords));
            paginationResult.setMaxResult(CommonUtils.NVL(limit));
            paginationResult.setFirst(CommonUtils.NVL(searchParams.getFirst()));
            if (totalRecords % limit == 0) {
                paginationResult.setTotalPages(totalRecords / limit);
            } else {
                paginationResult.setTotalPages((totalRecords / limit) + 1);
            }
        }else {
            paginationResult.setTotalRecords(0);
        }
        return paginationResult;
    }
}

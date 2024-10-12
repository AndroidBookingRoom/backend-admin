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
public class VfDataIml implements VfData {

    @PersistenceContext
    final EntityManager entityManager;

    final HttpServletRequest req;

    @Override
    public Session getSession() {
        return entityManager.unwrap(Session.class);
    }


    @Override
    @SuppressWarnings("deprecation")
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
        String NativeQuery = query.getQueryString();
        NativeQuery = NativeQuery.replace("\n", " ");
        NativeQuery = NativeQuery.replace("\t", " ");
        int numOfRightPythis = 0;
        int startPythis = -1;
        int endPythis = 0;
        boolean hasRightPythis = true;
        while (hasRightPythis) {
            char[] arrStr = NativeQuery.toCharArray();
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
                NativeQuery = NativeQuery.substring(0, startPythis) + " # " + NativeQuery.substring(endPythis + 1);
                hasRightPythis = true;
                endPythis = 0;
            }
        }
        String arrStr[] = NativeQuery.substring(0, NativeQuery.toUpperCase().indexOf(" FROM ")).split(",");
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
        return findPagination(nativeQuery, orderBy, paramList, obj, 10);
    }

    @Override
    public <T> List<T> findAllData(String nativeQuery, String orderBy, List<Object> paramList, Class obj) {
        return findALl(nativeQuery, orderBy, paramList, obj);
    }

    private <T> DataTableResults<T> findPagination(String nativeQuery, String orderBy,
                                                   List<Object> paramList, Class obj, int limit) {
        log.info("[VF DATA IMPL] findPagination");
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
        @SuppressWarnings("unchecked")
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

    private <T> List<T> findALl(String nativeQuery, String orderBy,
                                                   List<Object> paramList, Class obj) {
        log.info("[VF DATA IMPL] findALl");
        String paginatedQuery = CommonUtils.buildPaginatedQuery(nativeQuery, orderBy, null);
        String countStrQuery = CommonUtils.buildCountQuery(nativeQuery);
        NativeQuery query = createNativeQuery(paginatedQuery);
        setResultTransformer(query, obj);
        // pagination
        NativeQuery countQuery = createNativeQuery(countStrQuery);
        if (!CommonUtils.isNullOrEmpty(paramList)) {
            int paramSize = paramList.size();
            for (int i = 0; i < paramSize; i++) {
                countQuery.setParameter(i + 1, paramList.get(i));
                query.setParameter(i + 1, paramList.get(i));
            }
        }
        @SuppressWarnings("unchecked")
        List<T> userList = query.list();

        return userList;
    }

}

package com.adobe.aem.guides.wknd.core.services.query.impl;

import com.adobe.aem.guides.wknd.core.services.exception.QueryException;
import com.adobe.aem.guides.wknd.core.services.query.QueryService;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.SearchResult;
import lombok.extern.slf4j.Slf4j;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.query.Query;
import javax.jcr.query.QueryResult;
import java.util.Map;

import static java.util.Objects.isNull;

@Slf4j
@Component(service = QueryService.class, immediate = true)
public class QueryServiceImpl implements QueryService {

    @Reference
    private QueryBuilder queryBuilder;

    @Override
    public SearchResult getResult(Map<String, String> predicates, Session session) {
        if (isNull(predicates) || isNull(session)) {
            log.debug("QueryBuilder is null or session is null");
        }
        var query = queryBuilder.createQuery(PredicateGroup.create(predicates), session);
        return query.getResult();
    }

    @Override
    public QueryResult getResult(String sqlStatement, Session session) {
        try {
            var queryManager = session.getWorkspace().getQueryManager();
            var query = queryManager.createQuery(sqlStatement, Query.JCR_SQL2);
            return query.execute();
        } catch (RepositoryException e) {
            throw new QueryException(e.getMessage());
        }
    }
}

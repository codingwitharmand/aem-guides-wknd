package com.adobe.aem.guides.wknd.core.services.query;

import com.day.cq.search.result.SearchResult;

import javax.jcr.Session;
import javax.jcr.query.QueryResult;
import java.util.Map;

public interface QueryService {
    /**
     *
     * @param predicates a map defining search criteria
     * @param session user's session to perform search
     * @return SearchResult
     */
    SearchResult getResult(Map<String, String> predicates, Session session);

    /**
     *
     * @param sqlStatement JCR-SQL2 query as String
     * @param session user's session to perform search
     * @return QueryResult
     */
    QueryResult getResult(String sqlStatement, Session session);
}

package com.adobe.aem.guides.wknd.core.servlets;

import com.adobe.aem.guides.wknd.core.services.exception.GenericException;
import com.adobe.aem.guides.wknd.core.services.exception.QueryException;
import com.adobe.aem.guides.wknd.core.services.query.QueryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.Servlet;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

@Slf4j
@Component(service = Servlet.class)
@SlingServletPaths("/bin/wknd/templates")
public class TemplateServlet extends SlingSafeMethodsServlet {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Reference
    private transient QueryService queryService;

    @Override
    protected void doGet(SlingHttpServletRequest request, @NonNull SlingHttpServletResponse response) {
        final var sqlStatement = "SELECT * FROM [nt:base] AS node WHERE node.[jcr:title] LIKE '%Template%' AND ISDESCENDANTNODE(node, [/conf/wknd/])";

        var result = queryService.getResult(sqlStatement, request.getResourceResolver().adaptTo(Session.class));
        var responseBody = new ArrayList<Map<String, String>>();
        try {
            var nodes = result.getNodes();
            while (nodes.hasNext()) {
                var node = nodes.nextNode();
                var item = Map.of(
                        "template", node.getProperty("jcr:title").getString(),
                        "templateType", node.getProperty("cq:templateType").getString()
                );
                responseBody.add(item);
            }
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(MAPPER.writeValueAsString(responseBody));
        } catch (RepositoryException e) {
            throw new QueryException(e.getMessage());
        } catch (JsonProcessingException e) {
            throw new GenericException("Error parsing JSON", e);
        } catch (IOException e) {
            throw new GenericException("Error reading JSON", e);
        }
    }
}

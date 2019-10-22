package com.demo.modules.solr.service;

import com.demo.modules.solr.util.FacetConfig;
import com.demo.modules.solr.util.HighlightConfig;
import com.demo.modules.solr.util.SolrRsp;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.request.QueryRequest;
import org.apache.solr.client.solrj.request.UpdateRequest;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

/**
 * index service
 *
 * @author gc
 */
@Component
public class IndexService {

    private static final Logger LOG = LoggerFactory.getLogger(IndexService.class);

    private static final String PK_FIELD = "id";

    @Value("${solr.url}")
    private String solrUrl;
    @Value("${solr.user}")
    private String solrUser;
    @Value("${solr.password}")
    private String solrPassword;

    private static SolrClient solrClient = null;

    @PostConstruct
    private void init() {
        solrClient = new HttpSolrClient
                .Builder(solrUrl)
                .withConnectionTimeout(5000)
                .allowCompression(true)
                .build();
    }

    public SolrRsp query(String[] fields, String queryStr, FacetConfig facet, HighlightConfig hlc, Integer start, Integer rows, String[][] sorts) {
        if (solrClient == null || fields == null || fields.length == 0) {
            return null;
        }
        boolean hasHL = false;
        if (hlc != null && !queryStr.contains("*") && hlc.getFields() != null && hlc.getFields().length > 0) {
            hasHL = true;
        }
        boolean fac = false;
        if (facet != null && facet.getFields() != null && facet.getFields().length > 0) {
            fac = true;
        }
        SolrQuery query = new SolrQuery();
        for (String field : fields) {
            query.addField(field);
        }
        if (start != null) {
            query.setStart(start);
        }
        if (rows != null) {
            query.setRows(rows);
        }
        if (sorts != null) {
            for (String[] sort : sorts) {
                query.addSort(sort[0], "asc".equals(sort[1].toLowerCase()) ? SolrQuery.ORDER.asc : SolrQuery.ORDER.desc);
            }
        }
        if (hasHL) {
            query.setHighlight(true);
            query.setHighlightSimplePre(hlc.getHtmlStart());
            query.setHighlightSimplePost(hlc.getHtmlEnd());
            query.setHighlightSnippets(hlc.getSnippets());
            query.setHighlightFragsize(hlc.getFragsize());
            for (String field : hlc.getFields()) {
                query.addHighlightField(field);
            }
        }
        if (fac) {
            query.setFacet(true);
            query.setFacetLimit(facet.getFacetLimit());
            query.setFacetMinCount(facet.getFacetMinCount());
            for (int i = 0; i < facet.getFields().length; i++) {
                query.addFacetField(facet.getFields()[i]);
            }
        }
        queryStr = (queryStr == null || "".equals(queryStr.trim())) ? "*:*" : queryStr;
        query.setQuery(queryStr);

        QueryResponse rsp = null;
        try {
            QueryRequest req = new QueryRequest(query);
            req.setBasicAuthCredentials(solrUser, solrPassword);
            rsp = req.process(solrClient);
        } catch (Exception e) {
            LOG.error("", e);
            return null;
        }

        SolrDocumentList solrList = rsp.getResults();
        Map<String, Map<String, List<String>>> highlighting = null;
        if (hasHL) {
            highlighting = rsp.getHighlighting();
        }

        Map<String, List<FacetField.Count>> faceting = Maps.newHashMap();
        if (fac) {
            List<FacetField> limitingFacets = rsp.getLimitingFacets();
            for (FacetField facetField : limitingFacets) {
                String facetFieldName = facetField.getName();
                List<FacetField.Count> facCount = facetField.getValues();
                faceting.put(facetFieldName, facCount);
            }
        }

        List<Map<String, Object>> datas = Lists.newArrayListWithCapacity(solrList.size());
        for (SolrDocument doc : solrList) {
            String pkValue = doc.getFirstValue(PK_FIELD).toString();
            Map<String, List<String>> hlDoc = null;
            if (hasHL) {
                hlDoc = highlighting.get(pkValue);
            }

            Map<String, Object> keyValues = Maps.newHashMapWithExpectedSize(doc.getFieldNames().size());
            for (String field : doc.getFieldNames()) {
                String value = null;
                String listvalue = doc.get(field).toString();
                String[] list = listvalue.split(",");
                if (list.length > 1) {
                    if (hasHL && hlDoc != null && hlDoc.containsKey(field)) {
                        String hlvalue = hlDoc.get(field).toString();
                        value = hlvalue.substring(1, hlvalue.length());
                    }
                } else {
                    if (hasHL && hlDoc != null && hlDoc.containsKey(field)) {
                        value = hlDoc.get(field) != null ? hlDoc.get(field).get(0) : null;
                    }
                    if (value == null || value.equals("")) {
                        value = doc.getFirstValue(field).toString();
                    }
                }
                keyValues.put(field, value);
            }
            datas.add(keyValues);
        }
        SolrRsp solrRsp = new SolrRsp();
        solrRsp.setRows(datas);
        solrRsp.setTotal(((SolrDocumentList) rsp.getResponse().get("response")).getNumFound());
        solrRsp.setFacet(faceting);
        return solrRsp;
    }

    /**
     * delta/full Rebuilds Index
     *
     * @param delta delta/full
     */
    public void buildIndex(boolean delta) {
        SolrQuery query = new SolrQuery();
        // 指定RequestHandler，默认使用 /select
        query.setRequestHandler("/dataimport");

        String command = delta ? "delta-import" : "full-import";
        String clean = delta ? "false" : "true";
        String optimize = delta ? "false" : "true";

        query.setParam("command", command)
                .setParam("clean", clean)
                .setParam("commit", true)
                .setParam("entity", "article")
                .setParam("optimize", optimize)
                .setParam("wt", "json");
        try {

            QueryRequest req = new QueryRequest(query);
            req.setBasicAuthCredentials(solrUser, solrPassword);
            QueryResponse rsp = req.process(solrClient);

            LOG.info(rsp.toString());
        } catch (Exception e) {
            LOG.error("[build index failed]：", e);
        }
    }

    /**
     * Batch Updates/Adds indexing
     *
     * <p>
     * commit 动作在 solrconfig.xml 中配置，有 [soft & hard commit]
     *
     * <autoCommit>
     * <maxTime>15000</maxTime>
     * <openSearcher>false</openSearcher>
     * </autoCommit>
     * <autoSoftCommit>
     * <maxTime>2000</maxTime>
     * </autoSoftCommit>
     *
     * </p>
     *
     * @param records
     */
    public void saveOrUpdate(List<Map<String, Object>> records) {
        List<SolrInputDocument> docs = Lists.newArrayListWithCapacity(records.size());
        for (Map<String, Object> record : records) {
            docs.add(createSolrDoc(record));
        }
        try {
            UpdateRequest req = new UpdateRequest();
            req.setBasicAuthCredentials(solrUser, solrPassword);
            req.add(docs);
            req.process(solrClient);
        } catch (Exception e) {
            LOG.error("[add docs failed]：", e);
            try {
                solrClient.rollback();
            } catch (Exception e1) {
                LOG.error("", e);
            }
        }
    }

    /**
     * Updates/Adds indexing
     *
     * @param record
     */
    public void saveOrUpdate(Map<String, Object> record) {
        List<SolrInputDocument> docs = Lists.newArrayList();
        docs.add(createSolrDoc(record));
        try {
            UpdateRequest req = new UpdateRequest();
            req.setBasicAuthCredentials(solrUser, solrPassword);
            req.add(docs);
            UpdateResponse res = req.process(solrClient);

            LOG.info(res.toString());
        } catch (Exception e) {
            LOG.error("[add doc failed]：", e);
            try {
                solrClient.rollback();
            } catch (Exception e1) {
                LOG.error("", e);
            }
        }
    }

    /**
     * Build Solr Document
     *
     * @param record
     * @return SolrInputDocument
     */
    private SolrInputDocument createSolrDoc(Map<String, Object> record) {
        SolrInputDocument doc = new SolrInputDocument();
        for (String f : record.keySet()) {
            Object value = record.get(f);
            if (value == null) {
                doc.addField(f, "");
            } else {
                doc.addField(f, value);
            }
        }
        return doc;
    }

    /**
     * Removes document
     *
     * @param id
     */
    public void deleteById(String id) {
        UpdateRequest req = new UpdateRequest();
        req.setBasicAuthCredentials(solrUser, solrPassword);
        req.deleteById(id);
        try {
            UpdateResponse res = req.process(solrClient);

            LOG.info(res.toString());
        } catch (Exception e) {
            LOG.error("[delete doc failed]：", e);
        }
    }

}

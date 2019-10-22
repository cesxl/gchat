package com.demo.modules.solr.util;

import org.apache.solr.client.solrj.response.FacetField;

import java.util.List;
import java.util.Map;

/**
 * solr resultset
 *
 * @author gc
 */
public class SolrRsp {

    private List<?> rows;

    private long total;

    private Integer pageNumber;

    private Integer pageSize;

    private Map<String, List<FacetField.Count>> facet;

    public List<?> getRows() {
        return rows;
    }

    public void setRows(List<?> rows) {
        this.rows = rows;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Map<String, List<FacetField.Count>> getFacet() {
        return facet;
    }

    public void setFacet(Map<String, List<FacetField.Count>> facet) {
        this.facet = facet;
    }
}

package com.demo.modules.solr.util;

/**
 * facet serach
 *
 * @author gc
 */
public class FacetConfig {

    private String[] fields;
    private int facetThreads = 2;
    private int facetMinCount = 2;
    private int facetLimit = 120;

    public String[] getFields() {
        return fields;
    }

    public void setFields(String[] fields) {
        this.fields = fields;
    }

    public int getFacetMinCount() {
        return facetMinCount;
    }

    public void setFacetMinCount(int facetMinCount) {
        this.facetMinCount = facetMinCount;
    }

    public int getFacetLimit() {
        return facetLimit;
    }

    public void setFacetLimit(int facetLimit) {
        this.facetLimit = facetLimit;
    }

    public int getFacetThreads() {
        return facetThreads;
    }

    public void setFacetThreads(int facetThreads) {
        this.facetThreads = facetThreads;
    }
}

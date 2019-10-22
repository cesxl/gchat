package com.demo.modules.solr.util;

/**
 * 字体高亮
 *
 * @author gc
 */
public class HighlightConfig {

	//高亮字段
	private String[] fields;
    private String htmlStart ="<em>";
    private String htmlEnd = "</em>";

    //第一段
    private Integer snippets=20;
    //显示字数
    private Integer fragsize=50000;

	public String[] getFields() {
		return fields;
	}

	public void setFields(String[] fields) {
		this.fields = fields;
	}

	public String getHtmlStart() {
		return htmlStart;
	}

	public void setHtmlStart(String htmlStart) {
		this.htmlStart = htmlStart;
	}

	public String getHtmlEnd() {
		return htmlEnd;
	}

	public void setHtmlEnd(String htmlEnd) {
		this.htmlEnd = htmlEnd;
	}

	public Integer getSnippets() {
		return snippets;
	}

	public void setSnippets(Integer snippets) {
		this.snippets = snippets;
	}

	public Integer getFragsize() {
		return fragsize;
	}

	public void setFragsize(Integer fragsize) {
		this.fragsize = fragsize;
	}

}

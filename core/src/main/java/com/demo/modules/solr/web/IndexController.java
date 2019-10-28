package com.demo.modules.solr.web;

import com.alibaba.fastjson.JSON;
import com.demo.modules.solr.service.IndexService;
import com.demo.modules.solr.util.FacetConfig;
import com.demo.modules.solr.util.HighlightConfig;
import com.demo.modules.solr.util.SolrRsp;
import jodd.http.HttpRequest;
import jodd.http.HttpResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.Map;

/**
 * index controller
 *
 * @author gc
 */
@Controller
@RequestMapping(value = "/search")
public class IndexController {

    private static final Logger LOG = LoggerFactory.getLogger(IndexController.class);

    @Autowired
    private IndexService indexService;

    @Value("${solr.url}")
    private String solrUrl;
    @Value("${solr.user}")
    private String solrUser;
    @Value("${solr.password}")
    private String solrPasswd;

    /**
     * standard query parser
     *
     * @param mv
     * @return
     * @see <a href="https://lucene.apache.org/solr/guide/8_1/the-standard-query-parser.html">query</a>
     */
    @GetMapping(value = "")
    public ModelAndView solr(
            @RequestParam(defaultValue = "1", value = "curr") int curr,
            @RequestParam(defaultValue = "100", value = "limit") int limit,
            String q, ModelAndView mv) {
        if (StringUtils.isEmpty(q)) {
            q = "*";
        }
        String queryStr = "article:" + q;
        String[] queryFields = {"id", "authorName", "publishDate", "language", "siteName", "title", "url", "mobileUrl", "score"};
        String[][] sorts = new String[][]{{"score", "desc"}, {"publishDate", "desc"}};
        HighlightConfig hlc = new HighlightConfig();
        String[] hlcFields = {"title", "authorName"};
        hlc.setFields(hlcFields);

        FacetConfig facet = new FacetConfig();
        String[] facetFields = {"siteName", "authorName"};
        facet.setFields(facetFields);

        int start = (curr - 1) * limit;
        SolrRsp solrRsp = indexService.query(queryFields, queryStr, facet, hlc, start, limit, sorts);
        solrRsp.setPageNumber(curr);

        mv.addObject("q", q.equals("*") ? "" : q);
        mv.addObject("pv", solrRsp);
        mv.setViewName("solr/index");
        return mv;
    }

    /**
     * Solr 智能提醒
     *
     * @param q          查询值
     * @param dictionary 字典名称
     * @return
     * @throws IOException
     * @see <a href="https://lucene.apache.org/solr/guide/8_1/suggester.html">suggester</a>
     */
    @GetMapping(value = "/suggest")
    @ResponseBody
    public Map suggest(String q, String dictionary) throws IOException {
        String apiPath = "/suggest";

        final HttpResponse response = HttpRequest.get(solrUrl + apiPath)
                .query("suggest", "true")
                .query("suggest.build", "false")
                .query("suggest.count", "5")
                .query("suggest.dictionary", dictionary)
                .query("suggest.q", q)
                .header("User-Agent", "Solr[" + IndexController.class.getName() + "] 1.0")
                //.header("Authorization", "Basic " + basicAuth())
                .basicAuthentication(solrUser, solrPasswd)
                .contentTypeJson().timeout(5000).send();
        response.charset("UTF-8");
        return JSON.parseObject(response.bodyText(), Map.class);
    }

}

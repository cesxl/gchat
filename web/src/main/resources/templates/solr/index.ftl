<#include "../include/global.ftl">
<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <title></title>
    <meta name="viewport" content="width=device-width,initial-scale=1,user-scalable=0">
    <link rel="stylesheet" href="${ctxStatic}/weui/css/weui.css"/>
    <link rel="stylesheet" href="${ctxStatic}/weui/css/weuix.css"/>

    <style>
        a {
            color: #212121
        }
        em {
            color: #DF0037;
        }
        #solr {
            background-image: url(${ctxStatic}/weui/images/solr.svg);
            background-repeat: no-repeat;
            background-size: 75px;
            display: block;
            height: 40px;
            width: 150px;
        }
        .page-hd {
            padding: 10px 20px;
        }
    </style>
    <script src="${ctxStatic}/weui/js/zepto.min.js"></script>
    <script src="${ctxStatic}/weui/js/zepto.weui.min.js"></script>
    <script src="${ctxStatic}/weui/js/iscroll-lite.min.js"></script>
    <script>
        $(function(){
            $('.weui-tab').tab({
                defaultIndex: 0,
                activeClass: 'weui-bar__item_on',
                onToggle: function (index) {
                    if (index == 0) {
                        //location.href = "${ctx}/search";
                    }
                    console.log(index)
                }
            })

            TagNav('#tagnav',{
                type: 'scrollToNext',
                curClassName: 'weui-state-active',
                index:0
            });
        })

        function suggest() {
            $.ajax({
                type: 'GET',
                url: '${ctx}/search/suggest',
                data: {
                    "dictionary": "titleSuggester",
                    "q": 'jav'//$("#searchInput").val()
                },
                async: false,
                dataType: "json",
                success: function (data) {
                    var term = $("#searchInput").val().replace(/\"/g, "");
                    var suggestTerm = data.suggest.titleSuggester.jav.suggestions;
                    console.log(suggestTerm)
                    $("#suggest").empty();
                    for (var i = 0; i < suggestTerm.length; i++) {
                        $("#suggest").append("<li>" + suggestTerm[i].term + "</li>")
                    }
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                    toastr.error('suggest Error');
                },
            });
        }

    </script>
</head>

<body ontouchstart class="page-bg">
<div class="page-hd">
    <h1 class="page-hd-title">
        <a href="${ctx}/search?q=*" id="solr"></a>
    </h1>
    <p class="page-hd-desc">全文检索功能演示 检索总数：${pv.total}</p>
    <p class="page-hd-desc">中文分词采用 <a target="_blank" href="http://hanlp.com/">HanLP</a></p>
</div>

<div id="tagnav" class="weui-navigator weui-navigator-wrapper">
    <ul class="weui-navigator-list">
        <li><a href="${ctx}/search?q=*">首页</a></li>
        <#assign maps = pv.facet>
        <#list maps?keys as mKey>
            <#assign items = maps[mKey]>
            <#if mKey == 'siteName'>
                <#list items as item>
                    <li>
                        <a href="${ctx}/search?q=* AND ${mKey}:${item.name?replace(" ","%5C%20")}"><span class="layui-badge layui-bg-cyan">${item.name}</span></a>
                    </li>
                </#list>
            </#if>
        </#list>
    </ul>
</div>

<div class="weui-tab" style="height:auto;">

    <div class="weui-search-bar" id="searchBar">
        <form class="weui-search-bar__form" action="${ctx}/search?q=">
            <div class="weui-search-bar__box">
                <i class="weui-icon-search"></i>
                <input class="weui-search-bar__input" autocomplete="false" id="searchInput" οnkeyup="suggest()" placeholder="搜索" name="q" type="search" />
                <a href="javascript:" class="weui-icon-clear" id="searchClear"></a>
            </div>
            <label class="weui-search-bar__label" id="searchText" style="transform-origin: 0px 0px 0px; opacity: 1; transform: scale(1, 1);">
                <i class="weui-icon-search"></i>
                <span>搜索</span>
            </label>
        </form>
        <a href="javascript:" class="weui-search-bar__cancel-btn" id="searchCancel">取消</a>
    </div>

    <div class="weui-news">
        <ul class="weui-news-list">
            <#list pv.rows as item>
                <#if item.title??>
                <li class="weui-news-item">
                    <div class="weui-news-inner">
                        <div class="weui-news-inners">
                            <div class="weui-news-text">
                                <div class="weui-news-title"><a target="_blank" href="${item.url!}">${item.title!}</a></div>
                            </div>
                            <div class="weui-news-info">
                                <div class="weui-news-infoitem">
                                    <span class="weui-news-left">
                                        <a href="${ctx}/search?q=* AND siteName:${(item.siteName?replace(" ","%5C%20"))!}">
                                            <label class="weui-label-s b-blue f-orange">${item.siteName!}</label></a>
                                            /
                                        <a href="${ctx}/search?q=* AND authorName:${(item.authorName?replace(" ","%5C%20"))!}">${item.authorName!}</a>
                                    </span>
                                </div>
                                <div class="weui-news-infoitem">${item.publishDate!}</div>
                            </div>
                        </div>
                    </div>
                </li>
                </#if>
            </#list>
        </ul>
    </div>

    <div class="weui-tabbar tab-bottom">
        <a href="${ctx}/search?q=*" class="weui-tabbar__item">
            <span style="display: inline-block;position: relative;">
                <i class="icon icon-27 f27 weui-tabbar__icon"></i>
            </span>
            <p class="weui-tabbar__label">Solr</p>
        </a>
    </div>
</div>

</body>
</html>
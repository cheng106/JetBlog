package com.cheng.jetblog.utils;

import org.commonmark.Extension;
import org.commonmark.ext.gfm.tables.TableBlock;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.ext.heading.anchor.HeadingAnchorExtension;
import org.commonmark.node.Link;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.AttributeProvider;
import org.commonmark.renderer.html.HtmlRenderer;

import java.util.*;

/**
 * @author cheng
 * @since 2021/9/5 14:10
 **/
public class MarkdownUtils {

    /**
     * Markdown格式轉換HTML
     *
     * @param markdown 存文字格式
     * @return java.lang.String
     **/
    public static String markdownToHtml(String markdown) {
        Parser parser = Parser.builder().build();
        Node document = parser.parse(markdown);
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        return renderer.render(document);
    }

    /**
     * 新增擴充，標題的錨點，產生表格
     * Markdown格式轉換HTML
     *
     * @param markdown 存文字格式
     * @return java.lang.String
     **/
    public static String markdownToHtmlExtensions(String markdown) {
        // H標籤的標題產生ID
        Set<Extension> headingAnchorExtensions = Collections.singleton(HeadingAnchorExtension.create());
        // 轉換Table的HTML
        List<Extension> tableExtension = Collections.singletonList(TablesExtension.create());
        Parser parser = Parser.builder().extensions(tableExtension).build();
        Node document = parser.parse(markdown);
        HtmlRenderer renderer = HtmlRenderer.builder()
                .extensions(headingAnchorExtensions)
                .extensions(tableExtension)
                .attributeProviderFactory(attributeProviderContext -> new CustomAttributeProvider()).build();
        return renderer.render(document);
    }

    /**
     * 處理標籤的屬性
     */
    private static class CustomAttributeProvider implements AttributeProvider {
        @Override
        public void setAttributes(Node node, String tagName, Map<String, String> attributes) {
            // 改變a標籤的target屬性為_blank
            if (node instanceof Link) {
                attributes.put("target", "_blank");
            }
            if (node instanceof TableBlock) {
                attributes.put("class", "ui celled table");
            }
        }
    }

}

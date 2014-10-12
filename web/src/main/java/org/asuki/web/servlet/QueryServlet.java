package org.asuki.web.servlet;

import static java.util.Arrays.asList;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.AllArgsConstructor;
import lombok.ToString;

import org.apache.lucene.search.Query;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.infinispan.Cache;
import org.infinispan.configuration.cache.Configuration;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.query.CacheQuery;
import org.infinispan.query.Search;
import org.infinispan.query.SearchManager;

import org.slf4j.Logger;

@WebServlet("/query")
public class QueryServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Inject
    private Logger log;

    private Cache<String, Book> cache;

    public QueryServlet() {
        // @formatter:off
        Configuration infinispanConfiguration = new ConfigurationBuilder()
            .indexing()
                .enable()
                .indexLocalOnly(true)
            .build();
        // @formatter:on

        DefaultCacheManager cacheManager = new DefaultCacheManager(
                infinispanConfiguration);

        cache = cacheManager.getCache();
    }

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {

        cache.put("book1", new Book("earth", "author"));
        cache.put("book2", new Book("the earth", "author"));
        cache.put("book3", new Book("earth is peace", "author"));

        SearchManager sm = Search.getSearchManager(cache);
        QueryBuilder qb = sm.buildQueryBuilderForClass(Book.class).get();

        List<Query> luceneQueries = asList(queryByKeyword(qb),
                queryByPhrase(qb));

        for (Query luceneQuery : luceneQueries) {
            CacheQuery query = sm.getQuery(luceneQuery, Book.class);
            List<Object> list = query.list();

            log.info(list.toString());
        }

    }

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    private Query queryByKeyword(QueryBuilder qb) {
        // @formatter:off
        return qb
            .keyword()
                .wildcard()
            .onField("title")
            .matching("*peace")
            .createQuery();
        // @formatter:on
    }

    private Query queryByPhrase(QueryBuilder qb) {
        // @formatter:off
        return qb
            .phrase()
            .onField("title")
            .sentence("the earth")
            .createQuery();
        // @formatter:on
    }

    @Indexed
    @ToString
    @AllArgsConstructor
    public static class Book implements Serializable {

        private static final long serialVersionUID = 1L;

        @Field
        String title;

        @Field
        String author;

    }
}

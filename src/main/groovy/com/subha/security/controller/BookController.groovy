package com.subha.security.controller



import com.subha.security.entities.elastic.Author
import com.subha.security.entities.elastic.Book
import com.subha.security.entities.elastic.Doctor
import com.subha.security.entities.elastic.Hospital
import com.subha.security.service.BookService
import groovy.json.JsonBuilder
import org.apache.commons.logging.LogFactory
import org.elasticsearch.client.transport.NoNodeAvailableException
import org.elasticsearch.common.xcontent.XContentFactory
import org.springframework.data.domain.PageRequest
import org.springframework.data.elasticsearch.core.query.Criteria
import org.springframework.data.elasticsearch.core.query.CriteriaQuery
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder

import static org.elasticsearch.index.query.QueryBuilders.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder
import org.springframework.data.elasticsearch.core.query.StringQuery
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


import javax.servlet.http.HttpServletRequest

/**
 * Created by user on 9/12/2016.
 */
@RestController
@RequestMapping("/elasticbook")
class BookController {

    def logger = LogFactory.getLog(BookController);

    @Autowired
    BookService bookService

    @Autowired
    ElasticsearchTemplate elasticsearchTemplate

    @RequestMapping(value="/add",method = RequestMethod.POST)
    def addBook(@RequestBody Book book){
        logger.info "******  Adding Book $book in ELASTIC DB......"
        //bookService.addBook(book)
        def indexQuery = new IndexQueryBuilder().withObject(book).withIndexName("book").withType("funcprog").build();
        def result = elasticsearchTemplate.index(indexQuery)
        logger.info "****** Result is: $result "
        result
    }

    @RequestMapping(value="/addHospDoc",method = RequestMethod.POST)
    def addHospDoc(@RequestBody Hospital hospital){
        logger.info "******  Adding Hospital $hospital in ELASTIC DB......"
        def indexQuery = new IndexQueryBuilder().withId(hospital.id).withObject(hospital)
                            .withIndexName("parchild").withType("hospital").build();
        def result = elasticsearchTemplate.index(indexQuery)
        logger.info "****** Result (Hospital) is: $result "

        Doctor doctor = new Doctor(id:"child1${hospital.id}",parentId:hospital.id,docname:"Docname1",trade:"trade1")
         indexQuery = new IndexQueryBuilder().withId("child1${hospital.id}").withObject(doctor).withParentId(hospital.id)
                .withIndexName("parchild").withType("doctor").build();
        result = elasticsearchTemplate.index(indexQuery)
        logger.info "****** Result (Doctor) is: $result "

        doctor = new Doctor(id:"child2${hospital.id}",parentId:hospital.id,docname:"Docname11",trade:"trade11")
        indexQuery = new IndexQueryBuilder().withId("child2${hospital.id}").withObject(doctor).withParentId(hospital.id)
                .withIndexName("parchild").withType("doctor").build();

         result = elasticsearchTemplate.index(indexQuery)
        logger.info "****** Result (Doctor) is: $result "
        new JsonBuilder(result).toPrettyString()
    }

    @RequestMapping(value="/addtemp",method = RequestMethod.POST)
    def addBookTemp(@RequestBody Book book){
        logger.info "******  Adding Book $book in ELASTIC DB......"
        //bookService.addBook(book)
        def result = bookService.addBook(book)
        logger.info "****** Result is: $result "
        result
    }



    @RequestMapping(value="/retrieve",method = RequestMethod.GET)
    def retrieveBookByName(@RequestParam(name = "name")String name) {
        logger.info "Searching with Name: $name"

        def query = XContentFactory.jsonBuilder().startObject().startObject("query")
                .startObject("match_all")
                .endObject()
                .endObject()
                .endObject()



        /*StringQuery stringQuery = new StringQuery(query.string())
        stringQuery.addIndices("book")
        stringQuery.addTypes("funcprog")
        stringQuery.setPageable(new PageRequest(0,3))
        stringQuery.addFields("name")*/

        /**
            Both the StringQuery and NativeQuery (Hurrah!!! Nested Too) is Now working....
         */


        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        //def nativeQuery = nativeSearchQueryBuilder.withIndices("book").withTypes("funcprog").withQuery(matchAllQuery()).build()

        def matchQueryBuilder = matchQuery("author.name","gordon3")
        def boolQueryBuilder = boolQuery().must(matchQueryBuilder)

        def matchQueryBuilder2 = termQuery("name","action")
        def boolQueryBuilder2 = boolQuery().must(matchQueryBuilder2)

        def temQueryBuilder = termQuery("name","222")
        def boolQueryBuilder3 = boolQuery().must(temQueryBuilder)

        def nativeSearchQuery = nativeSearchQueryBuilder.withIndices("book").withTypes("funcprog").
                withQuery(boolQuery().must(nestedQuery("author",boolQueryBuilder)).must(boolQueryBuilder2))
                /*withQuery(temQueryBuilder)*/
                /*withQuery(nestedQuery("author",boolQueryBuilder2))*/
                .build()
        /**
         * Criteria Queria do not support nested querying...
         */



        /*Criteria criteria = new Criteria("name")
        criteria.contains("007").startsWith("Groovy2")
        *//*Criteria criteria2 = new Criteria("author")
        criteria2.and("author.name").startsWith("Godon2")
        criteria.and(criteria2)*//*
        CriteriaQuery criteriaQuery = new CriteriaQuery(criteria)
        criteriaQuery.addIndices("book")
        criteriaQuery.addTypes("funcprog")
        //criteriaQuery.addFields("id")*/


        def result = elasticsearchTemplate.queryForList(nativeSearchQuery,Book)//elasticsearchTemplate.queryForList(criteriaQuery,Book)/*elasticsearchTemplate.queryForPage(stringQuery,Book)*///elasticsearchTemplate.queryForList(stringQuery,Book)//elasticsearchTemplate.queryForObject(stringQuery,Book)/**/
        logger.info "@@@@@@@@ The Result is: $result "
        result
    }


    @RequestMapping(value="/retrieveParChild",method = RequestMethod.GET)
    def retrieveParChild() {
        def matchQueryBuilder = matchQuery("hospname","CMC33")
        //def boolQueryBuilder2 = boolQuery().must(matchQueryBuilder)
        def hasParentQueryBuilder = hasParentQuery("hospital",matchQueryBuilder)

        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();

        def nativeSearchQuery = nativeSearchQueryBuilder.withIndices("parchild").withTypes("hospital","doctor")
                    .withQuery(hasParentQueryBuilder).build()

        def result = elasticsearchTemplate.queryForList(nativeSearchQuery,Doctor)
        logger.info "### The Result is: $result "
        result

    }

    @RequestMapping(value="/addIndex",method = RequestMethod.POST)
    def addBookIndex(@RequestParam(name = "indexName")String indexName){
        logger.info "******  Adding Book  Index: $indexName in ELASTIC DB......"
        def result = elasticsearchTemplate.createIndex(indexName)
        logger.info "****** Result is: $result "
        result
    }

    @RequestMapping(value="/deleteIndex",method = RequestMethod.POST)
    def deleteBookIndex(@RequestParam(name = "indexName")String indexName){
        logger.info "******  Deleting Book  Index: $indexName in ELASTIC DB......"
        def result = elasticsearchTemplate.deleteIndex(indexName)
        logger.info "****** Result is: $result "
        result
    }
    @RequestMapping(value="/addMapping",method = RequestMethod.POST)
    def addBookMapping(){
        logger.info "******  Adding Book Mapping in ELASTIC DB......"
        def data = XContentFactory.jsonBuilder().startObject()/*.startObject("book")*/
               /* .startObject("mappings")*/
                .startObject("funcprog")
                .startObject("properties")
                .startObject("author").field("type","nested")
                .startObject("properties")
                .startObject("name")
                .field("type","String").endObject().endObject().endObject()
                .startObject("id").field("type","Long").endObject()
                .startObject("name").field("type","String").endObject()
                .endObject()
                .endObject()
                /*.endObject()*/
                /*.endObject()*/
                .endObject()

        def result = elasticsearchTemplate.putMapping("book","funcprog",data)
        logger.info "****** Result is: $result "
        result
    }

    @RequestMapping(value="/addMapping2",method = RequestMethod.POST)
    def addHospitalMapping(){
        logger.info "******  Adding Book Mapping in ELASTIC DB......"
        def data = XContentFactory.jsonBuilder().startObject()


                .startObject("doctor")

                .startObject("_parent")
                .field("type","hospital")
                .endObject()



                .startObject("properties")

                .startObject("id")
                .field("type","String")
                .endObject()

                .startObject("parentId")
                .field("type","String")
                .endObject()

                .startObject("docname")
                .field("type","String")
                .endObject()

                .startObject("trade")
                .field("type","String")
                .endObject()

                .endObject()
                .endObject()

                .endObject()



                def result = elasticsearchTemplate.putMapping("parchild","doctor",data)
        logger.info "****** First Result mapping is: $result "

        data = XContentFactory.jsonBuilder().startObject()/*.startObject("book")*/
        /* .startObject("mappings")*/
                .startObject("hospital")
                .startObject("properties")

                .startObject("id")
                .field("type","String")
                .endObject()

                .startObject("hospname")
                .field("type","String")
                .endObject()

                .startObject("location")
                .field("type","String")
                .endObject()

                .endObject()
                .endObject()



                .endObject()




            result = elasticsearchTemplate.putMapping("parchild","hospital",data)
        logger.info "****** Second Result Mapping is: $result "
        result
    }


    /**
     * Local Exception Handling
     */

    @ExceptionHandler(NoNodeAvailableException.class)
    def handleNoNodeAvailableError(HttpServletRequest req, Exception ex) {
        logger.error("**********  Request: " + req.getRequestURL() + " raised " + ex);
        logger.error ex
        throw new Exception(ex.getMessage())
    }

    @ExceptionHandler(Exception.class)
    def handleGenericError(HttpServletRequest req, Exception ex) {
        logger.error("Generic **********  Request: " + req.getRequestURL() + " raised " + ex);
        logger.error ex
        throw new Exception(ex.getMessage())
    }

}

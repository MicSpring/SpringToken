package com.subha.security.controller

import com.subha.security.entities.elastic.Author
import com.subha.security.entities.elastic.Book
import com.subha.security.service.BookService
import org.apache.commons.logging.LogFactory
import org.elasticsearch.client.transport.NoNodeAvailableException
import org.elasticsearch.common.xcontent.XContentFactory
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

    @RequestMapping(value="/retrieve",method = RequestMethod.GET)
    def retrieveBookByName(@RequestParam(name = "name")String name) {
        logger.info "Searching with Name: $name"
        bookService.getByName(name)
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


    /**
     * Local Exception Handling
     */

    @ExceptionHandler(NoNodeAvailableException.class)
    def handleNoNodeAvailableError(HttpServletRequest req, Exception ex) {
        logger.error("**********  Request: " + req.getRequestURL() + " raised " + ex);
        throw new Exception(ex.getMessage())
    }

    @ExceptionHandler(Exception.class)
    def handleGenericError(HttpServletRequest req, Exception ex) {
        logger.error("Generic **********  Request: " + req.getRequestURL() + " raised " + ex);
        throw new Exception(ex.getMessage())
    }

}

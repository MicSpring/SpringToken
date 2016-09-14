package com.subha.security.controller

import com.subha.security.entities.elastic.Author
import com.subha.security.entities.elastic.Book
import com.subha.security.service.BookService
import org.apache.commons.logging.LogFactory
import org.elasticsearch.client.transport.NoNodeAvailableException
import org.springframework.beans.factory.annotation.Autowired
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

    @RequestMapping(value="/add",method = RequestMethod.POST)
    def addBook(@RequestBody Book book){
        logger.info "******  Adding Book $book in ELASTIC DB......"
        bookService.addBook(book)
    }

    @RequestMapping(value="/retrieve",method = RequestMethod.GET)
    def retrieveBookByName(@RequestParam(name = "name")String name) {
        logger.info "Searching with Name: $name"
        bookService.getByName(name)
    }

    /**
     * Local Exception Handling
     */

    @ExceptionHandler(NoNodeAvailableException.class)
    def handleError(HttpServletRequest req, Exception ex) {
        logger.error("**********  Request: " + req.getRequestURL() + " raised " + ex);
        throw new Exception(ex.getMessage())
    }

}

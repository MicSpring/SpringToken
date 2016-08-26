package com.subha.security.controller

import com.subha.security.utils.Book
import groovy.util.logging.Slf4j
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

/**
 * Created by user on 8/25/2016.
 */

@Slf4j
@RestController
@RequestMapping("/resource")
class ResourceController {

    @RequestMapping( value = "/book", method = RequestMethod.GET)
    ResponseEntity<?> getBook() {
        ResponseEntity.ok(new Book(title:'Groovy In Action', author:'Gordon'))
    }
}

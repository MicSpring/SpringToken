package com.subha.security.service

import com.subha.security.entities.elastic.Book
import com.subha.security.repo.BookRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * Created by user on 9/12/2016.
 */
@Service
class BookService {

    @Autowired
    BookRepository bookRepository

     List < Book >  getByName(String name) {
         bookRepository.findByName(name)
    }

    def addBook(Book book){
        bookRepository.save(book)
    }

}

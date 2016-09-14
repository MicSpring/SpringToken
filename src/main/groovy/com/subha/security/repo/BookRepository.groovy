package com.subha.security.repo

import com.subha.security.entities.elastic.Book
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository

/**
 * Created by user on 9/12/2016.
 */
interface BookRepository extends ElasticsearchRepository<Book, String>{
    List<Book> findByName(String name)
}
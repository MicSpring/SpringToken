package com.subha.security.entities.elastic

import groovy.transform.ToString
import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.Document
import org.springframework.data.elasticsearch.annotations.Field
import org.springframework.data.elasticsearch.annotations.FieldType


/**
 * Created by user on 9/12/2016.
 */
@ToString
@Document(indexName = "bookindex", type = 'book')
class Book {

    @Id
    String id

    String name

    @Field(type = FieldType.Nested)
    Author author

}

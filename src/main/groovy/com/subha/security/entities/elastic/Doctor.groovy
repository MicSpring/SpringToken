package com.subha.security.entities.elastic

import groovy.transform.ToString
import org.springframework.data.elasticsearch.annotations.Document

/**
 * Created by user on 10/7/2016.
 */
@ToString
@Document(indexName = "parchild")
class Doctor {
     String id
     String parentId
     String docname
     String trade
}

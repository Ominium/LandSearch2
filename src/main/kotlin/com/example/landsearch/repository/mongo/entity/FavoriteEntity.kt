package com.example.landsearch.repository.mongo.entity

import com.querydsl.core.annotations.QueryEntity
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.mapping.Document
import javax.persistence.*


@Entity
@Document(collection = "FavoriteCollection")
class FavoriteEntity(

) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "_id")
    var _id : ObjectId?= null

    @Column(name="user_id")
    var userId : String?=null


    var favorite: List<String> = ArrayList()

    var consult: List<String> = ArrayList()
}
package com.example.landsearch.dto

import lombok.Data
import org.bson.types.ObjectId

@Data
class FavoriteDTO {

     var _id : ObjectId?= null

     var userId: String? = null

     var favorite: List<String> = ArrayList()

     var consult: List<String> = ArrayList()
}
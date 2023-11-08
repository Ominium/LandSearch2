package com.example.landsearch.repository.mongo

import com.example.landsearch.repository.mongo.entity.FavoriteEntity
import com.example.landsearch.repository.mongo.entity.UserEntity

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface FavoriteRepository : MongoRepository<FavoriteEntity, String> {
    fun findFirstByUserId(userId: String) : Optional<FavoriteEntity>?
    fun deleteAllByUserId(userId : String)
}
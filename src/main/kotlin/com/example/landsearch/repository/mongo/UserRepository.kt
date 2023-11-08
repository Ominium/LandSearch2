package com.example.landsearch.repository.mongo

import com.example.landsearch.repository.mongo.entity.UserEntity
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface UserRepository : MongoRepository<UserEntity,String> {
    fun findByEmail(userId : String): Optional<UserEntity>?

    fun findByUserId(userId: String) : Optional<UserEntity>?
    fun deleteByUserId(userId: String): Optional<UserEntity>?

    fun findByUserIdAndPassword(userId: String,password: String) : Optional<UserEntity>

    fun findByUserIdAndEmail(userId: String,email: String) : Optional<UserEntity>


}
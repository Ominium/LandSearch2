package com.example.landsearch.service

import com.example.landsearch.dto.UserDTO
import com.example.landsearch.repository.mongo.entity.UserEntity
import java.util.*

interface IUserService {
    fun findUserId(pDTO: UserDTO?): UserDTO?
    fun deleteUserId(pDTO: UserDTO?)
    fun insertUserInfo(pDTO: UserDTO?) : Int
    fun findPassword(pDTO: UserDTO) : Int

    fun findEmail(pDTO: UserDTO?) : Boolean?
    fun getUserLoginCheck(pDTO: UserDTO?): Optional<UserEntity>?
}
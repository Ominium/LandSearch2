package com.example.landsearch.service.impl

import com.example.landsearch.dto.FavoriteDTO
import com.example.landsearch.dto.MailDTO
import com.example.landsearch.dto.NoticeDTO
import com.example.landsearch.dto.UserDTO
import com.example.landsearch.repository.maria.NoticeRepository
import com.example.landsearch.repository.mongo.FavoriteRepository
import com.example.landsearch.repository.mongo.UserRepository
import com.example.landsearch.repository.mongo.entity.FavoriteEntity
import com.example.landsearch.repository.mongo.entity.UserEntity
import com.example.landsearch.service.IMyPageService
import com.example.landsearch.util.EncryptUtil
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.convertValue
import lombok.RequiredArgsConstructor
import lombok.extern.slf4j.Slf4j
import org.springframework.stereotype.Service
import java.util.*
import javax.transaction.Transactional
import kotlin.collections.ArrayList

@Slf4j
@RequiredArgsConstructor
@Service("MyPageService")
class MyPageService(
    private val favoriteRepository: FavoriteRepository? = null,
    private val userRepository: UserRepository? = null,
    private val noticeRepository: NoticeRepository?=null
) : IMyPageService {

    @Throws(Exception::class)
    override fun getMyList(userId :String) : List<NoticeDTO>?{


        return noticeRepository?.let { ObjectMapper().convertValue(it.findAllByUserId(userId)) }
    }
    @Throws(Exception::class)
    @Transactional
    override fun deleteNotice(userId: String,noticeSeq:String) : Int{
        noticeRepository?.deleteByUserIdAndNoticeSeq(userId,noticeSeq.toLong())
        return 1;
    }
    @Throws(Exception::class)
    override fun newPassword(pDTO: UserDTO, newPW: String?): Int {
        val uDTO: Optional<UserEntity>? = pDTO.userId?.let { pDTO.password?.let { it1 ->
            userRepository?.findByUserIdAndPassword(it,
                it1
            )
        } }
        var res : Int = 0
        if (uDTO?.isPresent == true) {
            val userEntity = UserEntity()
            userEntity.id = uDTO.get().id
            userEntity.password = newPW
            userEntity.email = uDTO.get().email
            userEntity.userId = uDTO.get().userId
            userRepository?.save(userEntity)
            res = 1

        }
        return res
    }
    @Throws(Exception::class)
    override fun myPage(pDTO: UserDTO?): UserDTO? {
        println("tq")
        val userId = (pDTO?.userId)
        val userEntity: Optional<UserEntity>? = userId?.let { userRepository?.findByUserId(it) }
        val userDTO = UserDTO()
        userDTO.userId = userEntity?.get()?.userId
        userDTO.password = userEntity?.get()?.password
        userDTO.email = EncryptUtil.decAES128CBC(userEntity?.get()?.email)
        userDTO.userName=userEntity?.get()?.userName

        return userDTO
    }

    @Throws(Exception::class)

    override fun getFav(user_id: String): FavoriteDTO? {
        println("tq2")
        val fEntity :Optional<FavoriteEntity>?
        println("tq3")
        val fDTO =  FavoriteDTO()
        if(favoriteRepository?.findFirstByUserId(user_id)?.isPresent == false){

            val favoriteEntity = FavoriteEntity()
            favoriteEntity.userId=user_id
            favoriteRepository.save(favoriteEntity)
           fEntity = favoriteRepository.findFirstByUserId(user_id)
            println("슈발")
            println(fEntity?.get()?.userId)
            fDTO._id = fEntity?.get()?._id
            fDTO.userId = fEntity?.get()?.userId
            fDTO.favorite = fEntity?.get()?.favorite!!
        }else{
            fEntity =favoriteRepository?.findFirstByUserId(user_id)
            println("슈발2")
            println(fEntity?.get()?.userId)
            fDTO._id = fEntity?.get()?._id
            fDTO.userId = fEntity?.get()?.userId
            fDTO.favorite = fEntity?.get()?.favorite!!
        }
        return fDTO
    }

    @Throws(Exception::class)
    override fun insertFav(fDTO: FavoriteDTO?, favorite: String?): Int {
        var res : Int = 0
        var fEntity: Optional<FavoriteEntity>? = fDTO?.userId?.let { favoriteRepository?.findFirstByUserId(it) }

        if(fEntity?.isPresent==true){
            val favoriteEntity = FavoriteEntity()
            favoriteEntity._id = fEntity.get()._id
            favoriteEntity.userId=fEntity.get().userId

            var fList = ArrayList<String>()
            fList = fEntity.get().favorite as ArrayList<String>
            favorite?.let { fList.add(it) }
            favoriteEntity.favorite=fList
            favoriteRepository?.save(favoriteEntity)
            res = 1
        }else{
            val favoriteEntity = FavoriteEntity()
            favoriteEntity.userId= fDTO?.userId
            val fList = ArrayList<String>()
            favorite?.let { fList.add(it) }
            favoriteEntity.favorite=fList
            favoriteRepository?.save(favoriteEntity)
        }
        return res;
    }

    @Throws(Exception::class)
    override fun deleteFav(fDTO: FavoriteDTO?, favorite: String?): Int {
        var res : Int = 0
        val fEntity: Optional<FavoriteEntity>? = fDTO?.userId?.let { favoriteRepository?.findFirstByUserId(it) }

        if(fEntity?.isPresent==true){
            val favoriteEntity = FavoriteEntity()
            favoriteEntity._id = fEntity.get()._id
            favoriteEntity.userId=fEntity.get().userId

            var fList = ArrayList<String>()
            fList = fEntity.get().favorite as ArrayList<String>
            fList.remove(favorite)
            favoriteEntity.favorite=fList
            favoriteRepository?.save(favoriteEntity)
            res = 1
        }
        return res;
    }

    @Throws(Exception::class)
    override  fun userDelete(user_id: String?) :Int {
        var res : Int = 0
       if(user_id?.let { userRepository?.findByUserId(it)?.isPresent }==true){
           user_id.let { userRepository?.deleteByUserId(it) };
           res = 1
       }
        return res

    }
}
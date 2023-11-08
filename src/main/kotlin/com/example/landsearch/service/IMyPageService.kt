package com.example.landsearch.service

import com.example.landsearch.dto.FavoriteDTO
import com.example.landsearch.dto.NoticeDTO
import com.example.landsearch.dto.UserDTO

interface IMyPageService {
    @Throws(Exception::class)
    fun newPassword(pDTO: UserDTO, newPW: String?): Int
    @Throws(Exception::class)
    fun getMyList(userId :String) : List<NoticeDTO>?
    @Throws(Exception::class)
    fun myPage(pDTO: UserDTO?): UserDTO?
    @Throws(Exception::class)
    fun deleteNotice(userId: String,noticeSeq:String) : Int
    @Throws(Exception::class)
    fun getFav(user_id: String): FavoriteDTO?

    @Throws(Exception::class)
    fun insertFav(fDTO: FavoriteDTO?, favorite: String?): Int

    @Throws(Exception::class)
    fun deleteFav(fDTO: FavoriteDTO?, favorite: String?): Int

    @Throws(Exception::class)
    fun userDelete(user_id: String?) :Int
}
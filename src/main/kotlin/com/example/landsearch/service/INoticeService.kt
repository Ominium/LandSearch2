package com.example.landsearch.service

import com.example.landsearch.dto.CommentDTO
import com.example.landsearch.dto.NoticeDTO
import javax.transaction.Transactional


interface INoticeService {
    @Transactional
    @Throws(Exception::class)
    fun getNoticeList(start: Int): List<NoticeDTO?>?
    @Transactional
    @Throws(Exception::class)
    fun getNoticeInfo(pDTO: NoticeDTO?, type: Boolean): NoticeDTO?
    @Transactional
    @Throws(Exception::class)
    fun insertNoticeInfo(pDTO: NoticeDTO?)
    @Transactional
    @Throws(Exception::class)
    fun deleteNoticeInfo(pDTO: NoticeDTO?)
    @Transactional
    @Throws(Exception::class)
    fun updateNoticeInfo(pDTO: NoticeDTO?)

    @Transactional
    fun noticeCount(): Long
    @Transactional
    @Throws(Exception::class)
    fun getCommentsList(start : Int,noticeSeq: String): List<CommentDTO?>?
    @Transactional
    @Throws(Exception::class)
    fun commentCount(noticeSeq: String):Int
    @Transactional
    @Throws(Exception::class)
    fun deleteComment(cDTO: CommentDTO?)



    @Transactional
    @Throws(Exception::class)
    fun commentUpdate(cDTO: CommentDTO?)





    @Transactional
    @Throws(Exception::class)
    fun InsertComment(cDTO: CommentDTO?)


    /*




 */
}

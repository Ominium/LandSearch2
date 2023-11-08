package com.example.landsearch.service.impl

import com.example.landsearch.dto.CommentDTO
import com.example.landsearch.dto.NoticeDTO
import com.example.landsearch.repository.maria.CommentRepository
import com.example.landsearch.repository.maria.NoticeRepository
import com.example.landsearch.repository.maria.entity.CommentEntity
import com.example.landsearch.repository.maria.entity.NoticeEntity
import com.example.landsearch.service.INoticeService
import com.example.landsearch.util.CmmUtil
import com.example.landsearch.util.DateUtil
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.convertValue
import lombok.RequiredArgsConstructor
import lombok.extern.slf4j.Slf4j
import org.springframework.stereotype.Service
import java.util.*
import javax.transaction.Transactional

@Slf4j
@RequiredArgsConstructor
@Service("NoticeService")
class NoticeService(

    private val noticeRepository: NoticeRepository,
    private val commentRepository: CommentRepository

    ) : INoticeService {

    @Transactional
    @Throws(Exception::class)
    override fun getNoticeList(start: Int): List<NoticeDTO>? {


        return ObjectMapper().convertValue(noticeRepository.getNoticeList(start))

    }

    @Transactional
    @Throws(Exception::class)
    override fun getNoticeInfo(pDTO: NoticeDTO?, type: Boolean): NoticeDTO? {

        // 상세보기할 때마다, 조회수 증가하기(수정보기는 제외)

        if (type) {
            // 조회수 증가하기
            val res = noticeRepository.updateReadCnt(pDTO?.noticeSeq)

        }

        // 공지사항 상세내역 가져오기

        // 공지사항 상세내역 가져오기
        val rEntity: NoticeEntity? = noticeRepository.findByNoticeSeq(pDTO?.noticeSeq)

        // 엔티티의 값들을 DTO에 맞게 넣어주기

        // 엔티티의 값들을 DTO에 맞게 넣어주기
        return ObjectMapper().convertValue(rEntity, NoticeDTO::class.java)
    }

    @Transactional
    @Throws(Exception::class)
    override fun insertNoticeInfo(pDTO: NoticeDTO?) {


        val title: String = CmmUtil.nvl(pDTO?.title)
        val noticeYn: String = CmmUtil.nvl(pDTO?.noticeYn)
        val contents: String = CmmUtil.nvl(pDTO?.contents)
        val userId: String = CmmUtil.nvl(pDTO?.userId)


        // 공지사항 저장을 위해서는 PK 값은 빌더에 추가하지 않는다.
        // JPA에 자동 증가 설정을 해놨음

        // 공지사항 저장을 위해서는 PK 값은 빌더에 추가하지 않는다.
        // JPA에 자동 증가 설정을 해놨음
        val noticeEntity = NoticeEntity()
        noticeEntity.title =title
        noticeEntity.contents = contents
        noticeEntity.userId = userId
        noticeEntity.noticeYn = noticeYn
        noticeEntity.readCnt = 0L
        noticeEntity.regDt = DateUtil.getDateTime("yyyy-MM-dd hh:mm:ss")
        noticeEntity.regId = userId
        noticeEntity.chgDt = DateUtil.getDateTime("yyyy-MM-dd hh:mm:ss")
        noticeEntity.chgId = userId
        noticeRepository.save(noticeEntity)

    }
    @Transactional
    @Throws(Exception::class)
    override fun deleteNoticeInfo(pDTO: NoticeDTO?) {
        val noticeSeq: Long? = pDTO?.noticeSeq
       noticeRepository.deleteByNoticeSeq(noticeSeq)
    }
    @Transactional
    @Throws(Exception::class)
    override fun updateNoticeInfo(pDTO: NoticeDTO?) {
        val noticeSeq: Long? = pDTO?.noticeSeq
        val title: String = CmmUtil.nvl(pDTO?.title)
        val noticeYn: String = CmmUtil.nvl(pDTO?.noticeYn)
        val contents: String = CmmUtil.nvl(pDTO?.contents)
        val userId: String = CmmUtil.nvl(pDTO?.userId)


        // 현재 공지사항 조회수 가져오기
        val rEntity = noticeRepository.findByNoticeSeq(noticeSeq)
        // 공지사항 저장을 위해서는 PK 값은 빌더에 추가하지 않는다.
        // JPA에 자동 증가 설정을 해놨음

        // 공지사항 저장을 위해서는 PK 값은 빌더에 추가하지 않는다.
        // JPA에 자동 증가 설정을 해놨음
        val noticeEntity = NoticeEntity()
        noticeEntity.noticeSeq = noticeSeq
        noticeEntity.title =title
        noticeEntity.contents = contents
        noticeEntity.userId = userId
        noticeEntity.noticeYn = noticeYn
        noticeEntity.readCnt = rEntity?.readCnt
        noticeEntity.chgDt = DateUtil.getDateTime("yyyy-MM-dd hh:mm:ss")
        noticeEntity.chgId = userId
        noticeRepository.save(noticeEntity)
    }
    @Transactional
    override fun noticeCount(): Long {

        return noticeRepository.count()
    }
    @Transactional
    @Throws(Exception::class)
    override fun getCommentsList(start : Int,noticeSeq: String): List<CommentDTO>? {
        println("get commentsList 까지 들어옴")
        println(start)
        println(noticeSeq)

        return  ObjectMapper().convertValue(commentRepository.getCommentsList(start,noticeSeq))
    }
    @Transactional
    @Throws(Exception::class)
    override fun commentCount(noticeSeq: String): Int {
        println("comment count 까진 들어옴")
        return commentRepository.countByNoticeSeq(noticeSeq).toInt()
    }
    @Transactional
    @Throws(Exception::class)
    override fun deleteComment(cDTO: CommentDTO?) {
        cDTO?.commentSeq?.let { commentRepository.deleteByCommentSeq(it) }
    }



    @Transactional
    @Throws(Exception::class)
    override fun commentUpdate(cDTO: CommentDTO?) {
        val commentSeq: Long? = cDTO?.commentSeq


        val contents: String = CmmUtil.nvl(cDTO?.contents)
        val chgDt = DateUtil.getDateTime("yyyy-MM-dd hh:mm:ss")
        var cDTO : CommentEntity? = commentSeq?.let { commentRepository.findByCommentSeq(it) }



        // 공지사항 저장을 위해서는 PK 값은 빌더에 추가하지 않는다.
        // JPA에 자동 증가 설정을 해놨음
        val commentEntity = CommentEntity()
        cDTO?.commentSeq = commentSeq
        cDTO?.contents = contents
        cDTO?.chgDt = chgDt
        commentRepository.save(commentEntity)

    }




    @Transactional
    @Throws(Exception::class)
    override fun InsertComment(cDTO: CommentDTO?) {

        val commentSeq: Long? = cDTO?.commentSeq
        val userId : String = CmmUtil.nvl(cDTO?.userId)
        val contents: String = CmmUtil.nvl(cDTO?.contents)
        val regDt : String = DateUtil.getDateTime("yyyy-MM-dd hh:mm:ss")
        val chgDt : String =  DateUtil.getDateTime("yyyy-MM-dd hh:mm:ss")
        val ref : String = CmmUtil.nvl(cDTO?.ref)
        val refRank : String = CmmUtil.nvl(cDTO?.refRank)
        val noticeSeq: String? = cDTO?.noticeSeq



        // 공지사항 저장을 위해서는 PK 값은 빌더에 추가하지 않는다.
        // JPA에 자동 증가 설정을 해놨음
        val commentEntity = CommentEntity()
        commentEntity.commentSeq = commentSeq
        commentEntity.contents = contents
        commentEntity.userId = userId
        commentEntity.ref=ref
        commentEntity.refRank = refRank
        commentEntity.regDt = regDt
        commentEntity.chgDt = chgDt
        commentEntity.noticeSeq = noticeSeq
        commentRepository.save(commentEntity)


    }






    /*
   */
}

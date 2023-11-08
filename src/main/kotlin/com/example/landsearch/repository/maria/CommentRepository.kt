package com.example.landsearch.repository.maria

import com.example.landsearch.repository.maria.entity.CommentEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

import org.springframework.stereotype.Repository
import java.util.*
import javax.transaction.Transactional

@Repository
interface CommentRepository: JpaRepository<CommentEntity, Long> {

    @Modifying(clearAutomatically = true)
    @Query(
        value="SELECT * FROM COMMENTS C WHERE C.NOTICE_SEQ = :noticeSeq ORDER BY C.REF, C.REF_RANK, C.REG_DT DESC LIMIT 10  OFFSET :start",
                nativeQuery = true
    )
    fun getCommentsList(@Param("start")start : Int,@Param("noticeSeq")noticeSeq: String) :List<CommentEntity>

    fun findByCommentSeq(commentSeq: Long) : CommentEntity
    fun deleteByCommentSeq(commentSeq: Long)

    fun countByNoticeSeq(noticeSeq: String): Long
    fun deleteAllByUserId(userId : String)
}
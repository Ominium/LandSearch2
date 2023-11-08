package com.example.landsearch.repository.maria

import com.example.landsearch.dto.NoticeDTO
import com.example.landsearch.repository.maria.entity.NoticeEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface NoticeRepository: JpaRepository<NoticeEntity, Long> {
    fun findAllByOrderByNoticeSeqDesc(): List<NoticeEntity>

    @Modifying(clearAutomatically = true)
    @Query(
        value = "UPDATE NOTICE A SET A.READ_CNT = IFNULL(A.READ_CNT, 0) + 1 WHERE A.NOTICE_SEQ = :noticeSeq",
        nativeQuery = true
    )
    fun updateReadCnt(@Param("noticeSeq") noticeSeq: Long?): Int

    fun deleteByNoticeSeq(noticeSeq: Long?)
    fun deleteByUserIdAndNoticeSeq(userId: String,noticeSeq: Long?)

    fun findAllByUserId(userId : String) :List<NoticeEntity>
    fun findByNoticeSeq(noticeSeq: Long?): NoticeEntity?
    @Modifying(clearAutomatically = true)
    @Query(
       "SELECT COUNT(*) FROM NOTICE",
        nativeQuery = true
    )
    fun noticeCount():Long
    @Modifying(clearAutomatically = true)
    @Query(
        value="SELECT *  FROM NOTICE N ORDER BY N.NOTICE_YN DESC,N.NOTICE_SEQ DESC, N.REG_DT DESC LIMIT 10  OFFSET :start",
        nativeQuery = true
    )
    fun getNoticeList(@Param("start")start : Int):List<NoticeEntity>
    fun deleteAllByUserId(userId : String)
}
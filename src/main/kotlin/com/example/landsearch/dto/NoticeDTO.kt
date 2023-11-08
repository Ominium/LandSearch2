package com.example.landsearch.dto

import com.example.landsearch.repository.maria.entity.NoticeEntity
import lombok.Data
import org.bson.types.ObjectId
import java.util.Date
import javax.persistence.Table


class NoticeDTO{

    var noticeSeq : Long? = null

    var title: String? = null // 제목

    var noticeYn: String? = null // 공지글 여부

    var contents: String? = null // 글 내용

    var userId: String? = null// 작성자

    var readCnt: Long? = null// 조회수

    var regId: String? = null // 등록자 아이디

    val regDt: String? = null // 등록일

    var chgId: String? = null // 수정자 아이디

    var chgDt: String? = null // 수정일





}
package com.example.landsearch.repository.maria.entity

import com.querydsl.core.annotations.QueryEntity
import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Getter
import lombok.NoArgsConstructor
import org.hibernate.annotations.DynamicInsert
import org.hibernate.annotations.DynamicUpdate
import java.sql.Date
import javax.persistence.*

@QueryEntity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "NOTICE")
@DynamicInsert
@DynamicUpdate
@Cacheable
@Entity
class NoticeEntity  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notice_seq")
    var noticeSeq: Long? = null

    @Column(name = "title", length = 500, nullable = false)
    var title: String? = null

    @Column(name = "notice_yn", length = 1, nullable = false)
    var noticeYn: String? = null

    @Column(name = "contents", nullable = false)
    var contents: String? = null

    @Column(name = "user_id", nullable = false)
    var userId: String? = null

    @Column(name = "read_cnt", nullable = false)
    var  readCnt: Long? = null

    @Column(name = "reg_id", updatable = false)
    var  regId: String? = null

    @Column(name = "reg_dt", updatable = false)
    var regDt: String? = null

    @Column(name = "chg_id")
    var chgId: String? = null

    @Column(name = "chg_dt")
    var chgDt: String? = null
}
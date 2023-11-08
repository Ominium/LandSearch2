package com.example.landsearch.repository.maria.entity

import com.querydsl.core.annotations.QueryEntity
import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Getter
import lombok.NoArgsConstructor
import org.bson.types.ObjectId
import org.hibernate.annotations.DynamicInsert
import org.hibernate.annotations.DynamicUpdate
import javax.persistence.*

@QueryEntity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "COMMENTS")
@DynamicInsert
@DynamicUpdate
@Cacheable
@Entity
class CommentEntity {

    @Column(name="notice_seq")
    var noticeSeq: String? = null
    @Column(name="user_id")
    var userId: String? = null
    @Column(name = "contents")
    var contents: String? = null
    @Column(name = "reg_dt")
    var regDt: String? = null
    @Column(name="chg_dt")
    var chgDt: String? = null
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="comment_seq")
    var commentSeq: Long? = null
    @Column(name="ref")
    var ref: String? = null
    @Column(name="ref_rank")
    var refRank: String? = null

}
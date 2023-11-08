package com.example.landsearch.repository.mongo.entity

import com.querydsl.core.annotations.QueryEntity
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.mapping.Document
import javax.persistence.*

@Entity
@QueryEntity
@Document(collection = "UserCollection")
class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "_id")
    var id: ObjectId? = null

    @Column(name = "user_name")
    var userName: String? = null

    @Column(name = "user_id")
    var userId: String? = null

    @Column(name = "password")
    var password: String? = null

    @Column(name = "email")
    var email: String? = null

    @Column(name = "reg_id")
    var regId: String? = null
    @Column(name = "reg_dt")
    var regDt: String? = null
    @Column(name = "chg_id")
    var chgId: String? = null
    @Column(name = "chg_dt")
    var chgDt: String? = null

    // 회원가입시, 중복가입을 방지 위해 사용할 변수
    // DB를 조회해서 회원이 존재하면 Y값을 반환함
    // DB테이블에 존재하지 않는 가상의 컬럼(ALIAS)
    @Column(name = "exists_yn")
    var existsYn: String? = null
}
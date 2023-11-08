package com.example.landsearch.dto


import lombok.Data
import org.bson.types.ObjectId

@Data
class UserDTO {
    var _id : ObjectId?= null
    var userName: String? = null
    var userId: String? = null
    var password: String? = null
    var email: String? = null
    var regId: String? = null
    var regDt: String? = null
    var chgId: String? = null
    var chgDt: String? = null

    // 회원가입시, 중복가입을 방지 위해 사용할 변수
    // DB를 조회해서 회원이 존재하면 Y값을 반환함
    // DB테이블에 존재하지 않는 가상의 컬럼(ALIAS)
    var existsYn: String? = null
}
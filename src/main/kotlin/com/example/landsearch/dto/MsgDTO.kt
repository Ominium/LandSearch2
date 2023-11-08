package com.example.landsearch.dto

import com.fasterxml.jackson.annotation.JsonInclude
import lombok.Getter
import lombok.Setter

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
class MsgDTO {
    val result = 0 // 성공 : 1 / 실패 : 그 외
    var msg: String? = null // 메시지
}

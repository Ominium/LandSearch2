package com.example.landsearch.dto

import com.fasterxml.jackson.annotation.JsonInclude
import lombok.Getter
import lombok.Setter

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
class MailDTO {
    var toMail: String? = null // 받는 사람

    var title: String? = null // 보내는 메일 제목

    var contents: String? = null // 보내는 메일 내용

}
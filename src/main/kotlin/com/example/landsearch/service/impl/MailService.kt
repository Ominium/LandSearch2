package com.example.landsearch.service.impl

import com.example.landsearch.dto.MailDTO
import com.example.landsearch.service.IMailService
import com.example.landsearch.util.CmmUtil
import lombok.extern.slf4j.Slf4j

import org.springframework.stereotype.Service
import java.util.*
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage


@Slf4j
@Service("MailService")
class MailService : IMailService {
    val host = "smtp.naver.com" // 네이버에서 제공하는 SMTP서버
    val user = "tlssd@naver.com" // 본인 네이버 아이디
    val password = "" // 본인 네이버 비밀번호 github private
    override fun doSendMail(pDTO: MailDTO?): Int {

        // 로그 찍기(추후 찍은 로그를 통해 이 함수에 접근했는지 파악하기 용이하다.)


        // 메일 발송 성공여부(발송성공 : 1 / 발송실패 : 0)
        val res = 1

        //전달 받은 DTO로부터 데이터 가져오기(DTO객체가 메모리에 올라가지 않아 Null이 발생할 수 있기 때문에 에러방지차원으로 if문 사용함

        val toMail: String = CmmUtil.nvl(pDTO?.toMail) // 받는사람
        val props = Properties()
        props["mail.smtp.host"] = host // javax 외부 라이브러리에 메일 보내는 사람의 정보 설정
        props["mail.smtp.port"] = 587 // javax 외부 라이브러리에 메일 보내는 사람의 정보 설정
        props["mail.smtp.auth"] = "true" // javax 외부 라이브러리에 메일 보내는 사람 인증 여부 설정

        // 네이버 SMTP서버 인증 처리 로직
        val session = Session.getInstance(props,
            object : Authenticator() {
                override fun getPasswordAuthentication(): PasswordAuthentication {
                    return PasswordAuthentication(user, password)
                }
            })

        // 메시지 객체 만들기
        val message = MimeMessage(session)
        message.setFrom(InternetAddress(user))
        // 수신자 설정, 여러명으로도 가능
        message.setRecipients(
            Message.RecipientType.TO,
            InternetAddress.parse(toMail)
        )
        message.subject = pDTO?.title
        message.setText(pDTO?.contents)

        // 메일발송
        Transport.send(message)
        return res
    }
}


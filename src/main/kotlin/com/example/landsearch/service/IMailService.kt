package com.example.landsearch.service

import com.example.landsearch.dto.MailDTO

interface IMailService {
    fun doSendMail(pDTO: MailDTO?): Int
}
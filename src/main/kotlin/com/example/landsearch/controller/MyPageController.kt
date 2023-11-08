package com.example.landsearch.controller

import com.example.landsearch.dto.FavoriteDTO
import com.example.landsearch.dto.NoticeDTO
import com.example.landsearch.dto.PageDTO
import com.example.landsearch.dto.UserDTO
import com.example.landsearch.service.IMyPageService
import lombok.extern.slf4j.Slf4j
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpSession
import com.example.landsearch.util.EncryptUtil

@Controller
@Slf4j
@RequestMapping("/myPage")
class MyPageController(
    private val myPageService: IMyPageService? = null
) {
    @GetMapping(value = ["myList"])
    @Throws(Exception::class)
    @ResponseBody
    fun myList(userId: String): List<NoticeDTO>? {

        return myPageService?.getMyList(userId)
    }
    @PostMapping(value=["deleteNotice"])
    @Throws(Exception::class)
    @ResponseBody
    fun deleteNotice(user_id: String,notice_seq : String): String{


        val res: Int? = myPageService?.deleteNotice(user_id, notice_seq)

        if (res != null) {
            return if (res > 0) {
                "성공하였습니다."
            } else {
                "실패하였습니다."
            }
        }
        return ""
    }

    @GetMapping(value = ["/myPage"])
    @Throws(Exception::class)
    fun myPage(session: HttpSession, model: ModelMap): String? {
        println("ㅇㅅㅇ")
        val pDTO = UserDTO()
        val user_id = session.getAttribute("SS_USER_ID") as String
        pDTO.userId = (user_id)
        val fDTO: FavoriteDTO? = myPageService?.getFav(user_id)
        val rDTO: UserDTO? = myPageService?.myPage(pDTO)
        model.addAttribute("rDTO", rDTO)
        model.addAttribute("fDTO", fDTO)
        return "myPage/myPage"
    }

    @PostMapping(value = ["/newPassword"])
    @Throws(Exception::class)
    fun newPassword(request: HttpServletRequest, session: HttpSession, model: ModelMap): String? {

        val pDTO = UserDTO()
        val user_id = session.getAttribute("SS_USER_ID") as String
        val password = request.getParameter("password")
        val newPassword = request.getParameter("newpassword")
        pDTO.userId = (user_id)
        pDTO.password = (EncryptUtil.encHashSHA256(password))
        var msg = ""
        val res: Int? = myPageService?.newPassword(pDTO, EncryptUtil.encHashSHA256(newPassword))
        if (res != null) {
            msg = if (res > 0) {
                "성공하였습니다."
            } else {
                "실패하였습니다."
            }
        }
        model.addAttribute("msg", msg)
        return "myPage/newPassword"
    }

    @GetMapping(value = ["/myPassword"])
    @Throws(Exception::class)
    fun myPassword(): String? {
        return "myPage/myPassword"
    }

    @PostMapping(value = ["/insertFav"])
    @ResponseBody
    @Throws(Exception::class)
    fun insertFav(user_id: String?, favorite: String?): String? {

        val fDTO = FavoriteDTO()
        fDTO.userId = (user_id)
        val res: Int? = myPageService?.insertFav(fDTO, favorite)

        if (res != null) {
            return if (res > 0) {
                "성공하였습니다."
            } else {
                "실패하였습니다."
            }
        }
        return ""
    }

    @PostMapping(value = ["/userDelete"])
    @ResponseBody
    @Throws(Exception::class)
    fun userDelete(user_id: String?, session: HttpSession): String? {
         myPageService?.userDelete(user_id)
        session.removeAttribute("SS_USER_ID")
        return "회원탈퇴 되었습니다."
    }

    @PostMapping(value = ["/deleteFav"])
    @ResponseBody
    @Throws(Exception::class)
    fun deleteFav(user_id: String?, favorite: String?): String? {
        val fDTO = FavoriteDTO()
        fDTO.userId = (user_id)
        val res: Int? = myPageService?.deleteFav(fDTO, favorite)

        if (res != null) {
            return if (res > 0) {
                "성공하였습니다."
            } else {
                "실패하였습니다."
            }
        }
        return ""
    }

}
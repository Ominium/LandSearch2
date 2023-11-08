package com.example.landsearch.controller

import com.example.landsearch.dto.CommentDTO
import com.example.landsearch.dto.MsgDTO
import com.example.landsearch.dto.NoticeDTO
import com.example.landsearch.dto.PageDTO
import com.example.landsearch.service.INoticeService
import com.example.landsearch.util.CmmUtil
import com.fasterxml.jackson.databind.ObjectMapper
import lombok.extern.slf4j.Slf4j
import org.json.JSONArray
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.*

import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpSession

@Controller
@Slf4j
@RequestMapping("/notice")

class NoticeController(
    private val noticeService : INoticeService
) {


    @GetMapping(value = ["noticeList"])
    @Throws(Exception::class)
    fun noticeList(request: HttpServletRequest, model: ModelMap): String? {
        // 로그 찍기(추후 찍은 로그를 통해 이 함수에 접근했는지 파악하기 용이하다.)

        val nDTO = NoticeDTO()
        val pageDTO: PageDTO
        val count: Long = noticeService.noticeCount()

        val no: String = CmmUtil.nvl(request.getParameter("num"))
        if (no.isEmpty()) {
            pageDTO = PageDTO(1, count.toInt())
        } else {
            pageDTO = PageDTO(no.toInt(), count.toInt())
        }
        println(pageDTO.start)

        // 공지사항 리스트 가져오기
        var rList: List<NoticeDTO?>? = noticeService.getNoticeList(pageDTO.start)
        if (rList == null) {
            rList = ArrayList<NoticeDTO?>()
        }else{
            model.addAttribute("rList", rList)
        }

        // 조회된 리스트 결과값 넣어주기

        model.addAttribute("count", count)

        // 현재 페이지
        model.addAttribute("select", pageDTO.num)
        model.addAttribute("startPageNum", pageDTO.startPageNum)
        model.addAttribute("endPageNum", pageDTO.endPageNum)

        // 이전 및 다음
        model.addAttribute("prev", pageDTO.prev)
        model.addAttribute("next", pageDTO.next)

        return "notice/noticeList"
    }
    @GetMapping(value = ["noticeInfo"])
    @Throws(Exception::class)
    fun noticeInfo(request: HttpServletRequest, model: ModelMap): String? {

        val nSeq: String = CmmUtil.nvl(request.getParameter("nSeq")) // 공지글번호(PK)


        val pDTO = NoticeDTO()
        pDTO.noticeSeq = (nSeq.toLong()) // String 타입을 long 타입으로 변경

        // 공지사항 상세정보 가져오기
        // Java 8부터 제공되는 Optional 활용하여 NPE(Null Pointer Exception) 처리
        val rDTO: NoticeDTO? = noticeService.getNoticeInfo(pDTO,true)
        // 조회된 리스트 결과값 넣어주기
        model.addAttribute("rDTO", rDTO)

        return "notice/noticeInfo"
    }
    @GetMapping(value = ["noticeReg"])
    fun noticeReg(): String? {



        return "notice/noticeReg"
    }
    @GetMapping(value = ["noticeEditInfo"])
    @Throws(Exception::class)
    fun noticeEditInfo(request: HttpServletRequest, model: ModelMap): String? {

        val nSeq: String = CmmUtil.nvl(request.getParameter("nSeq")) // 공지글번호(PK)


        val pDTO = NoticeDTO()
        pDTO.noticeSeq = nSeq.toLong() // String 타입을 long 타입으로 변경


        // 공지사항 상세정보 가져오기
        // Java 8부터 제공되는 Optional 활용하여 NPE(Null Pointer Exception) 처리
        val rDTO: NoticeDTO? = noticeService.getNoticeInfo(pDTO, true)


        // 조회된 리스트 결과값 넣어주기
        model.addAttribute("rDTO", rDTO)

        return "notice/noticeEditInfo"
    }
    @ResponseBody
    @PostMapping(value = ["noticeInsert"])
    fun noticeInsert(request: HttpServletRequest, session: HttpSession): MsgDTO? {

        var msg = "" // 메시지 내용
        var dto: MsgDTO? = null // 결과 메시지 구조
        try {
            // 로그인된 사용자 아이디를 가져오기
            // 로그인을 아직 구현하지 않았기에 공지사항 리스트에서 로그인 한 것처럼 Session 값을 저장함
            val userId: String = CmmUtil.nvl(session.getAttribute("SS_USER_ID") as String)
            val title: String = CmmUtil.nvl(request.getParameter("title")) // 제목
            val noticeYn: String = CmmUtil.nvl(request.getParameter("noticeYn")) // 공지글 여부
            val contents: String = CmmUtil.nvl(request.getParameter("contents")) // 내용




            // 데이터 저장하기 위해 DTO에 저장하기
            val pDTO = NoticeDTO()
            pDTO.userId = userId
            pDTO.title =title
            pDTO.noticeYn = (noticeYn)
            pDTO.contents = (contents)



            noticeService.insertNoticeInfo(pDTO)

            // 저장이 완료되면 사용자에게 보여줄 메시지
            msg = "등록되었습니다."
        } catch (e: Exception) {

            // 저장이 실패되면 사용자에게 보여줄 메시지
            msg = "실패하였습니다. : " + e.message

            e.printStackTrace()
        } finally {
            // 결과 메시지 전달하기
            dto = MsgDTO()
            dto.msg = (msg)

        }
        return dto
    }
    @ResponseBody
    @PostMapping(value = ["noticeUpdate"])
    fun noticeUpdate(session: HttpSession, request: HttpServletRequest): MsgDTO? {

        var msg = "" // 메시지 내용
        var dto: MsgDTO? = null // 결과 메시지 구조
        try {
            val userId: String = CmmUtil.nvl(session.getAttribute("SS_USER_ID") as String) // 아이디
            val nSeq: String = CmmUtil.nvl(request.getParameter("nSeq")) // 글번호(PK)
            val title: String = CmmUtil.nvl(request.getParameter("title")) // 제목
            val noticeYn: String = CmmUtil.nvl(request.getParameter("noticeYn")) // 공지글 여부
            val contents: String = CmmUtil.nvl(request.getParameter("contents")) // 내용


            val pDTO = NoticeDTO()
            pDTO.userId = (userId)
            pDTO.noticeSeq = (nSeq.toLong()) // String 타입을 long 타입으로 변경
            pDTO.title=title
            pDTO.noticeYn = (noticeYn)
            pDTO.contents = (contents)

            // 게시글 수정하기 DB
            noticeService.updateNoticeInfo(pDTO)
            msg = "수정되었습니다."
        } catch (e: Exception) {
            msg = "실패하였습니다. : " + e.message

            e.printStackTrace()
        } finally {

            // 결과 메시지 전달하기
            dto = MsgDTO()
            dto.msg = (msg)

        }
        return dto
    }
    @ResponseBody
    @PostMapping(value = ["noticeDelete"])
    fun noticeDelete(request: HttpServletRequest): MsgDTO? {

        var msg = "" // 메시지 내용
        var dto: MsgDTO? = null // 결과 메시지 구조
        try {
            val nSeq: String = CmmUtil.nvl(request.getParameter("nSeq")) // 글번호(PK)


            val pDTO = NoticeDTO()
            pDTO.noticeSeq = nSeq.toLong()

            // 게시글 삭제하기 DB
            noticeService.deleteNoticeInfo(pDTO)
            msg = "삭제되었습니다."
        } catch (e: Exception) {
            msg = "실패하였습니다. : " + e.message

            e.printStackTrace()
        } finally {
            // 결과 메시지 전달하기
            dto = MsgDTO()
            dto.msg = (msg)

        }
        return dto
    }
    @GetMapping(value = ["CommentCount"])
    @ResponseBody
    @Throws(Exception::class)
    fun CommentCount(notice_seq: String?): String? {

        val nDTO = CommentDTO()
        nDTO.noticeSeq = (notice_seq)
        val count: Int? = nDTO.noticeSeq?.let { noticeService.commentCount(it) }
        return count.toString()
    }
    @GetMapping(value = ["Comment"])
    @ResponseBody
    @Throws(Exception::class)
    fun CommentCheck(notice_seq: String?, num: String): Any? {

        val nDTO = CommentDTO()
        nDTO.noticeSeq = (notice_seq)
        val pageDTO: PageDTO
        val count: Int? = nDTO.noticeSeq?.let { noticeService.commentCount(it) }
        if (num.isEmpty()) {
            pageDTO = count?.let { PageDTO(1, it) }!!
        } else {
            pageDTO = count?.let { PageDTO(num.toInt(), it) }!!
        }
        println(pageDTO.start)
        println(notice_seq)
        val cList: List<CommentDTO?>? = notice_seq?.let { noticeService.getCommentsList(pageDTO.start, it) }

        val objectMapper = ObjectMapper()
        val jsonArray = JSONArray()
        if (cList != null) {
            for (commentDTO in cList) {
                jsonArray.put(objectMapper.writeValueAsString(commentDTO))
            }
        }
        return jsonArray.toString()
    }
    @PostMapping(value = ["InsertComment"])
    @ResponseBody
    @Throws(Exception::class)
    fun InsertComment(notice_seq: String?, user_id: String?, contents: String?) {

        val cDTO = CommentDTO()
        cDTO.noticeSeq = notice_seq
        cDTO.userId = user_id
        cDTO.contents= contents
        noticeService.InsertComment(cDTO)

    }

    @PostMapping(value = ["InsertReply"])
    @ResponseBody
    @Throws(Exception::class)
    fun InsertReply(notice_seq: String?, user_id: String?, contents: String?, ref: String?, ref_rank: String) {

        val cDTO = CommentDTO()
        cDTO.noticeSeq = (notice_seq)
        cDTO.userId = (user_id)
        cDTO.contents=(contents)
        cDTO.ref = (ref)
        cDTO.refRank = ((ref_rank.toInt() + 1).toString())
        noticeService.InsertComment(cDTO)

    }

    @PostMapping(value = ["CommentUpdate"])
    @ResponseBody
    @Throws(Exception::class)
    fun CommentUpdate(comment_seq: String?, contents: String?) {
        val cDTO = CommentDTO()

        cDTO.commentSeq = (comment_seq)?.toLong()
        cDTO.contents = (contents)
        noticeService.commentUpdate(cDTO)
    }





    @GetMapping(value = ["CommentPage"])
    @ResponseBody
    @Throws(Exception::class)
    fun CommentPageCheck(notice_seq: String?, num: String): Any? {

        val nDTO = CommentDTO()
        nDTO.noticeSeq = (notice_seq)
        val pageDTO: PageDTO
        val count: Int? = nDTO.noticeSeq?.let { noticeService.commentCount(it) }
        if (num.isEmpty()) {
            pageDTO = count?.let { PageDTO(1, it) }!!
        } else {
            pageDTO = count?.let { PageDTO(num.toInt(), it) }!!
        }
        val objectMapper = ObjectMapper()
        return objectMapper.writeValueAsString(pageDTO)
    }

    @DeleteMapping(value = ["commentDelete"])
    @ResponseBody
    @Throws(Exception::class)
    fun CommentDelete(comment_seq: String?) {
        val cDTO = CommentDTO()
        cDTO.commentSeq = (comment_seq)?.toLong()
        noticeService.deleteComment(cDTO)
    }


}

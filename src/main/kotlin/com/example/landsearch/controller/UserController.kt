package com.example.landsearch.controller

import com.example.landsearch.dto.UserDTO
import com.example.landsearch.service.IUserService
import com.example.landsearch.util.CmmUtil
import com.example.landsearch.util.EncryptUtil
import lombok.extern.slf4j.Slf4j
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpSession

@Controller
@Slf4j
@RequestMapping("/user")
class UserController(
    private val userService: IUserService
) {



    @GetMapping(value = ["signForm"])
    fun userRegForm(): String? {
        return "user/signForm"
    }

    @DeleteMapping(value = ["UserDelete"])
    @ResponseBody
    @Throws(Exception::class)
    fun userDelete(user_id: String?) {
        val uDTO = UserDTO()
        userService.deleteUserId(uDTO)

    }



    /**
     * 회원가입 로직 처리
     */
    @PostMapping("/insertUserInfo")
    @Throws(Exception::class)
    fun insertUserInfo(request: HttpServletRequest, model: ModelMap): String? {


        var msg = ""
        var res = 1
        val pDTO = UserDTO()
        try {


            val userId: String = CmmUtil.nvl(request.getParameter("user_id")) //아이디
            val userName: String = CmmUtil.nvl(request.getParameter("user_name")) //이름
            val password: String = CmmUtil.nvl(request.getParameter("password")) //비밀번호
            val email: String = CmmUtil.nvl(request.getParameter("email")) //이메일

            pDTO.userId = userId
            pDTO.userName = userName
            pDTO.password =EncryptUtil.encHashSHA256(password)
            pDTO.email = EncryptUtil.encAES128CBC(email)

            res = userService.insertUserInfo(pDTO)

            if(res ==1){
                model.addAttribute("res", true)
            }else{
                model.addAttribute("res", false)
            }
            msg = if (res == 1) {
                "회원가입되었습니다."

                //추후 회원가입 입력화면에서 ajax를 활용해서 아이디 중복, 이메일 중복을 체크하길 바람
            } else if (res == 2) {
                "이미 가입된 이메일 주소이거나 아이디가 존재합니다."
            } else {
                "오류로 인해 회원가입이 실패하였습니다."
            }
        } catch (e: Exception) {
            //저장이 실패되면 사용자에게 보여줄 메시지
            msg = "실패하였습니다. : $e"

            e.printStackTrace()
        } finally {

            //회원가입 여부 결과 메시지 전달하기
            model.addAttribute("msg", msg)


            //회원가입 여부 결과 메시지 전달하기
            model.addAttribute("pDTO", pDTO)

            //변수 초기화(메모리 효율화 시키기 위해 사용함)

        }
        return "user/signSuccess"
    }


    /**
     * 로그인을 위한 입력 화면으로 이동
     */
    @GetMapping("/loginPage")
    fun loginForm(): String? {

        return "user/loginPage"
    }

    @GetMapping("/logOut")
    fun logOut(session: HttpSession): String? {

        session.removeAttribute("SS_USER_ID")
        return "user/logOut"
    }

    @GetMapping("/idpsCheck")
    fun loginidSearch(): String? {

        return "user/idpsCheck"
    }

    @PostMapping("/find_id")
    @ResponseBody
    @Throws(Exception::class)
    fun find_id(email: String?): String? {
        val pDTO = UserDTO()
        pDTO.email = email?.let { EncryptUtil.encAES128CBC(it) }
        println(pDTO.email)
        val rDTO: UserDTO? = userService.findUserId(pDTO)
        println(rDTO?.userId)
        return rDTO?.userId
    }

    @PostMapping("/findEmail")
    @ResponseBody
    fun findEmail(email: String?): Boolean? {
        val pDTO = UserDTO()
        pDTO.email = email?.let { EncryptUtil.encAES128CBC(it) }

        return userService.findEmail(pDTO)

    }

    @PostMapping("/find_ps")
    @ResponseBody
    @Throws(Exception::class)
    fun find_ps(email: String?, user_id: String?): String {
        var msg = ""
        var res = 1
        val pDTO = UserDTO()
        try {
            pDTO.userId = user_id

            pDTO.email = (email?.let { EncryptUtil.encAES128CBC(it) })
            res = userService.findPassword(pDTO)

            msg = if (res == 1) {
                "새로운 비밀번호가 메일로 발송되었습니다."

                //추후 회원가입 입력화면에서 ajax를 활용해서 아이디 중복, 이메일 중복을 체크하길 바람
            } else if (res == 2) {
                "아이디가 존재하지 않습니다"
            } else {
                "오류로 인해 비밀번호 찾기가 실패하였습니다."
            }
        } catch (e: Exception) {
            //저장이 실패되면 사용자에게 보여줄 메시지
            msg = "실패하였습니다. : $e"

            e.printStackTrace()
        }
        return msg
    }
    /**
     * 로그인 처리 및 결과 알려주는 화면으로 이동
     */
    @PostMapping("/loginResult")
    @Throws(Exception::class)
    fun getUserLoginCheck(session: HttpSession, request: HttpServletRequest, model: ModelMap): String? {


        //로그인 처리 결과를 저장할 변수 (로그인 성공 : 1, 아이디, 비밀번호 불일치로인한 실패 : 0, 시스템 에러 : 2)
        var res = false


        try {


            val user_id: String = CmmUtil.nvl(request.getParameter("user_id")) //아이디
            val password: String = CmmUtil.nvl(request.getParameter("password")) //비밀번호

            val pDTO = UserDTO()
            pDTO.userId = user_id

            //비밀번호는 절대로 복호화되지 않도록 해시 알고리즘으로 암호화함
            pDTO.password = EncryptUtil.encHashSHA256(password)
            println(pDTO.userId)
            println(pDTO.password)
            res = userService.getUserLoginCheck(pDTO)?.isPresent == true
            println(res)
            /*
             * 로그인을 성공했다면, 회원아이디 정보를 session에 저장함
             *
             * 세션은 톰켓(was)의 메모리에 존재하며, 웹사이트에 접속한 사람(연결된 객체)마다 메모리에 값을 올린다.
             * 			 *
             * 예) 톰켓에 100명의 사용자가 로그인했다면, 사용자 각각 회원아이디를 메모리에 저장하며.
             *    메모리에 저장된 객체의 수는 100개이다.
             *    따라서 과도한 세션은 톰켓의 메모리 부하를 발생시켜 서버가 다운되는 현상이 있을 수 있기때문에,
             *    최소한으로 사용하는 것을 권장한다.
             *
             * 스프링에서 세션을 사용하기 위해서는 함수명의 파라미터에 HttpSession session 존재해야 한다.
             * 세션은 톰켓의 메모리에 저장되기 때문에 url마다 전달하는게 필요하지 않고,
             * 그냥 메모리에서 부르면 되기 때문에 jsp, controller에서 쉽게 불러서 쓸수 있다.
             * */if (res) { //로그인 성공

                /*
                 * 세션에 회원아이디 저장하기, 추후 로그인여부를 체크하기 위해 세션에 값이 존재하는지 체크한다.
                 * 일반적으로 세션에 저장되는 키는 대문자로 입력하며, 앞에 SS를 붙인다.
                 *
                 * Session 단어에서 SS를 가져온 것이다.
                 */
                session.setAttribute("SS_USER_ID", user_id)
            }
        } catch (e: Exception) {
            //저장이 실패되면 사용자에게 보여줄 메시지


            e.printStackTrace()
        } finally {


            /* 로그인 처리 결과를 jsp에 전달하기 위해 변수 사용
             * 숫자 유형의 데이터 타입은 값을 전달하고 받는데 불편함이  있어
             * 문자 유형(String)으로 강제 형변환하여 jsp에 전달한다.
             * */
            if(res){
                model.addAttribute("res", true)
            }else{
                model.addAttribute("res", false)
            }


        }
        return "user/loginResult"
    }

}
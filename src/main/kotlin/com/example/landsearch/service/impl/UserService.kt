package com.example.landsearch.service.impl

import com.example.landsearch.dto.CommentDTO
import com.example.landsearch.dto.MailDTO
import com.example.landsearch.dto.UserDTO
import com.example.landsearch.repository.maria.CommentRepository
import com.example.landsearch.repository.maria.NoticeRepository
import com.example.landsearch.repository.mongo.FavoriteRepository
import com.example.landsearch.repository.mongo.UserRepository
import com.example.landsearch.repository.mongo.entity.UserEntity
import com.example.landsearch.service.IMailService
import com.example.landsearch.service.IUserService
import com.example.landsearch.util.CmmUtil
import com.example.landsearch.util.DateUtil
import com.example.landsearch.util.EncryptUtil
import lombok.RequiredArgsConstructor
import lombok.extern.slf4j.Slf4j
import org.springframework.stereotype.Service
import java.util.*


@Slf4j
@RequiredArgsConstructor
@Service("UserService")
class UserService(
    private val userRepository: UserRepository,
    private val commentRepository: CommentRepository,
    private val favoriteRepository: FavoriteRepository,
    private val noticeRepository: NoticeRepository,
    private val mailService: IMailService
) : IUserService {


    //메일 발송을 위한 MailService 자바 객체 가져오기





    override fun deleteUserId(pDTO: UserDTO?) {
        val userId = pDTO?.userId

        userId?.let { noticeRepository.deleteAllByUserId(it) }
        userId?.let { favoriteRepository.deleteAllByUserId(it) }
        userId?.let { commentRepository.deleteAllByUserId(it) }
        userId?.let { userRepository.deleteByUserId(it) }
    }


    @Throws(Exception::class)
    override fun findUserId(pDTO: UserDTO?): UserDTO? {
        try{
            val uDTO: Optional<UserEntity>? = pDTO?.email?.let { userRepository.findByEmail(it) }
            println(uDTO?.get()?.userId)
            val mDTO = MailDTO()

            if (uDTO?.isPresent==true) {
                mDTO.toMail = EncryptUtil.decAES128CBC(uDTO.get().email)
                mDTO.title = "아이디 찾기 입니다."
                mDTO.contents = "회원님의 아이디 : " + uDTO.get().userId
                mailService.doSendMail(mDTO)
            }


            val userDTO = UserDTO()
            userDTO.userId = uDTO?.get()?.userId
            userDTO.email = uDTO?.get()?.email
            return userDTO
        }catch (e : Exception){
            return null
        }

    }

    fun randomPs(passwordlength: Int): String {
        val pwdcharSet = charArrayOf(
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
            'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'
        )
        var idx : Int
        val sb = StringBuffer()
        for (i in 0 until passwordlength) {
            idx = (pwdcharSet.size * Math.random()).toInt()
            sb.append(pwdcharSet[idx])
        }
        return sb.toString()
    }
    override fun findEmail(pDTO: UserDTO?) : Boolean? {


        return pDTO?.email?.let { userRepository.findByEmail(it)?.isPresent }
    }
    @Throws(Exception::class)
    override fun findPassword(pDTO: UserDTO) : Int {
        var res = 0
        try {

            val psnew = randomPs(8)

            val newpassword = EncryptUtil.encHashSHA256(psnew)
            val uDTO: Optional<UserEntity>? = pDTO.userId?.let {
                pDTO.email?.let { it1 ->
                    userRepository.findByUserIdAndEmail(it, it1)
                }
            }
            if (uDTO?.isPresent == true) {
                val userEntity = UserEntity()
                userEntity.id = uDTO.get().id
                userEntity.password = newpassword
                userEntity.email = uDTO.get().email
                userEntity.userId = uDTO.get().userId
                userRepository.save(userEntity)

                val mDTO = MailDTO()
                mDTO.toMail = EncryptUtil.decAES128CBC(uDTO.get().email)
                mDTO.title = "새로운 비밀번호로 로그인 후 비밀번호 변경 바랍니다."
                mDTO.contents = "회원님의 비밀번호 : $psnew"
                mailService.doSendMail(mDTO)
                res = 1

            }


        } catch (e: Exception) {


            res = 2
        }

        return res
    }


    override fun insertUserInfo(pDTO: UserDTO?) : Int {

        // 회원가입 성공 : 1, 아이디 중복으로인한 가입 취소 : 2, 기타 에러 발생 : 0
        val res : Int

        // controller에서 값이 정상적으로 못 넘어오는 경우를 대비하기 위해 사용함
        if (pDTO?.userId?.let { userRepository.findByUserId(it)?.isPresent } != true|| findEmail(pDTO) != true) {
            val userEntity = UserEntity()

            userEntity.password = pDTO?.password
            userEntity.email = pDTO?.email
            userEntity.userId = pDTO?.userId
            userEntity.userName = pDTO?.userName
            userEntity.regDt = DateUtil.getDateTime("yyyy-MM-dd hh:mm:ss")
            userEntity.chgDt = DateUtil.getDateTime("yyyy-MM-dd hh:mm:ss")

            userRepository.save(userEntity)
            println(pDTO?.email)

            val mDTO = MailDTO()
            println(EncryptUtil.decAES128CBC(CmmUtil.nvl(pDTO?.email)))
            //회원정보화면에서 입력받은 이메일 변수(아직 암호화되어 넘어오기 때문에 복호화 수행함)
            mDTO.toMail = EncryptUtil.decAES128CBC(CmmUtil.nvl(pDTO?.email))
            mDTO.title = "회원가입을 축하드립니다." //제목

            //메일 내용에 가입자 이름넣어서 내용 발송
            mDTO.contents = CmmUtil.nvl(pDTO?.userName) + "님의 회원가입을 진심으로 축하드립니다."

            //회원 가입이 성공했기 때문에 메일을 발송함
            mailService.doSendMail(mDTO)
            res = 1
        }else{
            res = 2
        }

        return res
    }

    /**
     * 로그인을 위해 아이디와 비밀번호가 일치하는지 확인하기
     *
     * @param pDTO 로그인을 위한 회원아이디, 비밀번호
     * @return 로그인된 회원아이디 정보
     */
    @Throws(Exception::class)
    override fun getUserLoginCheck(pDTO: UserDTO?): Optional<UserEntity>? {
        return pDTO?.userId?.let { pDTO.password?.let { it1 -> userRepository.findByUserIdAndPassword(it, it1) } }
    }
}
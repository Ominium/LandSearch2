package com.example.landsearch.util

import org.apache.tomcat.util.codec.binary.Base64
import java.io.UnsupportedEncodingException
import java.security.InvalidAlgorithmParameterException
import java.security.InvalidKeyException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.security.spec.AlgorithmParameterSpec
import javax.crypto.BadPaddingException
import javax.crypto.Cipher
import javax.crypto.IllegalBlockSizeException
import javax.crypto.NoSuchPaddingException
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object EncryptUtil {
    /*
     * 암호화 알고리즘에 추가시킬 암호화 문구
     *
     *
     * 일반적인 암호화 알고리즘  SHA-256을 통해서만 암호화 시킬 경우, 암호화 된 값만 보고 일반적인 비밀번호에 대한 값을 쉽게
     * 예측이 가능함 따라서, 암호화할 때 암호화되는 값에 추가적인 문자열을 붙여서 함께 암호화를 진행함
     */
    const val addMessage = "PolyDataAnalysis" // 임의 값

    /*
     * AES128-CBC 암호화 알고리즘에 사용되는 초기 백터와 암호화 키
     */
    //초기 백터(16바이트 크기를 가지며, 16바이트 단위로 암호화시, 암호화할 총 길이가 16바이트가 되지 못하면 뒤에 추가하는 바이트)
    private val ivBytes = byteArrayOf(
        0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
        0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00
    )

    //AES128-CBC 암호화 알고리즘에 사용되는 키 (16자리 문자만 가능함)
    const val key = "PolyTechnic12345" //16글자(영문자 1글자당 1바이트임)

    /**
     * 해시 알고리즘(단방향 암호화 알고리즘)-SHA-256
     *
     * @param str 암호화 시킬 값
     * @return 암호화된 값
     */
    @Throws(Exception::class)
    fun encHashSHA256(str: String): String {
        var res = "" // 암호화 결과괎이 저장되는 변수
        val plantText = addMessage + str // 암호화 시킬 값에 보안강화를 위해 임의 값을 추가함
        res = try {

            // 자바는 기본적으로 표준 암호화 알고리즘을 java.security 패키지를 통해 제공함
            // 여러 해시 알고리즘 중 가장 많이 사용되는 SHA-256를 지원하고 있음
            val sh = MessageDigest.getInstance("SHA-256")
            sh.update(plantText.toByteArray())
            val byteData = sh.digest()
            val sb = StringBuffer()
            for (i in byteData.indices) {
                sb.append(((byteData[i].toInt() and 0xff) + 0x100).toString(16).substring(1))
            }
            sb.toString()

            // 자바에서 제공하지 알고리즘이 아닌 경우, 에러 발생
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
            ""
        }
        return res
    }

    /**
     * AES128 CBC 암호화 함수
     *
     * 128은 암호화 키 길이를 의미함 128비트는 = 16바이트(1바이트=8비트 * 16 = 128)
     */
    @Throws(
        UnsupportedEncodingException::class,
        NoSuchAlgorithmException::class,
        NoSuchPaddingException::class,
        InvalidKeyException::class,
        InvalidAlgorithmParameterException::class,
        IllegalBlockSizeException::class,
        BadPaddingException::class
    )
    fun encAES128CBC(str: String): String {
        val textBytes = str.toByteArray(charset("UTF-8"))
        val ivSpec: AlgorithmParameterSpec = IvParameterSpec(ivBytes)
        val newKey = SecretKeySpec(key.toByteArray(charset("UTF-8")), "AES")
        var cipher: Cipher? = null
        cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        cipher.init(Cipher.ENCRYPT_MODE, newKey, ivSpec)
        return Base64.encodeBase64String(cipher.doFinal(textBytes))
    }

    /**
     * AES128 CBC 복호화 함수
     *
     * 128은 암호화 키 길이를 의미함 128비트는 = 16바이트(1바이트=8비트 * 16 = 128)
     */
    @Throws(
        UnsupportedEncodingException::class,
        NoSuchAlgorithmException::class,
        NoSuchPaddingException::class,
        InvalidKeyException::class,
        InvalidAlgorithmParameterException::class,
        IllegalBlockSizeException::class,
        BadPaddingException::class
    )
    fun decAES128CBC(str: String?): String {
        val textBytes = Base64.decodeBase64(str)
        // byte[] textBytes = str.getBytes("UTF-8");
        val ivSpec: AlgorithmParameterSpec = IvParameterSpec(ivBytes)
        val newKey = SecretKeySpec(key.toByteArray(charset("UTF-8")), "AES")
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        cipher.init(Cipher.DECRYPT_MODE, newKey, ivSpec)

        return String(cipher.doFinal(textBytes))
    }
}
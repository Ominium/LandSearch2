package com.example.landsearch.util

import com.example.landsearch.util.EncryptUtil.key
import org.springframework.lang.Nullable
import java.io.*
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

object NetworkUtil {
    /**
     * GET 방식으로 OpenAPI 호출하기
     * 네이버 API 전송 소스 참고하여 구현
     *
     * @param apiUrl         호출할 OpenAPI URL 주소
     * @param requestHeaders 전송하고 싶은 해더 정보
     */
    /**
     * GET 방식으로 OpenAPI 호출하기(전송할 헤더값이 존재하지 않는 경우 사용)
     * 네이버 API 전송 소스 참고하여 구현
     *
     * @param apiUrl 호출할 OpenAPI URL 주소
     */
    @JvmOverloads
    operator fun get(apiUrl: String, @Nullable requestHeaders: Map<String?, String?>? = null): String {
        val con = connect(apiUrl)
        return try {
            con.requestMethod = "GET"

            // 전송할 헤더 값이 존재하면, 해더 값 추가하기
            if (requestHeaders != null) {
                for (entry in requestHeaders.entries) {
                    con.setRequestProperty(entry.key, entry.value)
                }
            }

            // API 호출 후, 결과 받기
            val responseCode = con.responseCode

            // API 호출이 성공하면..
            if (responseCode == HttpURLConnection.HTTP_OK) {
                readBody(con.inputStream) // 성공 결과 값을 문자열로 변환하기
            } else { // 에러 발생
                readBody(con.errorStream) // 실패 결과 값을 문자열로 변환하기
            }
        } catch (e: IOException) {
            throw RuntimeException("API 요청과 응답 실패", e)
        } finally {
            con.disconnect()
        }
    }

    /**
     * POST 방식으로 OpenAPI 호출하기
     * 네이버 API 전송 소스 참고하여 구현
     *
     * @param apiUrl         호출할 OpenAPI URL 주소
     * @param postParams     전송할 파라미터
     * @param requestHeaders 전송하고 싶은 해더 정보
     */
    fun post(apiUrl: String, @Nullable requestHeaders: Map<String?, String?>, postParams: String): String {
        val con = connect(apiUrl)

        return try {
            con.requestMethod = "POST"

            // 전송할 헤더 값이 존재하면, 해더 값 추가하기
            for (entry in requestHeaders.entries) {
                con.setRequestProperty(entry.key,entry.value)

            }
            // POST 방식으로 전송할때, 전송할 파라미터 정보 넣기(GET 방식은 필요없음)
            con.doOutput = true
            DataOutputStream(con.outputStream).use { wr ->
                wr.write(postParams.toByteArray())
                wr.flush()
            }

            // API 호출 후, 결과 받기
            val responseCode = con.responseCode

            // API 호출이 성공하면..
            if (responseCode == HttpURLConnection.HTTP_OK) {
                readBody(con.inputStream) // 성공 결과 값을 문자열로 변환하기
            } else { // 에러 발생
                readBody(con.errorStream) // 실패 결과 값을 문자열로 변환하기
            }
        } catch (e: IOException) {
            throw RuntimeException("API 요청과 응답 실패", e)
        } finally {
            con.disconnect()
        }
    }

    /**
     * OpenAPI URL에 접속하기
     * 이 함수는 NetworkUtil에서만 사용하기에 접근 제한자를 private으로 선언함
     * 외부 자바 파일에서 호출 불가
     *
     * @param apiUrl 호출할 OpenAPI URL 주소
     */
    private fun connect(apiUrl: String): HttpURLConnection {
        return try {
            val url = URL(apiUrl)
            url.openConnection() as HttpURLConnection
        } catch (e: MalformedURLException) {
            throw RuntimeException("API URL이 잘못되었습니다. : $apiUrl", e)
        } catch (e: IOException) {
            throw RuntimeException("연결이 실패했습니다. : $apiUrl", e)
        }
    }

    /**
     * OpenAPI 호출 후, 받은 결과를 문자열로 변환하기
     * 이 함수는 NetworkUtil에서만 사용하기에 접근 제한자를 private으로 선언함
     * 외부 자바 파일에서 호출 불가
     *
     * @param body 읽은 결과값
     */
    private fun readBody(body: InputStream): String {
        val streamReader = InputStreamReader(body)
        try {
            BufferedReader(streamReader).use { lineReader ->
                val responseBody = StringBuilder()
                var line: String?
                while (lineReader.readLine().also { line = it } != null) {
                    responseBody.append(line)
                }
                return responseBody.toString()
            }
        } catch (e: IOException) {
            throw RuntimeException("API 응답을 읽는데 실패했습니다.", e)
        }
    }
}
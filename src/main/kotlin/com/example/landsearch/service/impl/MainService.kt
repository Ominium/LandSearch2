package com.example.landsearch.service.impl

import com.example.landsearch.dto.AddressDTO
import com.example.landsearch.dto.CategoryDTO
import com.example.landsearch.dto.DiagDTO
import com.example.landsearch.repository.maria.CategoryRepository
import com.example.landsearch.service.IMainService
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.convertValue
import lombok.RequiredArgsConstructor
import lombok.extern.slf4j.Slf4j
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.json.JSONException
import org.json.XML
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Service
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder


@Slf4j
@RequiredArgsConstructor
@Service("MainService")
class MainService (
    //github private key
    var serviceKey: String = "",
    private var categoryRepository: CategoryRepository
) : IMainService{

    @Throws(IOException::class)

    override fun urlBuilder(url: String?): StringBuilder {
        val urlbuilder = StringBuilder(url)
        urlbuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + serviceKey)
        return urlbuilder
    }
    override fun callLandApi(urlbuilder: StringBuilder,key:String): StringBuilder{
        urlbuilder.append("&pageNo=1&numOfRows=10&divId=adongCd&key=11110540&type=xml")

        return urlbuilder
    }
    override fun callLawApi(urlbuilder: StringBuilder,category:String): StringBuilder{
        println(category)
        urlbuilder.append("&pageNo=1&numOfRows=10")
        urlbuilder.append("&" + URLEncoder.encode("reglFcltsNm","UTF-8") + "=" + URLEncoder.encode(category, "UTF-8"))
        println(urlbuilder)
        return urlbuilder
    }
    override fun getCategory(): List<CategoryDTO>?{
        println("get category 들어옴")
        return ObjectMapper().convertValue(categoryRepository.findAll())
    }
    override fun callAroundApi(urlbuilder: StringBuilder, lon: String, lat: String): StringBuilder {
        urlbuilder.append("&radius=2000&cx=$lon&cy=$lat&type=xml")


        return urlbuilder
    }

    @Throws(IOException::class)
    override fun callApi(urlbuilder: StringBuilder): String? {

        val url = URL(urlbuilder.toString())
        val conn = url.openConnection() as HttpURLConnection
        conn.requestMethod = "GET"
        conn.setRequestProperty("Content-type", "application/json")
        println("Response code: " + conn.responseCode)
        val rd: BufferedReader = if (conn.responseCode in 200..300) {
            BufferedReader(InputStreamReader(conn.inputStream))
        } else {
            BufferedReader(InputStreamReader(conn.errorStream))
        }
        val sb = java.lang.StringBuilder()
        var line: String?
        while (rd.readLine().also { line = it } != null) {
            sb.append(line)
        }
        rd.close()
        conn.disconnect()
        println("sb : $sb")
        return xmltoJson(sb.toString())

    }
    override fun getDiagList(tag:String, name:String):List<DiagDTO>?{
        val dList = mutableListOf<DiagDTO>()

        val resource = ClassPathResource("/static/excel/diagnosis.xlsx")

        val file = resource.inputStream
        val workbook: Workbook = XSSFWorkbook(file)
        val worksheet : Sheet = workbook.getSheetAt(0)



        for(i in 1 until worksheet.physicalNumberOfRows){
            val row : Row = worksheet.getRow(i)
            val dDTO = DiagDTO()
            if(tag == "name"){
                if(row.getCell(0).stringCellValue.contains(name)){
                    dDTO.name = row.getCell(0).stringCellValue
                    dDTO.address = row.getCell(1).stringCellValue
                    dDTO.rank = row.getCell(3).stringCellValue
                    dList.add(dDTO)
                }
            }else{
                if(row.getCell(1).stringCellValue.contains(name)){
                    dDTO.name = row.getCell(0).stringCellValue
                    dDTO.address = row.getCell(1).stringCellValue
                    dDTO.rank = row.getCell(3).stringCellValue
                    dList.add(dDTO)
                }
            }


        }
        return dList
    }

    override fun getAddressList(mainAddress : String): List<AddressDTO>?{
        val aList = mutableListOf<AddressDTO>()




        val resource = ClassPathResource("/static/excel/code.xlsx")
        val file = resource.inputStream

        val workbook: Workbook = XSSFWorkbook(file)
        val worksheet : Sheet = workbook.getSheetAt(0)



        for(i in 1 until worksheet.physicalNumberOfRows){
            val row : Row = worksheet.getRow(i)
            val aDTO = AddressDTO()
            if(row.getCell(1).stringCellValue==mainAddress){
                aDTO.code = row.getCell(0).numericCellValue.toInt().toString()
                aDTO.address1 = row.getCell(1).stringCellValue
                aDTO.address2 = row.getCell(2).stringCellValue
                aList.add(aDTO)
            }

        }


        return aList
    }

    @Throws(JSONException::class)
    fun xmltoJson(xml: String?): String? {
        var json = ""
        try {
            val jsonObj: org.json.JSONObject? = XML.toJSONObject(xml)
            json = jsonObj.toString()
            println("json = $json")
        } catch (ex: JSONException) {
            ex.printStackTrace()
        }
        return json
    }
}
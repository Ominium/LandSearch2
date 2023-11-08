package com.example.landsearch.controller

import com.example.landsearch.dto.AddressDTO
import com.example.landsearch.dto.CategoryDTO
import com.example.landsearch.dto.DiagDTO
import com.example.landsearch.main
import com.example.landsearch.service.IMainService
import com.fasterxml.jackson.databind.ObjectMapper
import lombok.extern.slf4j.Slf4j
import org.json.JSONArray
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.xml.sax.SAXException
import java.io.IOException
import java.net.URLEncoder
import java.text.ParseException
import javax.servlet.http.HttpSession
import javax.xml.parsers.ParserConfigurationException

@Controller
@Slf4j
@RequestMapping("/main")
class MainController (
    private val mainService: IMainService,
    @Value("https://apis.data.go.kr/B553077/api/open/sdsc2/storeListInDong")
    private val landkey : String? = null,
    @Value("https://apis.data.go.kr/B553077/api/open/sdsc2/storeZoneInRadius")
    private val aroundkey : String? = null,
    @Value("http://apis.data.go.kr/1611000/LuReglGuidanceService/attr/getLuIndproAttrList")
    private val lawkey : String? =null,
    @Value("https://apis.data.go.kr/B553077/api/open/sdsc2/middleUpjongList")
    private val subcategorykey : String?=null,
    @Value("https://apis.data.go.kr/B553077/api/open/sdsc2/storeListInUpjong")
    private val categorySearchkey : String?=null,
    @Value("https://apis.data.go.kr/B553077/api/open/sdsc2/storeListInArea")
    private val mapCategorySearchkey : String?=null,
    @Value("http://openapi.molit.go.kr/OpenAPI_ToolInstallPackage/service/rest/RTMSOBJSvc/getRTMSDataSvcNrgTrade")
    private val addressSearchkey : String?=null
){
    @GetMapping("/diagSearch")
    @ResponseBody
    fun diagSearch(tag:String,value:String):Any?{
        val aList : List<DiagDTO>? = mainService.getDiagList(tag,value);

        val objectMapper = ObjectMapper()
        val jsonArray = JSONArray()
        if (aList != null) {
            for (dDTO in aList) {
                jsonArray.put(objectMapper.writeValueAsString(dDTO))
            }
        }
        return jsonArray.toString()
    }
    @GetMapping("/addressLoad")
    @ResponseBody
    fun addressLoad(mainAddress : String):Any?{
        val aList : List<AddressDTO>? = mainService.getAddressList(mainAddress);

        val objectMapper = ObjectMapper()
        val jsonArray = JSONArray()
        if (aList != null) {
            for (aDTO in aList) {
                jsonArray.put(objectMapper.writeValueAsString(aDTO))
            }
        }
        return jsonArray.toString()
    }
    @GetMapping("/addressSearch")
    @ResponseBody
    fun addressSearch(address : String): String?{
        val urlbuilder = mainService.urlBuilder(addressSearchkey)

        urlbuilder.append("&" + URLEncoder.encode("LAWD_CD", "UTF-8") + "=" + URLEncoder.encode(address, "UTF-8"))
        urlbuilder.append("&" + URLEncoder.encode("DEAL_YMD", "UTF-8") + "=" + URLEncoder.encode("201512", "UTF-8"))
        return mainService.callApi(urlbuilder)

    }



    @GetMapping("/categoryLoad")
    @ResponseBody
    fun categoryLoad(indsLclsCd : String):String?{
        println("indsLclsCd = $indsLclsCd")
        val urlbuilder = mainService.urlBuilder(subcategorykey)

        urlbuilder.append("&" + URLEncoder.encode("indsLclsCd", "UTF-8") + "=" + URLEncoder.encode(indsLclsCd, "UTF-8"))
        println(urlbuilder)
        return mainService.callApi(urlbuilder)
    }
    @GetMapping("/categorySearch")
    @ResponseBody
    fun categorySearch(indsMclsCd :String):String?{
        val urlbuilder = mainService.urlBuilder(categorySearchkey)
        urlbuilder.append("&" + URLEncoder.encode("divId", "UTF-8") + "=" + URLEncoder.encode("indsMclsCd", "UTF-8"))
        urlbuilder.append("&" + URLEncoder.encode("key", "UTF-8") + "=" + URLEncoder.encode(indsMclsCd, "UTF-8"))
        println(urlbuilder)
        return mainService.callApi(urlbuilder)
    }
    @GetMapping("/categorySearchPage")
    @ResponseBody
    fun categorySearchPage(indsMclsCd :String,num:String):String?{
        val urlbuilder = mainService.urlBuilder(categorySearchkey)
        urlbuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode(num, "UTF-8"))
        urlbuilder.append("&" + URLEncoder.encode("divId", "UTF-8") + "=" + URLEncoder.encode("indsMclsCd", "UTF-8"))
        urlbuilder.append("&" + URLEncoder.encode("key", "UTF-8") + "=" + URLEncoder.encode(indsMclsCd, "UTF-8"))
        println(urlbuilder)
        return mainService.callApi(urlbuilder)
    }
    @GetMapping("/mapCategorySearch")
    @ResponseBody
    fun mapCategorySearch(key : String, indsMclsCd :String):String?{
        val urlbuilder = mainService.urlBuilder(mapCategorySearchkey)

        urlbuilder.append("&" + URLEncoder.encode("key", "UTF-8") + "=" + URLEncoder.encode(key, "UTF-8"))
        urlbuilder.append("&" + URLEncoder.encode("indsMclsCd", "UTF-8") + "=" + URLEncoder.encode(indsMclsCd, "UTF-8"))
        println(urlbuilder)
        return mainService.callApi(urlbuilder)
    }
    @GetMapping("/category")
    @ResponseBody
    fun category():List<CategoryDTO>?{
        println("category 들어옴")
       return mainService.getCategory();
    }
    @GetMapping("/mainPage")
    fun mainPage(session: HttpSession): String? {
        return "main/mainPage"
    }
    @GetMapping("/mainDiagSearch")
    fun mainDiagSearch(session: HttpSession): String? {
        return "main/mainDiagSearch"
    }
    @GetMapping("/mainQnA")
    fun mainQnA(session: HttpSession): String? {
        return "main/mainQnA"
    }

    @GetMapping("/mainQnAPage")
    @ResponseBody
    fun mainQnAPage(category: String,num : String):String?{
        val urlbuilder = mainService.urlBuilder(lawkey)
        println(category)
        urlbuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode(num, "UTF-8"))
        urlbuilder.append("&" + URLEncoder.encode("reglFcltsNm", "UTF-8") + "=" + URLEncoder.encode(category, "UTF-8"))
        return mainService.callApi(urlbuilder)
    }
    @GetMapping("/mainLandSearch")
    fun mainLandSearch(): String?{
        return "main/mainLandSearch"
    }
    @PostMapping("/mainQnASearch")
    @ResponseBody
    fun mainQnASearch(category: String):String?{
        var urlbuilder = mainService.urlBuilder(lawkey)
        urlbuilder = mainService.callLawApi(urlbuilder,category)
        return mainService.callApi(urlbuilder)
    }

    @GetMapping(value = ["landapi"])
    @ResponseBody
    @Throws(
        IOException::class,
        ParseException::class,
        ParserConfigurationException::class,
        SAXException::class
    )
    fun callLandApi(key : String): String? {

        var urlbuilder = mainService.urlBuilder(landkey)
        urlbuilder = mainService.callLandApi(urlbuilder,key)
        return mainService.callApi(urlbuilder)
    }
    @GetMapping(value=["aroundapi"])
    @ResponseBody
    fun callAroundApi(lon: String,lat : String): String? {

        println(lon)
        println(lat)
        var urlbuilder = mainService.urlBuilder(aroundkey)
        urlbuilder = mainService.callAroundApi(urlbuilder,lon,lat)
        return mainService.callApi(urlbuilder)
    }
}
package com.example.landsearch.service

import com.example.landsearch.dto.AddressDTO
import com.example.landsearch.dto.CategoryDTO
import com.example.landsearch.dto.DiagDTO
import com.sun.javafx.fxml.builder.URLBuilder

interface IMainService {
    fun urlBuilder(url: String?): StringBuilder
    fun getCategory(): List<CategoryDTO>?
    fun callApi(urlbuilder :StringBuilder) : String?

    fun callLandApi(urlbuilder: StringBuilder,key:String): StringBuilder
    fun callLawApi(urlbuilder: StringBuilder,category:String): StringBuilder
    fun callAroundApi(urlbuilder : StringBuilder,lon : String,lat : String):StringBuilder

    fun getAddressList(mainAddress : String): List<AddressDTO>?
    fun getDiagList(tag:String,name:String):List<DiagDTO>?
}
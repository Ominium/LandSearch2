package com.example.landsearch.dto

import lombok.Data


@Data
class PageDTO(var num: Int, count: Int) {
      var start: Int
     val finish = 0
      val pageNum_cnt = 10
       var endPageNum = 0

    // 표시되는 페이지 번호 중 첫번째 번호
    var startPageNum = 0
    val endPageNum_tmp: Int
    val prev: Boolean
    val next: Boolean

    init {
        start = (num - 1) * 10
        endPageNum = Math.ceil(num.toDouble() / pageNum_cnt.toDouble()).toInt() * pageNum_cnt
        startPageNum = endPageNum - (pageNum_cnt - 1)
        endPageNum_tmp = endpage(count)
        if (endPageNum > endPageNum_tmp) {
            endPageNum = endPageNum_tmp
        }
        if (start <= 1) start = 0
        prev = num != 1
        next = num * pageNum_cnt < count
    }

    fun endpage(count: Int): Int {
        return Math.ceil(count.toDouble() / pageNum_cnt.toDouble()).toInt()
    }
}
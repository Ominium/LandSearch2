package com.example.landsearch.util

object CmmUtil {
    /*public static String[] nvls(String[] str,String chg_str){
        String[] res;
        if(str == null){

        }
    }*/
    @JvmOverloads
    fun nvl(str: String?, chgStr: String = ""): String {
        val res: String = if (str == null) {
            chgStr
        } else if (str == "") {
            chgStr
        } else {
            str
        }
        return res
    }

    fun checked(str: String, comStr: String): String {
        return if (str == comStr) {
            " checked"
        } else {
            ""
        }
    }

    fun checked(str: Array<String>, comStr: String): String {
        for (i in str.indices) {
            if (str[i] == comStr) return " checked"
        }
        return ""
    }

    fun select(str: String, comStr: String): String {
        return if (str == comStr) {
            " selected"
        } else {
            ""
        }
    }
}
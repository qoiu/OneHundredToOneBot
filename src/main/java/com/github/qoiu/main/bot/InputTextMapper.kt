package com.github.qoiu.main.bot

import main.java.com.github.qoiu.main.Mapper

class InputTextMapper: Mapper<String,String> {
    override fun map(data: String): String = when(data){
            "hi"-> "I'm also glad to see u"
        else->"I don't get it"

    }
}
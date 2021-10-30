package com.github.qoiu.main.bot

import com.github.qoiu.main.Mapper

class InputTextMapper: Mapper<String, String> {

    private val values: HashMap<String,String> = hashMapOf(
            Pair("hi","I'm also glad to see u"),
            Pair("","I don't get it")
    )
    override fun map(data: String): String = values[data.toLowerCase()] ?:"I don't get it"
}
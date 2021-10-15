package main.java.com.github.qoiu.main

interface Mapper<T,S> {
    fun map(data: T): S
}
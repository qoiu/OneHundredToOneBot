package com.github.qoiu.main.data.mappers;

import com.github.qoiu.main.data.DatabaseBase;

public interface DbMapper<Output, Input> {

        Output map(Input object);


    abstract class Base<Output, Input> implements DbMapper<Output, Input>{
        DatabaseBase db;
        public Base(DatabaseBase db) {
            this.db = db;
        }
    }
}
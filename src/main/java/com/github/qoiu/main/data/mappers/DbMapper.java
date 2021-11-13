package com.github.qoiu.main.data.mappers;

import com.github.qoiu.main.data.DatabaseInterface;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface DbMapper<Output, Input> {

        Output map(Input object);


    abstract class Base<Output, Input> implements DbMapper<Output, Input>{
        DatabaseInterface.Executor db;
        public Base(DatabaseInterface.Executor db) {
            this.db = db;
        }
    }

    abstract class Result<Output> implements DbMapper<Output, ResultSet>{
        String sql;

        Logger log = LogManager.getLogger();

        public Result(String sql) {
            this.sql = sql;
        }

        Output exception(SQLException e){
            if(sql!=null) log.error("Wrong result for request: "+sql);
            log.error(e);
            return null;
        }
    }
}
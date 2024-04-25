package com.example.coolweather.util;

import org.litepal.crud.DataSupport;

public class DataException extends RuntimeException {
    DataException(String msg){
        super(msg);
    }
}

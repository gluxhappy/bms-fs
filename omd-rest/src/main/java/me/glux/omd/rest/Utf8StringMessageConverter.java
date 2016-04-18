package me.glux.omd.rest;

import java.nio.charset.Charset;

import org.springframework.http.converter.StringHttpMessageConverter;

public class Utf8StringMessageConverter extends StringHttpMessageConverter
{
    public Utf8StringMessageConverter(){
        super(Charset.forName("utf-8"));
    }
}

package com.dataan.utils;

import com.dataan.vo.Pageable;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * @author zhan bing liang
 * @date 2024/6/4 15:53
 */
public class Response {

    private Response(){

    }

    public static <T> ResponseEntity<T> ok(T data) {
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    public static <T> ResponseEntity<Pageable<T>> ok(List<T> data, int total, int page, int size) {
        Pageable<T> pageable = new Pageable<>((List)data, total, page, size);
        return ok(pageable);
    }

    public static <T> ResponseEntity<T> created(T data) {
        return new ResponseEntity<>(data, HttpStatus.CREATED);
    }

    public static <T> ResponseEntity<T> noContent() {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}

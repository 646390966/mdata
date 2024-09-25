package com.dataan.vo;

import com.dataan.utils.JsonUtil;
import java.time.LocalDateTime;

/**
 * @author zhan bing liang
 * @date 2024/6/5 16:09
 */
public class ErrorVo {
    private Integer status;
    private String message;
    private LocalDateTime timestamp;
    private String path;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ErrorVo(Integer status, String message, LocalDateTime timestamp, String path) {
        this.status = status;
        this.message = message;
        this.timestamp = timestamp;
        this.path = path;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }


    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String json() {
        return JsonUtil.writeValueAsString(this);
    }
}

package com.dataan.vo;

import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author zhan bing liang
 * @date 2024/6/28 13:25
 */
public class UserRequestVo {


    @NotEmpty(message = "账号不能为空")
    @Size(max=10,message = "账号长度不应超过10")
    private String username;
    @NotEmpty(message = "密码不能为空")
    private String password;
    private List<String> role;
    private MultipartFile file;
    private Boolean remember;

    public Boolean getRemember() {
        return remember;
    }

    public void setRemember(Boolean remember) {
        this.remember = remember;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getRole() {
        return role;
    }

    public void setRole(List<String> role) {
        this.role = role;
    }
}

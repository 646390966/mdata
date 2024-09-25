package com.dataan.controller;


import com.dataan.annotation.SqlArgument;
import com.dataan.entity.User;
import com.dataan.service.UserDetailsService;
import com.dataan.utils.MyBeanUtils;
import com.dataan.utils.Response;
import com.dataan.vo.Pageable;
import com.dataan.vo.UserRequestVo;
import com.dataan.vo.UserVo;
import java.io.IOException;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

/**
 * @author zhan bing liang
 * @date 2024/3/15 9:32
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @PostMapping(value = "/login")
    public ResponseEntity<String> login(@RequestBody @Valid UserRequestVo user) {


        String token = userDetailsService.getToken(user.getUsername(), user.getPassword(),user.getRemember());
        return Response.created(token);
    }

    @PostMapping(consumes = "multipart/form-data",produces = "application/json;charset=utf-8")
    public ResponseEntity<UserVo> createUser(@Valid UserRequestVo userRequestVo) throws IOException {
        User user = MyBeanUtils.copyProperties(userRequestVo, User::new);
        if (userRequestVo.getFile()!=null&&!userRequestVo.getFile().isEmpty()) {
            String avator = Base64.getEncoder().encodeToString(userRequestVo.getFile().getBytes());
            user.setAvator(avator);
        }
        User res = userDetailsService.createUser(user);
        return Response.created(MyBeanUtils.copyProperties(res, UserVo::new));
    }

    @PutMapping(value = "/{id}",consumes = "multipart/form-data",produces = "application/json;charset=utf-8")
    public ResponseEntity<Void> updateUser(@PathVariable String id,@Valid UserRequestVo userRequestVo) throws IOException {
        User user = MyBeanUtils.copyProperties(userRequestVo, User::new);
        if (userRequestVo.getFile()!=null&&!userRequestVo.getFile().isEmpty()) {
            String avator = Base64.getEncoder().encodeToString(userRequestVo.getFile().getBytes());
            user.setAvator(avator);
        }
        user.setId(id);
        userDetailsService.updateUser(user);
        return Response.<Void>noContent();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        userDetailsService.deleteUser(id);
        return Response.<Void>noContent();
    }

    @GetMapping("/{id}/detail")
    public ResponseEntity<UserVo> getUserById(@PathVariable String id) {
        User user = userDetailsService.getUserById(id);
        return Response.ok(MyBeanUtils.copyProperties(user, UserVo::new));
    }
    @GetMapping("/token")
    public ResponseEntity<UserVo> getUserByToken() {
        User user = userDetailsService.getCurrentUser();
        return Response.ok(MyBeanUtils.copyProperties(user, UserVo::new));
    }
    @GetMapping("/all")
    public ResponseEntity<Pageable<UserVo>> getUserList(@RequestParam(required = false,defaultValue = "1")Integer page
        , @RequestParam(required = false,defaultValue = "10")Integer size
        , @RequestParam(required = false)String username
        , @RequestParam(required = false)@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") Date createTimeStart
        , @RequestParam(required = false)@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") Date createTimeEnd
        , @SqlArgument @RequestParam(required = false,defaultValue = "create_time")  String orderColumn
                                                        , @RequestParam(required = false,defaultValue = "true")Boolean desc
        ) {
        List<User> users = userDetailsService.getUserList(page,size,username,createTimeStart,createTimeEnd,orderColumn,desc);
        return Response.ok(new Pageable<>(MyBeanUtils.copyListProperties(users,UserVo::new),userDetailsService.getTotalCount(),page,size));
    }

}


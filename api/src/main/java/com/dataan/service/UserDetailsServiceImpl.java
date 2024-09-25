package com.dataan.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.dataan.entity.User;
import com.dataan.mapper.UserMapper;
import com.dataan.utils.DataInformation;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author zhan bing liang
 * @date 2024/6/4 10:09
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    @Lazy
    private AuthenticationManager authenticationManager;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return getUserByUsername(username);
    }
    @Override
    public User createUser(User user) {
        String encodePassword = passwordEncoder.encode(new String(Base64.getDecoder().decode(user.getPassword())));
        user.setPassword(encodePassword);
        user.setId(UUID.randomUUID().toString().replace("-", ""));
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        userMapper.createUser(user);
        return user;
    }
    @Override
    public void updateUser(User user) {
        String encodePassword = passwordEncoder.encode(new String(Base64.getDecoder().decode(user.getPassword())));
        user.setPassword(encodePassword);
        user.setUpdateTime(LocalDateTime.now());
        userMapper.updateUser(user);
    }
    @Override
    public void deleteUser(String id) {
        userMapper.deleteUser(id);
    }

    @Override
    public User getUserById(String id) {
        return Optional.ofNullable(userMapper.getUserById(id)).orElseThrow(()->new IllegalArgumentException(MessageFormat.format("id={0}找不到指定用户",id)));
    }
    @Override
    public User getUserByUsername(String username) {
       return Optional.ofNullable(userMapper.getUserByUsername(username)).orElseThrow(()->new IllegalArgumentException(MessageFormat.format("username={0}找不到指定用户",username)));
    }
    @Override
    public List<User> getUserList(Integer page, Integer size, String username, Date createTimeStart, Date createTimeEnd, String orderColumn, Boolean desc) {
        return userMapper.getUserList(new RowBounds((page - 1) * size, size),username,createTimeStart,createTimeEnd,orderColumn,desc);
    }
    @Override
    public User getCurrentUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication instanceof UsernamePasswordAuthenticationToken) {
            String username = authentication.getPrincipal().toString();
            return getUserByUsername(username);
        }
        throw new AuthenticationCredentialsNotFoundException("无法获取当前用户");

    }
    @Override
    public String getToken(String username, String password, Boolean rememberMe) {
        UsernamePasswordAuthenticationToken authentication
            = new UsernamePasswordAuthenticationToken(username, new String(Base64.getDecoder().decode(password)));


        Authentication authenticate = authenticationManager.authenticate(authentication);

        //获取jwt生成器
        JWTCreator.Builder jwtBuilder = JWT.create();
        //由于该生成器设置Header的参数为一个<String, Object>的Map,
        //所以我们提前准备好
        Map<String, Object> headers = new HashMap<>(2);
        //设置token的type为jwt
        headers.put("typ", "jwt");
        //表明加密的算法为HS256
        headers.put("alg", "hs256");
        //开始生成token
        //我们将之前准备好的header设置进去
        User principal = (User)authenticate.getPrincipal();
        int delayDay = Boolean.TRUE.equals(rememberMe)?14:1;
        return jwtBuilder.withHeader(headers)

            //接下来为设置PayLoad,Claim中的键值对可自定义
            //设置用户名
            .withClaim("userName", principal.getUsername())
            //token失效时间，这里为一天后失效
            .withExpiresAt(Date.from(LocalDateTime.now().plusDays(delayDay).atZone(ZoneId.systemDefault()).toInstant()))
            //设置该jwt的发行时间，一般为当前系统时间
            .withIssuedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()))
            //进行签名，选择加密算法，以一个字符串密钥为参数
            .sign(Algorithm.HMAC256(DataInformation.JWT_KEY));

    }
    @Override
    public Integer getTotalCount() {
        return userMapper.getTotalCount();
    }

}

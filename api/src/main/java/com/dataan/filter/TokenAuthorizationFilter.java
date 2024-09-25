package com.dataan.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.dataan.utils.DataInformation;
import java.text.MessageFormat;
import java.util.Map;
import java.util.function.Supplier;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @author zhan bing liang
 * @date 2024/6/4 8:57
 */
@Component
public class TokenAuthorizationFilter implements AuthorizationManager<RequestAuthorizationContext> {
    private static final Logger LOGGER = LoggerFactory.getLogger(TokenAuthorizationFilter.class);

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext requestAuthorizationContext) {
        //获取request
        HttpServletRequest request = requestAuthorizationContext.getRequest();
        //获取token
        String token = request.getHeader("mdata-token");
        if (!StringUtils.hasText(token)) {
            throw new BadCredentialsException("请传入token");
        }
        try {
            var jwt = JWT.require(Algorithm.HMAC256(DataInformation.JWT_KEY)).build();
        /*开始进行验证，该函数会验证此token是否遭到修改，
            以及是否过期，验证成功会生成一个解码对象
            ，如果token遭到修改或已过期就会
            抛出异常*/
            DecodedJWT decode = jwt.verify(token);
            Map<String, Claim> claims = decode.getClaims();
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(claims.get("userName").asString(),null));
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new BadCredentialsException(MessageFormat.format("token={0}无效", token));
        }
        return new AuthorizationDecision(true);
    }
}

package com.dataan.interceptor;

import com.dataan.utils.JsonUtil;
import com.dataan.vo.ErrorVo;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;

/**
 * @author zhan bing liang
 * @date 2023/5/18 9:11
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgumentException(HttpServletRequest request,HttpServletResponse response, HandlerMethod handler,
                                                   Model model, IllegalArgumentException e) {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        return resolveReturnValue(request,response, handler, model, e,e.getMessage());
    }
    @ExceptionHandler(BadCredentialsException.class)
    public String handleBadCredentialsException(HttpServletRequest request,HttpServletResponse response, HandlerMethod handler,
                                                 Model model, BadCredentialsException e) {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        return resolveReturnValue(request,response, handler, model, e,e.getMessage());
    }
    @ExceptionHandler(value = {BindException.class})
    public String handleMethodArgumentNotValidException(HttpServletRequest request,HttpServletResponse response, HandlerMethod handler,
                                                        Model model, BindException e) {
        BindingResult bindingResult = e.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        int i = 1;
        int len = bindingResult.getFieldErrorCount();
        StringBuilder errorMessage = new StringBuilder();
        for (FieldError fieldError : fieldErrors) {
            errorMessage
//                .append(fieldError.getField())
//                .append(":")
                .append(fieldError.getDefaultMessage());
            if (i != len) {
                errorMessage.append(",");
            }
            i++;
        }

        response.setStatus(HttpStatus.BAD_REQUEST.value());
        return resolveReturnValue(request,response, handler, model, e,errorMessage.toString());
    }

    @ExceptionHandler(Exception.class)
    public String handleException(HttpServletRequest request,HttpServletResponse response, HandlerMethod handler,
                                  Model model, Exception e) {

        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        return resolveReturnValue(request, response, handler, model, e,e.getMessage());
    }

    private String resolveReturnValue(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler, Model model,
         Exception exception,String message) {
        LOGGER.error(exception.getMessage(),exception);
        if (!handler.getMethod().isAnnotationPresent(ResponseBody.class)
            && !handler.getBeanType().isAnnotationPresent(RestController.class)) {
            model.addAttribute("msg", response);
            return "error/" + response.getStatus();
        }
        try {
            if (!response.isCommitted()) {
                response.resetBuffer();
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.setCharacterEncoding(StandardCharsets.UTF_8.name());
                PrintWriter writer = response.getWriter();
                JsonUtil.OBJECT_MAPPER.writeValue(writer, new ErrorVo(response.getStatus(), message, LocalDateTime.now(),request.getRequestURI()));
            } else {
                return exception.getMessage();
            }
        } catch (IllegalStateException | IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }

}

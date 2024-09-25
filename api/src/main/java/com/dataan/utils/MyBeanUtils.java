package com.dataan.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import org.springframework.beans.BeanUtils;

/**
 * @author zhan bing liang
 * @date 2023/5/17 13:54
 */
public class MyBeanUtils extends BeanUtils{

    public static <S, T> List<T> copyListProperties(List<S> sources, Supplier<T> target) {
        return copyListProperties(sources, target, null);
    }

    public static <S, T> T copyProperties(S source, Supplier<T> target) {
        if (source == null) {
            return null;
        }
        T t = target.get();
        copyProperties(source,t);
        return t;
    }
    public static <S, T> List<T> copyListProperties(List<S> sources, Supplier<T> target, MyBeanUtilsCallBack<S, T> callBack) {
        List<T> list = new ArrayList<>(sources.size());
        for (S source : sources) {
            T t = target.get();
            copyProperties(source, t);
            if (callBack != null) {
                // 回调
                callBack.callBack(source, t);
            }
            list.add(t);
        }
        return list;
    }

    @FunctionalInterface
    public interface MyBeanUtilsCallBack<S, T> {
        /**
         * 回调方法
         * @param t
         * @param s
         */
        void callBack(S t, T s);
    }
}

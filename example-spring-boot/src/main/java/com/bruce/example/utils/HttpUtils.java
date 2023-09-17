package com.bruce.example.utils;

import com.alibaba.fastjson2.JSONObject;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * http 工具类 获取请求中的参数
 */
@Slf4j
public class HttpUtils {

    /**
     * 获取请求参数
     */
    public static Map<String, Object> getRequestParameter(HttpServletRequest request) {
        Map<String, Object> paramMap;
        if (request.getContentType() != null && request.getContentType().toLowerCase().contains("application/json")) {
            paramMap = getBodyParams(request);
        } else {
            paramMap = getUrlParams(request);
        }
        return paramMap;
    }

    /**
     * 获取请求体参数
     *
     * @param request
     * @return java.lang.String
     * @author: zoubaolu
     * @date: 2022/9/2 16:49
     */
    public static String getBodyString(ServletRequest request) {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = null;
        String line;
        try {
            reader = new BufferedReader(new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8));
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            throw new RuntimeException("获取请求体参数异常：" + e.getMessage());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    log.error(ExceptionUtils.getMessage(e));
                }
            }
        }
        return sb.toString();
    }

    /**
     * 获取 Body 参数
     *
     * @param request
     */
    public static Map<String, Object> getBodyParams(HttpServletRequest request) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
            String str;
            StringBuilder wholeStr = new StringBuilder();
            while ((str = reader.readLine()) != null) {
                wholeStr.append(str);
            }
            if (StringUtils.isEmpty(wholeStr)) {
                wholeStr.append("{}");
            }
            return JSONObject.parseObject(wholeStr.toString(), Map.class);
        } catch (IOException e) {
            throw new RuntimeException("获取请求体参数异常：" + e.getMessage());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    log.error(ExceptionUtils.getMessage(e));
                }
            }
        }
    }


    /**
     * 将URL请求参数转换成Map
     *
     * @param request
     */
    public static Map<String, Object> getUrlParams(HttpServletRequest request) {
        String param;
        SortedMap<String, Object> result = new TreeMap<>();
        if (StringUtils.isEmpty(request.getQueryString())) {
            return result;
        }
        param = URLDecoder.decode(request.getQueryString(), StandardCharsets.UTF_8);
        String[] params = param.split("&");
        for (String s : params) {
            int index = s.indexOf("=");
            result.put(s.substring(0, index), s.substring(index + 1));
        }
        return result;
    }
}

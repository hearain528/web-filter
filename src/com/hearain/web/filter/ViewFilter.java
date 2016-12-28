package com.hearain.web.filter;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Administrator on 2016/12/7 0007.
 */
public class ViewFilter implements Filter {

    private String excludedPages;
    private String[] excludedPageArray;
    private String ipList;

    protected final Log log = LogFactory.getLog(getClass());


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        excludedPages = filterConfig.getInitParameter("excludedPages");
        ipList = filterConfig.getInitParameter("allowIp");
        if(excludedPages.length() > 0){
            excludedPageArray = excludedPages.split(",");
        }
    }

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        //判断ip
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        PrintWriter pw = null;

        Boolean isExcludePage = false;
        for (String page : excludedPageArray) {//判断是否在过滤url之外
            if(req.getServletPath().equals(page)){
                isExcludePage = true;
                break;
            }
        }

        /////做ip列表限制
        try{
            if(ipList != null &&
                    !ipList.isEmpty()){
                if("*".equals(ipList)){
                    chain.doFilter(request, response);
                }else{
                    String ip = getRemoteIP(req);
                    if(ip != null && !ip.isEmpty() &&
                            !ipList.contains(ip) && !isExcludePage){
                        log.info("您的ip为:" + ip);
                        res.setHeader("Content-type", "text/html;charset=UTF-8");
                        res.setCharacterEncoding("UTF-8");
                        pw = res.getWriter();
                        pw.write("您没有权限");
                    }else{
                        chain.doFilter(request, response);
                    }
                }
            }else{
                chain.doFilter(request, response);
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            if(pw != null){
                pw.close();
            }
        }
    }

    @Override
    public void destroy() {

    }

    /** 描述：获取客户端IP地址*/
    public static String getRemoteIP(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip) || "null".equalsIgnoreCase(ip))    {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)   || "null".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)    || "null".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}

package com.idreamsky.permission.filter;

import com.google.common.collect.Sets;
import com.idreamsky.permission.common.Constant;
import com.idreamsky.permission.common.JsonData;
import com.idreamsky.permission.common.RequestHolder;
import com.idreamsky.permission.model.User;
import com.idreamsky.permission.service.CoreService;
import com.idreamsky.permission.util.JsonMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author: colby
 * @Date: 2018/12/29 21:19
 */
@Slf4j
@Component
public class AclControllerFilter implements Filter {

    private static Set<String> exclusionUrlSet = Sets.newConcurrentHashSet();

    private static String noAuthUrl = "/sys/user/noAuth.html";

    @Resource
    private CoreService coreService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String exclusionUrls = filterConfig.getInitParameter("exclusionUrls");
        if (StringUtils.isNotBlank(exclusionUrls)) {
            List<String> exclusionUrlList = Arrays.stream(exclusionUrls.split(","))
                    .filter(StringUtils::isNotBlank).collect(Collectors.toList());
            exclusionUrlSet = Sets.newConcurrentHashSet(exclusionUrlList);
        }
        String noAuthPage = filterConfig.getInitParameter("noAuthPage");
        if (StringUtils.isNotBlank(noAuthPage)) {
            noAuthUrl = noAuthPage;
        }
        // 防止无权限时死循环
        exclusionUrlSet.add(noAuthUrl);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String servletPath = request.getServletPath();
        Map<String, String[]> parameterMap = request.getParameterMap();

        if (exclusionUrlSet.contains(servletPath)) {
            chain.doFilter(request, response);
            return;
        }

        User user = RequestHolder.getCurrentUser();
        if (user == null) {
            log.info("someone visit {}, but no login, parameter:{}", servletPath, JsonMapper.toJSONString(parameterMap));
            noAuth(request, response);
            return;
        }

        if (!coreService.hasUrlAcl(servletPath)) {
            log.info("{} visit {}, but no permission, parameter:{}", JsonMapper.toJSONString(user), servletPath, JsonMapper.toJSONString(parameterMap));
            noAuth(request, response);
            return;
        }

        chain.doFilter(request, response);
    }

    private void noAuth(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String servletPath = request.getServletPath();
        if (!servletPath.endsWith(Constant.HTTP_PAGE_REQUEST_SUFFIX)) {
            JsonData jsonData = JsonData.fail("没有访问权限，如需要访问，请联系管理员");
            response.setHeader("Content-Type", "application/json");
            response.getWriter().print(JsonMapper.toJSONString(jsonData));
        } else {
            clientRedirect(noAuthUrl,response);
        }
    }

    private void clientRedirect(String url, HttpServletResponse response) throws IOException {
        response.setHeader("Content-Type", "text/html");
        response.getWriter().print("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n"
                + "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" + "<head>\n" + "<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\"/>\n"
                + "<title>跳转中...</title>\n" + "</head>\n" + "<body>\n" + "跳转中，请稍候...\n" + "<script type=\"text/javascript\">//<![CDATA[\n"
                + "window.location.href='" + url + "?ret='+encodeURIComponent(window.location.href);\n" + "//]]></script>\n" + "</body>\n" + "</html>\n");}

    @Override
    public void destroy() {

    }
}

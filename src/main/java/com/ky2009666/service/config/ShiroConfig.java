package com.ky2009666.service.config;

import com.ky2009666.service.realm.CustomRealm;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.Authenticator;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.authz.Authorizer;
import org.apache.shiro.authz.ModularRealmAuthorizer;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author ky2009666
 * @description shiro初始化信息配置
 * @date 2021/4/27
 **/
@Configuration
@Slf4j
public class ShiroConfig {
    /**
     * 自定义realm，
     *
     * @return
     */
    @Bean
    public CustomRealm customRealm() {
        CustomRealm customRealm = new CustomRealm();
        return customRealm;
    }

    /**
     * @Description 创建cookie对象
     */
    @Bean(name = "sessionIdCookie")
    public SimpleCookie simpleCookie() {
        SimpleCookie simpleCookie = new SimpleCookie();
        simpleCookie.setName("ShiroSession");
        return simpleCookie;
    }

    /**
     * 注入授权的类Authorizer.
     *
     * @return Authorizer.
     */
    @Bean
    public Authorizer authorizer() {
        return new ModularRealmAuthorizer();
    }

    /**
     * 注入授权的类Authorizer.
     *
     * @return Authorizer.
     */
    @Bean
    public Authenticator authenticator() {
        return new ModularRealmAuthenticator();
    }

    /**
     * @Description 会话管理器
     */
    @Bean(name = "sessionManager")
    public DefaultWebSessionManager shiroSessionManager() {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setSessionValidationSchedulerEnabled(false);
       /* sessionManager.setSessionIdCookieEnabled(true);
        sessionManager.setSessionIdCookie(simpleCookie());
        sessionManager.setGlobalSessionTimeout(3600000);*/
        return sessionManager;
    }

    /**
     * @Description 权限管理器
     */
    @Bean(name="securityManager")
    public DefaultWebSecurityManager defaultWebSecurityManager(){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(customRealm());
        securityManager.setSessionManager(shiroSessionManager());
        return securityManager;
    }
    /**
     * shiro filter factory。
     *
     * @return ShiroFilterFactoryBean.
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean() {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        // Shiro的核心安全接口,这个属性是必须的
        shiroFilterFactoryBean.setSecurityManager(defaultWebSecurityManager());
        //需要权限的请求，如果没有登录则会跳转到这里设置的url
        shiroFilterFactoryBean.setLoginUrl("/login.html");
        //设置登录成功跳转url，一般在登录成功后自己代码设置跳转url，此处基本没用
        //shiroFilterFactoryBean.setSuccessUrl("/main.html");
        //设置无权限跳转界面,此处一般不生效,一般自定义异常
        //shiroFilterFactoryBean.setUnauthorizedUrl("/error.html");
        Map<String, Filter> filterMap = new LinkedHashMap<>();
        shiroFilterFactoryBean.setFilters(filterMap);
        /*
         * 定义shiro过滤链 Map结构
         * Map中key(xml中是指value值)的第一个'/'代表的路径是相对于HttpServletRequest.getContextPath()的值来的
         * anon：它对应的过滤器里面是空的,什么都没做,这里.do和.jsp后面的*表示参数,比方说login.jsp?main这种
         * authc：该过滤器下的页面必须验证后才能访问,它是Shiro内置的一个拦截器org.apache.shiro.web.filter.authc.
         * FormAuthenticationFilter
         */
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        /*
         * 过滤链定义，从上向下顺序执行，一般将 /**放在最为下边;
         * authc:所有url都必须认证通过才可以访问;
         * anon:所有url都都可以匿名访问
         */
        filterChainDefinitionMap.put("/login.html*", "anon");
        filterChainDefinitionMap.put("/error.html*", "anon");
        filterChainDefinitionMap.put("/error_404.html*", "anon");
        filterChainDefinitionMap.put("/error_400.html*", "anon");
        filterChainDefinitionMap.put("/error_5xx.html*", "anon");
        filterChainDefinitionMap.put("/user/index", "anon");
        filterChainDefinitionMap.put("/user/login", "anon");
        filterChainDefinitionMap.put("/static/**", "anon");
        filterChainDefinitionMap.put("/js/**", "anon");
        filterChainDefinitionMap.put("/css/**", "anon");
        filterChainDefinitionMap.put("/user/logout", "logout");
        filterChainDefinitionMap.put("/**", "authc");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    /**
     * 开启Shiro的注解(如@RequiresRoles,@RequiresPermissions),需借助SpringAOP扫描使用Shiro注解的类,并在必要时进行安全逻辑验证
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor() {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(defaultWebSecurityManager());
        return authorizationAttributeSourceAdvisor;
    }
}

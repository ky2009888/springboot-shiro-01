package com.ky2009666.service.controller;

import com.alibaba.fastjson.JSON;
import com.ky2009666.service.domain.ShUser;
import com.ky2009666.service.service.ShUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

/**
 * @author ky2009666
 * @description 创建user的controller
 * @date 2021/4/27
 **/
@Controller
@RequestMapping("user")
@Slf4j
public class UserController {
    /**
     * 定义shUserService.
     */
    @Resource
    private ShUserService shUserService;

    /**
     * 显示用户信息.
     *
     * @return shUser.
     */
    @GetMapping("show")
    @RequiresPermissions("read")
    public String showUser(String id) {
        ShUser shUser = shUserService.getById(id);
        return JSON.toJSONString(shUser);
    }

    @GetMapping("index")
    public String index() {
        return "login.html";
    }

    @GetMapping("login")
    public String login(String userName, String password, Model model) {
        String result = "login.html";
        if (StringUtils.isEmpty(userName) || StringUtils.isEmpty(password)) {
            return result;
        }
        Subject currentUser = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(userName, password);
        //Returns {@code true} if this Subject/user proved their identity <em>during their current session</em>
        //by providing valid credentials matching those known to the system, {@code false} otherwise.
        if (!currentUser.isAuthenticated()) {
            try {
                // 会触发com.shiro.config.MyShiroRealm的doGetAuthenticationInfo方法
                currentUser.login(token);
                result = "main.html";
                model.addAttribute("userName", userName);
            } catch (UnknownAccountException e) {
                result = "login.html";
            } catch (IncorrectCredentialsException e) {
                result = "login.html";
            }
        } else {
            model.addAttribute("userName", userName);
            result = "main.html";
        }
        return result;
    }

    @GetMapping("logout")
    public String logout() {
        Subject currentUser = SecurityUtils.getSubject();
        if (currentUser != null) {
            currentUser.logout();
        }
        return index();
    }

}

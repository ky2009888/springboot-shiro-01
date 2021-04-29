package com.ky2009666.service.service;

import com.ky2009666.service.domain.ShRole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 *
 */
public interface ShRoleService extends IService<ShRole> {
    /**
     * 根据登录用户名查询对应的角色列表.
     *
     * @param loginName 登录用户名.
     * @return List<ShRole> 角色列表.
     */
    List<ShRole> findRolesByLoginName(String loginName);
}

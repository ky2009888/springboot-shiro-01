package com.ky2009666.service.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ky2009666.service.domain.ShRole;
import com.ky2009666.service.service.ShRoleService;
import com.ky2009666.service.mapper.ShRoleMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 角色类.
 *
 * @author Lenovo
 */
@Service
public class ShRoleServiceImpl extends ServiceImpl<ShRoleMapper, ShRole>
        implements ShRoleService {
    /**
     * 根据登录用户名查询对应的角色列表.
     *
     * @param loginName 登录用户名.
     * @return List<ShRole> 角色列表.
     */
    @Override
    public List<ShRole> findRolesByLoginName(String loginName) {
        if (StringUtils.isEmpty(loginName)) {
            return null;
        }
        return getBaseMapper().findRolesByLoginName(loginName);
    }
}





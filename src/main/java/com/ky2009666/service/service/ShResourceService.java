package com.ky2009666.service.service;

import com.ky2009666.service.domain.ShResource;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 *
 */
public interface ShResourceService extends IService<ShResource> {
    List<ShResource> findPermissionsByRoleId(String roleId);
}

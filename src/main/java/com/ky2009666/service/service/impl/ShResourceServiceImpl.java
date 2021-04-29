package com.ky2009666.service.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ky2009666.service.domain.ShResource;
import com.ky2009666.service.service.ShResourceService;
import com.ky2009666.service.mapper.ShResourceMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
@Service
public class ShResourceServiceImpl extends ServiceImpl<ShResourceMapper, ShResource>
        implements ShResourceService {
    @Override
    public List<ShResource> findPermissionsByRoleId(String roleId) {
        ShResource shResource1 = new ShResource();
        shResource1.setResourceName("sys:user:add");
        ShResource shResource2 = new ShResource();
        shResource2.setResourceName("sys:user:list");
        ShResource shResource3 = new ShResource();
        shResource3.setResourceName("sys:user:update");
        List<ShResource> shResourceList = new ArrayList<>();
        shResourceList.add(shResource1);
        shResourceList.add(shResource2);
        shResourceList.add(shResource3);
        return shResourceList;
    }
}





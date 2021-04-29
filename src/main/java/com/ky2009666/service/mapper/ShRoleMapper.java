package com.ky2009666.service.mapper;

import com.ky2009666.service.domain.ShRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Entity com.ky2009666.service.domain.ShRole
 */
public interface ShRoleMapper extends BaseMapper<ShRole> {
    public List<ShRole> findRolesByLoginName(@Param("loginName") String loginName);
}





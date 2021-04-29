package com.ky2009666.service.realm;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.ky2009666.service.domain.ShResource;
import com.ky2009666.service.domain.ShRole;
import com.ky2009666.service.domain.ShUser;
import com.ky2009666.service.exception.CustomRuntimeException;
import com.ky2009666.service.service.ShResourceService;
import com.ky2009666.service.service.ShRoleService;
import com.ky2009666.service.service.ShUserService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author ky2009666
 * @description 自定义realm
 * @date 2021/4/28
 **/
public class CustomRealm extends AuthorizingRealm {
    /**
     * 自定义用户的service.
     */
    @Resource
    private ShUserService shUserService;
    /**
     * 自定义role的service句柄.
     */
    @Resource
    private ShRoleService shRoleService;
    /**
     * 自定义资源的service.
     */
    @Resource
    private ShResourceService shResourceService;

    /**
     * 授权
     * Retrieves the AuthorizationInfo for the given principals from the underlying data store.  When returning
     * an instance from this method, you might want to consider using an instance of
     * {@link SimpleAuthorizationInfo SimpleAuthorizationInfo}, as it is suitable in most cases.
     *
     * @param principals the primary identifying principals of the AuthorizationInfo that should be retrieved.
     * @return the AuthorizationInfo associated with this principals.
     * @see SimpleAuthorizationInfo
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        String loginName = principals.getPrimaryPrincipal() == null ? "" : principals.getPrimaryPrincipal().toString();
        // 受理权限
        // 角色
        Set<String> roles = new HashSet<String>();
        List<ShRole> roleList = shRoleService.findRolesByLoginName(loginName);
        List<ShResource> querypermissions = null;
        if (roleList != null) {
            for (ShRole shRole : roleList) {
                roles.add(shRole.getRoleName());
                querypermissions = shResourceService.findPermissionsByRoleId(shRole.getId());
            }
        }
        authorizationInfo.setRoles(roles);
        // 权限列表
        Set<String> permissions = new HashSet<String>();
        for (ShResource permission : querypermissions) {
            permissions.add(permission.getResourceName());
        }
        authorizationInfo.setStringPermissions(permissions);
        return authorizationInfo;
    }

    /**
     * 认证
     * Retrieves authentication data from an implementation-specific datasource (RDBMS, LDAP, etc) for the given
     * authentication token.
     * <p/>
     * For most datasources, this means just 'pulling' authentication data for an associated subject/user and nothing
     * more and letting Shiro do the rest.  But in some systems, this method could actually perform EIS specific
     * log-in logic in addition to just retrieving data - it is up to the Realm implementation.
     * <p/>
     * A {@code null} return value means that no account could be associated with the specified token.
     *
     * @param token the authentication token containing the user's principal and credentials.
     * @return an {@link AuthenticationInfo} object containing account data resulting from the
     * authentication ONLY if the lookup is successful (i.e. account exists and is valid, etc.)
     * @throws AuthenticationException if there is an error acquiring data or performing
     *                                 realm-specific authentication logic for the specified <tt>token</tt>
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //获取用户名
        String loginName = token.getPrincipal() == null ? "" : token.getPrincipal().toString();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("LOGIN_NAME", loginName);
        ShUser user = shUserService.getOne(queryWrapper);
        if (user == null) {
            throw new CustomRuntimeException("当前用户或密码不存在！");
        }
        return new SimpleAuthenticationInfo(loginName, user.getPassWord(), getName());
    }
}

package com.demo.common.shiro.realm;

import com.demo.common.utils.EncodeUtil;
import com.demo.modules.role.entity.Role;
import com.demo.modules.role.service.RoleService;
import com.demo.modules.user.entity.User;
import com.demo.modules.user.service.UserService;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.List;

/**
 * 认证、授权 realm
 *
 * @author gc
 */
public class SystemAuthorizingRealm extends AuthorizingRealm {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;

    public SystemAuthorizingRealm() {
        this.setCachingEnabled(false);
    }

    /**
     * 认证回调函数, 登录时调用
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) {
        UsernamePasswordToken token = (UsernamePasswordToken) authcToken;

        User user = userService.getUserByLoginName(token.getUsername());
        if (user != null) {
            byte[] salt = EncodeUtil.decodeHex(user.getSalt());
            return new SimpleAuthenticationInfo(new Principal(user),
                    user.getPassword(), ByteSource.Util.bytes(salt), this.getName());
        } else {
            return null;
        }
    }

    /**
     * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        Principal principal = (Principal) getAvailablePrincipal(principals);
        User user = userService.getUserByLoginName(principal.getLoginName());
        if (user != null) {
            SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
            // 添加用户权限
            info.addStringPermission("user");
            List<Role> roleList = roleService.findRoleByUserId(user.getId());
            // 添加用户角色信息
            for (Role role : roleList) {
                info.addRole(role.getCode());
            }
            return info;
        } else {
            return null;
        }
    }

    /**
     * 授权用户信息
     */
    public static class Principal implements Serializable {

        private static final long serialVersionUID = 1L;

        private String id;
        private String loginName;
        private String name;

        public Principal(User user) {
            this.id = user.getId();
            this.loginName = user.getLoginName();
            this.name = user.getName();
        }

        public String getId() {
            return id;
        }

        public String getLoginName() {
            return loginName;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return id;
        }

    }

}

package com.learn.demo.config;

import com.learn.demo.model.ShiroConfigModel;
import com.learn.demo.utils.shiro.ShiroRealm;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.mgt.DefaultFilter;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * shiro 配置类.
 *
 * @author demo
 * @version 1.0.0
 * @date 2019/9/19 10:57
 */
@Configuration
public class ShiroConfig {

  @Resource
  private ShiroConfigModel shiroConfigModel;

  /**
   * shiro 过滤器.
   *
   * @param securityManager securityManager
   * @return ShiroFilterFactoryBean
   */
  @Bean("shiroFilter")
  public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
    ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
    shiroFilterFactoryBean.setSecurityManager(securityManager);
    // 设置页面
    if (shiroConfigModel.isSetUrl()) {
      //登录页面，不设置默认寻找web工程根目录下的login.jsp
      shiroFilterFactoryBean.setLoginUrl(shiroConfigModel.getLoginUrl());
      // 登录成功后跳转的页面
      shiroFilterFactoryBean.setSuccessUrl(shiroConfigModel.getSuccessUrl());
      // 未授权的页面
      shiroFilterFactoryBean.setUnauthorizedUrl(shiroConfigModel.getNoAuthorityUrl());
    }
    // 拦截器
    Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();

    // 设置调试时不过滤路径
    if (shiroConfigModel.isDebug()) {
      List<String> debugUnFilterList = shiroConfigModel.getDebugUnFilterList();
      for (String path : debugUnFilterList) {
        //不要权限的路径
        filterChainDefinitionMap.put(path, DefaultFilter.anon.name());
      }
    }
    // 不过滤的路径
    List<String> unFilterList = shiroConfigModel.getUnFilterList();
    for (String path : unFilterList) {
      //不要权限的路径
      filterChainDefinitionMap.put(path, DefaultFilter.anon.name());
    }

    // 过滤路径
    List<String> filterList = shiroConfigModel.getFilterList();
    for (String path : filterList) {
      filterChainDefinitionMap.put(path, DefaultFilter.authc.name());
    }

    shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);

    return shiroFilterFactoryBean;
  }

  /**
   * .
   *
   * @param myRealm myRealm
   * @return DefaultWebSecurityManager
   */
  @Bean("securityManager")
  public DefaultWebSecurityManager securityManager(ShiroRealm myRealm) {
    DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
    securityManager.setRealm(myRealm);

    /*
     * 关闭shiro自带的session，详情见文档
     * http://shiro.apache.org/session-management.html#SessionManagement-
     * StatelessApplications%28Sessionless%29
     */

    return securityManager;
  }
}

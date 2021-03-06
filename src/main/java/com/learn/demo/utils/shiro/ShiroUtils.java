package com.learn.demo.utils.shiro;

import com.learn.demo.entity.UserEntity;
import com.learn.demo.model.MyExceptionModel;
import com.learn.demo.utils.ConstUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Component;

/**
 * Shiro工具类.
 *
 * @author demo
 * @version 1.0.0
 * @date 2019/9/23 16:34
 */
@Component
public class ShiroUtils {

  /**
   * shiro保存用户信息.
   *
   * @param user 用户信息
   */
  public void login(UserEntity user) {
    UsernamePasswordToken token = new UsernamePasswordToken(user.getAccount(), user.getPassword());
    Subject subject = SecurityUtils.getSubject();
    try {
      //这一步在调用login(token)方法时,它会走到MyRealm.doGetAuthenticationInfo()方法中
      subject.login(token);
    } catch (Exception ex) {
      throw new MyExceptionModel(ex.getMessage());
    }
    //验证是否登录成功
    if (subject.isAuthenticated()) {
      Session session = Jurisdiction.getSession();
      session.removeAttribute(ConstUtils.SHIRO_ACCOUNT);
      session.removeAttribute(ConstUtils.SHIRO_USER);
      session.setAttribute(ConstUtils.SHIRO_USER, user);
      session.setAttribute(ConstUtils.SHIRO_ACCOUNT, user.getAccount());
    } else {
      token.clear();
      throw new MyExceptionModel("尝试登录系统失败,无权限");
    }
  }

  /**
   * shiro 登出用户.
   */
  public void logout() {
    Subject subject = SecurityUtils.getSubject();
    try {
      subject.logout();
    } catch (Exception ex) {
      throw new MyExceptionModel(ex.getMessage());
    }
    if (subject.isAuthenticated()) {
      throw new MyExceptionModel("尝试登出系统失败");
    }
  }
}

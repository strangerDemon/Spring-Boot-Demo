package com.learn.demo.service;

import com.learn.demo.entity.UserEntity;
import com.learn.demo.model.MyExceptionModel;
import com.learn.demo.repository.UserRepository;
import com.learn.demo.utils.EncryptUtils;
import com.learn.demo.utils.RedisUtils;
import java.util.List;
import java.util.UUID;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  @Resource
  private UserRepository userRepository;

  @Resource
  private RedisUtils redisUtils;


  public UserEntity getOne(String s) {
    return userRepository.getOne(s);
  }

  public UserEntity getByAccount(String account) {
    return userRepository.getByAccount(account);
  }

  public void delete(UserEntity userEntity) {
    userRepository.delete(userEntity);
  }

  public List<UserEntity> findAll() {
    return userRepository.findAll();
  }

  /**
   * 用户登录.
   *
   * @param entity 登录账号
   * @return 用户信息
   */
  public UserEntity userLogin(UserEntity entity) {
    String encryptPS = EncryptUtils.encrypt(entity.getPassword());
    UserEntity user = userRepository.getByAccount(entity.getAccount());
    if (user == null) {
      throw new MyExceptionModel("账号不存在!");
    }
    if (!encryptPS.equals(user.getPassword())) {
      throw new MyExceptionModel("密码不正确!");
    }
    return user;
  }

  /**
   * 添加用户.
   *
   * @param entity 新增用户信息
   * @return 用户信息
   */
  public UserEntity addUser(UserEntity entity) {
    UserEntity user = userRepository.getByAccount(entity.getAccount());
    if (user != null) {
      throw new MyExceptionModel("账号:" + entity.getAccount() + " 已存在！");
    }
    entity.setUserId(UUID.randomUUID().toString());
    entity.setPassword(EncryptUtils.encrypt(entity.getPassword()));
    return userRepository.save(entity);
  }
}
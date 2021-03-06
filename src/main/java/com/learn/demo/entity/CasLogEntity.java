package com.learn.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

/**
 * cas日志.
 *
 * @author demo
 * @version 1.0.0
 * @date 2019/9/16 10:28
 */
@Data
@Entity
@Table(name = "CasLog")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class CasLogEntity {

  @Id
  private String casLogId;

  private String userId;

  private String account;

  private String realName;

  private String ip;

  private Date loginTime;

  private Date logoutTime;

  private Integer logStatus;

  private String browserType;

  private String casSessionId;

  private String description;
}

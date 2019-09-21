package com.revolut.domain;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;

@Entity(name = "account")
@Table(name = "account")
public class AccountInfo {
  @Id
  @GeneratedValue
  private long id;

  @NotEmpty(message = "Name must be present")
  @Column(name = "name")
  private String name;

  @NotEmpty(message = "Name is required")
  @Email(message = "Email address is not valid.")
  @Column(name = "email_address", unique = true)
  private String emailAddress;

  @Column(name = "account_balance")
  private BigDecimal accountBalance;

  public AccountInfo() {

  }

  public AccountInfo(long id) {
    this.id = id;
  }

  public long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public BigDecimal getAccountBalance() {
    return accountBalance;
  }

  public void setAccountBalance(BigDecimal accountBalance) {
    this.accountBalance = accountBalance;
  }

  public String getEmailAddress() {
    return emailAddress;
  }

  public void setEmailAddress(String emailAddress) {
    this.emailAddress = emailAddress;
  }

  @Override
  public String toString() {
    return "AccountInfo{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", emailAddress='" + emailAddress + '\'' +
            ", accountBalance=" + accountBalance +
            '}';
  }
}

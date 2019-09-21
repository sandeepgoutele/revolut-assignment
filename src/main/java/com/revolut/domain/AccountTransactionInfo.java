package com.revolut.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.sql.Date;

@Entity(name = "account_transaction")
@Table(name = "account_transaction")
public class AccountTransactionInfo {
  @Id
  @GeneratedValue
  private Long id;

  @NotNull(message = "Sender Account id must be present")
  @Column(nullable = false)
  private Long sendingAccountId;

  @NotNull(message = "Receiving Account id must be present")
  @Column(nullable = false)
  private Long receivingAccountId;

  @NotNull(message = "Transaction Amount cannot be absent.")
  @Column(nullable = false)
  private BigDecimal transactionAmount;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private TransactionStatus transactionStatus;

  @Column
  private Date dateOfTransaction;
  /**
   * reason for transaction failure if transaction failed.
   */
  @Column
  private String reason;

  public AccountTransactionInfo(@NotNull(message = "Sender Account id must be present") Long sendingAccountId,
                                @NotNull(message = "Receiving Account id must be present") Long receivingAccountId,
                                @NotNull(message = "Transaction Amount cannot be absent.") BigDecimal transactionAmount,
                                TransactionStatus transactionStatus,
                                Date dateOfTransaction) {
    this.sendingAccountId = sendingAccountId;
    this.receivingAccountId = receivingAccountId;
    this.transactionAmount = transactionAmount;
    this.transactionStatus = transactionStatus;
    this.dateOfTransaction = dateOfTransaction;
  }

  public Date getDateOfTransaction() {
    return dateOfTransaction;
  }

  public AccountTransactionInfo setDateOfTransaction(Date dateOfTransaction) {
    this.dateOfTransaction = dateOfTransaction;
    return this;
  }

  public Long getId() {
    return id;
  }

  public AccountTransactionInfo setId(Long id) {
    this.id = id;
    return this;
  }

  public Long getSendingAccountId() {
    return sendingAccountId;
  }

  public AccountTransactionInfo setSendingAccountId(Long sendingAccountId) {
    this.sendingAccountId = sendingAccountId;
    return this;
  }

  public BigDecimal getTransactionAmount() {
    return transactionAmount;
  }

  public AccountTransactionInfo setTransactionAmount(BigDecimal transactionAmount) {
    this.transactionAmount = transactionAmount;
    return this;
  }

  public Long getReceivingAccountId() {
    return receivingAccountId;
  }

  public AccountTransactionInfo setReceivingAccountId(Long receivingAccountId) {
    this.receivingAccountId = receivingAccountId;
    return this;
  }

  public TransactionStatus getTransactionStatus() {
    return transactionStatus;
  }

  public AccountTransactionInfo setTransactionStatus(TransactionStatus transactionStatus) {
    this.transactionStatus = transactionStatus;
    return this;
  }

  public String getReason() {
    return reason;
  }

  public AccountTransactionInfo setReason(String reason) {
    this.reason = reason;
    return this;
  }

  @Override
  public String toString() {
    return "AccountTransactionInfo{" +
            "id=" + id +
            ", sendingAccountId=" + sendingAccountId +
            ", receivingAccountId=" + receivingAccountId +
            ", transactionStatus=" + transactionStatus +
            ", reason='" + reason + '\'' +
            '}';
  }
}

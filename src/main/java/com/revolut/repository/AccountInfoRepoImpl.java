package com.revolut.repository;

import com.revolut.domain.AccountInfo;
import com.revolut.domain.AccountTransactionInfo;
import com.revolut.domain.TransactionStatus;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;
import java.util.Objects;

public class AccountInfoRepoImpl implements AccountInfoRepo {
  private EntityManager entityManager;

  @Inject
  public AccountInfoRepoImpl(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  @Override
  public AccountInfo saveAccount(AccountInfo accountInfo) {
    entityManager.getTransaction().begin();
    entityManager.persist(accountInfo);
    entityManager.getTransaction().commit();
    return getAccountByEmail(accountInfo.getEmailAddress());
  }

  @Override
  public boolean doesAccountExistById(Long id) {
    return Objects.nonNull(getAccountById(id));
  }

  @Override
  public void updateAccountBalancesAndTransactionLog(AccountInfo updatedSenderAccountBalance,
                                                     AccountInfo updatedReceiverAccountBalance,
                                                     AccountTransactionInfo accountTransactionInfo) {

    entityManager.getTransaction().begin();

    entityManager.persist(updatedSenderAccountBalance);
    entityManager.persist(updatedReceiverAccountBalance);
    entityManager.persist(updateTransactionEntity(accountTransactionInfo,
            TransactionStatus.SUCCESS,
            ""));

    entityManager.getTransaction().commit();
  }

  private AccountTransactionInfo updateTransactionEntity(AccountTransactionInfo accountTransactionInfo,
                                                         TransactionStatus transactionStatus,
                                                         String reason) {
    accountTransactionInfo.setTransactionStatus(transactionStatus);
    accountTransactionInfo.setReason(reason);
    accountTransactionInfo.setDateOfTransaction(new Date(System.currentTimeMillis()));

    return accountTransactionInfo;
  }

  @Override
  public AccountInfo getAccountById(Long id) {
    AccountInfo accountInfo = null;
    try {
      Query query =
              entityManager.createQuery("from " + AccountInfo.class.getName() + " acc where acc.id = ?1");
      query.setParameter(1, id);
      accountInfo = (AccountInfo) query.getSingleResult();
    } catch (Exception ex) {
      return accountInfo;
    }
    return accountInfo;
  }

  @Override
  public AccountInfo getAccountByEmail(String emailAddress) {
    AccountInfo accountInfo = null;
    try {
      Query query =
              entityManager.createQuery("from " + AccountInfo.class.getName() + " acc where acc.emailAddress = ?1");
      query.setParameter(1, emailAddress);
      accountInfo = (AccountInfo) query.getSingleResult();
    } catch (Exception ex) {
      System.out.println("Exception occurred: " + ex);
    }
    return accountInfo;
  }

  @Override
  @SuppressWarnings("unchecked")
  public List<AccountInfo> getAllAccounts() {
    Query query = entityManager.createQuery("from " + AccountInfo.class.getName() + " a");
    return query.getResultList();
  }

  @Transactional
  @Override
  public void deleteAccount(Long id) {
    entityManager.getTransaction().begin();
    Query query =
            entityManager.createQuery("delete from " + AccountInfo.class.getName() + " acc where acc.id = ?1");
    query.setParameter(1, id);
    query.executeUpdate();
    entityManager.getTransaction().commit();
  }
}

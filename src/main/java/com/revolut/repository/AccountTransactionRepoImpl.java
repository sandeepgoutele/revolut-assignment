package com.revolut.repository;

import com.revolut.domain.AccountTransactionInfo;
import com.revolut.domain.TransactionStatus;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.sql.Date;
import java.util.List;

public class AccountTransactionRepoImpl implements AccountTransactionRepo {
  private EntityManager entityManager;

  @Inject
  public AccountTransactionRepoImpl(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  @Override
  public void createAccountTransaction(AccountTransactionInfo accountTransactionInfo,
                                       TransactionStatus transactionStatus,
                                       String reason) throws Exception {
    try {
      entityManager.getTransaction().begin();
      accountTransactionInfo.setDateOfTransaction(new Date(System.currentTimeMillis()));
      accountTransactionInfo.setTransactionStatus(transactionStatus);
      accountTransactionInfo.setReason(reason);

      entityManager.persist(accountTransactionInfo);
      entityManager.getTransaction().commit();
    } catch (Exception exception) {
      throw new Exception(exception.getMessage());
    }
  }

  @Override
  @SuppressWarnings("unchecked")
  public List<AccountTransactionInfo> getAccountTransactionsByAccountId(Long accountId) {
    Query query = entityManager.createQuery("from " + AccountTransactionInfo.class.getName() + " a");

    List<AccountTransactionInfo> accountTransactionEntities = query.getResultList();

    accountTransactionEntities.forEach(accountTransactionEntity -> System.out.println("\ntransaction entity = " + accountTransactionEntity.toString()));
    return accountTransactionEntities;
  }
}

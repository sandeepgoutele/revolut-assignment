package com.revolut.service;

import com.revolut.domain.AccountInfo;
import com.revolut.domain.AccountTransactionInfo;
import com.revolut.domain.TransactionStatus;
import com.revolut.repository.AccountInfoRepo;
import com.revolut.repository.AccountTransactionRepo;
import com.revolut.util.JsonParser;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Arrays;

import static org.junit.Assert.*;

public class AccountTransactionServiceTest {
  @Mock
  private AccountTransactionRepo accountTransactionRepo;
  @Mock
  private AccountInfoRepo accountInfoRepo;

  @Mock
  private JsonParser jsonParser;

  private AccountTransactionService accountTransactionService;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    accountTransactionService = new AccountTransactionService(accountInfoRepo,
            accountTransactionRepo, jsonParser);
  }

  @Test
  public void get_all_money_transfer_transactions_for_account_expect_200_status() {
    String accountId = "3";

    AccountInfo accountInfo = new AccountInfo(Long.valueOf(accountId));
    accountInfo.setName("revolut");
    accountInfo.setEmailAddress("revolut@revolut.com");

    Mockito.when(accountInfoRepo.doesAccountExistById(Long.valueOf(accountId))).thenReturn(true);

    Mockito.when(accountTransactionRepo.getAccountTransactionsByAccountId(Long.valueOf(accountId)))
            .thenReturn(
                    Arrays.asList(
                            new AccountTransactionInfo(Long.valueOf(accountId)
                                    , 4L
                                    , new BigDecimal("2500")
                                    , TransactionStatus.SUCCESS
                                    , new Date(System.currentTimeMillis()))));

    assertEquals(200,
            accountTransactionService.getAccountTransactionsByAccountId(accountId).getStatusCode());
  }
}

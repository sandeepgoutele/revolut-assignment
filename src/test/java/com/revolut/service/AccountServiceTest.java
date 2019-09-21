package com.revolut.service;

import com.revolut.domain.AccountInfo;
import com.revolut.model.ApplicationOpResponse;
import com.revolut.repository.AccountInfoRepo;
import com.revolut.repository.AccountTransactionRepo;
import com.revolut.util.JsonParser;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class AccountServiceTest {
  @Mock
  private AccountInfoRepo accountInfoRepo;
  @Mock
  private AccountTransactionRepo accountTransactionRepo;
  @Mock
  private JsonParser jsonParser;
  private AccountService accountService;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    accountService = new AccountService(accountInfoRepo,
            accountTransactionRepo, jsonParser);
  }

  @Test
  public void create_account_expect_status_code_201() {
    AccountInfo accountInfo = new AccountInfo();
    accountInfo.setName("revolut");
    accountInfo.setEmailAddress("revolut@revolut.com");
    accountInfo.setAccountBalance(BigDecimal.valueOf(500000));

    Mockito.when(accountInfoRepo.saveAccount(accountInfo)).thenReturn(accountInfo);
    ApplicationOpResponse applicationOpResponse =
            accountService.createAccount(accountInfo);

    System.out.println(accountInfo.toString());
    assertEquals(applicationOpResponse.getStatusCode(), 201);
  }

  @Test
  public void create_account_when_no_email_present_expect_status_code_400() {
    AccountInfo accountInfo = new AccountInfo();

    Mockito.when(accountInfoRepo.saveAccount(accountInfo)).thenReturn(accountInfo);
    ApplicationOpResponse applicationOpResponse =
            accountService.createAccount(accountInfo);

    System.out.println(accountInfo.toString());
    assertEquals(applicationOpResponse.getStatusCode(), 400);
  }

  @Test
  public void create_account_when_email_address_exist_but_empty_expect_status_code_400() {
    AccountInfo accountInfo = new AccountInfo();
    accountInfo.setName("");
    accountInfo.setEmailAddress("");

    Mockito.when(accountInfoRepo.saveAccount(accountInfo)).thenReturn(accountInfo);
    ApplicationOpResponse applicationOpResponse =
            accountService.createAccount(accountInfo);

    System.out.println(accountInfo.toString());
    assertEquals(applicationOpResponse.getStatusCode(), 400);
  }

  @Test
  public void create_account_when_email_address_format_is_incorrect_expect_status_code_400() {
    AccountInfo accountInfo = new AccountInfo();
    accountInfo.setName("aa");
    accountInfo.setEmailAddress("bb");

    Mockito.when(accountInfoRepo.saveAccount(accountInfo)).thenReturn(accountInfo);
    ApplicationOpResponse applicationOpResponse =
            accountService.createAccount(accountInfo);

    System.out.println(accountInfo.toString());
    assertEquals(applicationOpResponse.getStatusCode(), 400);
  }

  @Test
  public void delete_account_expect_status_code_400() {

    long id = 3;
    AccountInfo accountInfo = new AccountInfo(id);
    accountInfo.setName("aa");
    accountInfo.setEmailAddress("bb");

    Mockito.when(accountInfoRepo.getAccountById(id)).thenReturn(accountInfo);
    ApplicationOpResponse applicationOpResponse =
            accountService.deleteAccountById(String.valueOf(id));

    System.out.println(accountInfo.toString());
    assertEquals(applicationOpResponse.getStatusCode(), 204);
  }

  @Test(expected = IllegalArgumentException.class)
  public void create_account_when_account_data_is_null_expect_null_pointer_exception_thrown() {
    ApplicationOpResponse applicationOpResponse =
            accountService.createAccount(null);
    assertEquals(applicationOpResponse.getStatusCode(), 400);
  }
}

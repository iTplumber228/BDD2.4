package ru.netology.web.test;


import com.codeborne.selenide.ElementsCollection;
import lombok.NonNull;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import lombok.Value;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.DashboardPage;
import ru.netology.web.page.LoginPage;


import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.impl.Html.text;
import static com.codeborne.selenide.logevents.SelenideLogger.get;

import static com.google.common.collect.Multimaps.index;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.shadow.com.univocity.parsers.common.ArgumentUtils.indexOf;
import static ru.netology.web.data.DataHelper.*;


class MoneyTransferTest {

    DashboardPage dashboardPage;

  @BeforeEach
  void setUp() {
    open("http://localhost:9999");
    var loginPage = new LoginPage();
    var authInfo = DataHelper.getAuthInfo();
    var verificationPage = loginPage.validLogin(authInfo);
    var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
    dashboardPage = verificationPage.validVerify(verificationCode);

  }

  @Test

    void shouldTransferFromFirstToSecond(){
      var firstCard = DataHelper.getFirstCardInfo();//CardInfo("5559 0000 0000 0001", "92df3f1c-a033-48e6-8390-206f6b1f56c0")
      var secondCard = DataHelper.getSecondCardInfo();//CardInfo("5559 0000 0000 0002", "0f3f5c2a-249e-4c3d-8287-09f7a039391d")
      var firstCardBalance = dashboardPage.getCardBalance(DataHelper.getFirstIndex());
      var secondCardBalance = dashboardPage.getCardBalance(DataHelper.getSecondIndex());
      var amount = generateValidAmount(firstCardBalance);
      var expectedBalanceFirstCard = firstCardBalance - amount;
      var expectedBalanceSecondCard = secondCardBalance + amount;
      var transferPage = dashboardPage.selectCardToTransfer(secondCard);
      dashboardPage = transferPage.makeValidTransfer(String.valueOf(amount), firstCard);
      var actualBalanceFirstCard = dashboardPage.getCardBalance(DataHelper.getFirstIndex());
      var actualBalanceSecondCard = dashboardPage.getCardBalance(DataHelper.getSecondIndex());
      assertEquals(expectedBalanceFirstCard,actualBalanceFirstCard);
      assertEquals(expectedBalanceSecondCard,actualBalanceSecondCard);
  }

   @Test

    void shouldGetErrorMessageIfAmountMoreBalance(){
      var firstCardInfo = getFirstCardInfo();
      var secondCardInfo = getSecondCardInfo();
      var firstCardBalance = dashboardPage.getCardBalance(DataHelper.getFirstIndex());
      var secondCardBalance = dashboardPage.getCardBalance(DataHelper.getSecondIndex());
      var amount = generateInvalidAmount(secondCardBalance);
      var transferPage = dashboardPage.selectCardToTransfer(firstCardInfo);
      transferPage.makeTransfer(String.valueOf(amount),secondCardInfo);
      transferPage.findErrorMessage("Вы пытаетесь перевести сумму, превышающую остаток на карте списания");
      var actualBalanceFirstCard = dashboardPage.getCardBalance(DataHelper.getFirstIndex());
      var actualBalanceSecondCard = dashboardPage.getCardBalance(DataHelper.getSecondIndex());
      assertEquals(firstCardBalance, actualBalanceFirstCard);
      assertEquals(secondCardBalance, actualBalanceSecondCard);

    }
}


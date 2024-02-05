package ru.netology.web.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import lombok.val;
import ru.netology.web.data.DataHelper;

import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Condition.visible;
//import static com.codeborne.selenide.ElementsCollection.getText;
import static com.codeborne.selenide.Selenide.*;

public class DashboardPage {
  // к сожалению, разработчики не дали нам удобного селектора, поэтому так
  private final String balanceStart = "баланс: ";
  private final String balanceFinish = " р.";
  private final SelenideElement heading = $("[data-test-id=dashboard]");
  private final ElementsCollection cards = $$(".list__item div");




  public DashboardPage() {
    heading.shouldBe(visible);
  }



 /* public int getCardBalance(DataHelper.CardInfo cardInfo) {
    var text = cards.findBy(text(cardInfo.getCardNumber().substring(15))).getText();
    return extractBalance(text);
  }*/

  private int extractBalance(String text) {
    val start = text.indexOf(balanceStart);
    val finish = text.indexOf(balanceFinish);
    val value = text.substring(start + balanceStart.length(), finish);
    return Integer.parseInt(value);
  }
  public int getCardBalance(int index) {
    val text = cards.get(index).getText();
    return extractBalance(text);
  }

  public TransferPage selectCardToTransfer(DataHelper.CardInfo cardinfo){
    cards.findBy(attribute("data-test-id", cardinfo.getTestId())).$("button").click();
    return new TransferPage();
  }




}

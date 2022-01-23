package team.hello.usedbook.domain.enums;

public enum Payment {
    READY("결제준비"),
    CARD("카드결제"),
    PHONE("휴대폰결제"),
    ACCOUNT("계좌이체");

    private final String value;
    Payment(String value) {  this.value = value;  }
    public String getValue() {
        return value;
    }
}

package team.hello.usedbook.domain.enums;

public enum SaleStatus {
    READY("판매중"),
    RESERVATION("예약중"),
    COMPLETE("판매완료");

    private final String value;
    SaleStatus(String value) {  this.value = value;  }
    public String getValue() {
        return value;
    }
}

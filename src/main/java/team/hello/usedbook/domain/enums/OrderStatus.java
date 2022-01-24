package team.hello.usedbook.domain.enums;

public enum OrderStatus {
    COMPLETE("구매완료"),
    CANCEL("구매취소");

    private final String value;
    OrderStatus(String value) {  this.value = value;  }
    public String getValue() {
        return value;
    }
}

package team.hello.usedbook.domain;

import lombok.Getter;
import lombok.ToString;

@Getter @ToString
public class Address {
    private Long id;
    private String orderId;
    private String postcode;
    private String defaultAddress;
    private String detailAddress;
    private String extraAddress;

    public Address(String postcode, String defaultAddress, String detailAddress, String extraAddress) {
        this.postcode = postcode;
        this.defaultAddress = defaultAddress;
        this.detailAddress = detailAddress;
        this.extraAddress = extraAddress;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}

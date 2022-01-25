package team.hello.usedbook.repository;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.springframework.stereotype.Repository;
import team.hello.usedbook.domain.Address;

@Mapper
@Repository
public interface AddressRepository {

    @Insert("insert into address(orderid, postcode, defaultaddress, detailaddress, extraaddress)" +
            "values(#{orderId}, #{postcode}, #{defaultAddress}, #{detailAddress}, #{extraAddress})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void save(Address address);
}

package team.hello.usedbook.repository;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.springframework.stereotype.Repository;
import team.hello.usedbook.domain.Address;

@Mapper
@Repository
public interface AddressRepository {

    @Insert("insert into address(postcode, defaultaddress, detailaddress, extraaddress)" +
            "values(#{postcode}, #{defaultaddress}, #{detailaddress}, #{extraaddress})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void save(Address address);
}

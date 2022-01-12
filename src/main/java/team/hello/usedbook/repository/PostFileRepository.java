package team.hello.usedbook.repository;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.springframework.stereotype.Repository;
import team.hello.usedbook.domain.PostFile;

@Mapper
@Repository
public interface PostFileRepository {

    @Insert("insert into postfile(postid, filepath, filename) values(#{postId}, #{filePath}, #{fileName})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void save(PostFile postFile);
}

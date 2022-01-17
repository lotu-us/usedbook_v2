package team.hello.usedbook.repository;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import team.hello.usedbook.domain.PostFile;

import java.util.List;

@Mapper
@Repository
public interface PostFileRepository {

    @Insert("insert into postfile(postid, filepath, filename) values(#{postId}, #{filePath}, #{fileName})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void save(PostFile postFile);

    @Select("select * from postfile where postid=#{postId}")
    List<PostFile> findById(Long postId);

    @Delete("delete from postfile where postid=#{postId} and filename=#{fileName}")
    int removeFile(Long postId, String fileName);
}

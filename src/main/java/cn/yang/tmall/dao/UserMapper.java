package cn.yang.tmall.dao;

import cn.yang.tmall.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    int checkUserName(String userName);

    int checkEmail(String email);

    User selectLogin(@Param("username") String userName, @Param("password") String password);

    String selectQuestion(String userName);

    int checkAnswer(@Param("username") String userName, @Param("question") String question, @Param("answer") String answer);

    int updatePasswordByUserName(@Param("username") String userName, @Param("password") String password);

    int checkPassword(@Param("password") String password,@Param("userId") Integer userId);

    int checkEmailByUserId(@Param("email") String email,@Param("userId") Integer userId);
}
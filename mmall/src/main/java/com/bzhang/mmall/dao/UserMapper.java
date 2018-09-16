package com.bzhang.mmall.dao;

import org.apache.ibatis.annotations.Param;

import com.bzhang.mmall.pojo.User;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
    
    int checkUsername(String username);
    
    int checkEmail(String email);
    
    User selectLogin(@Param("username") String username, @Param("password")String password);
    
    String  selectQuestionByUsername(String username);
    
    int checkAnswer(@Param("question")String question,@Param("username")String username,@Param("answer")String answer);
    
    int updatePasswordByUsername(@Param("username")String username,@Param("passwordNew")String passwordNew);
    
    int updatePasswordByUsernamePassword(@Param("username")String username,@Param("password")String password,@Param("passwordNew")String passwordNew);
    
    int selectByUsernamePassword(@Param("username") String username, @Param("password")String password);
    
    int checkEmailById(@Param("id")int id,@Param("email")String email);



}
package com.msa4lms.domain.user.mapper;

import com.msa4lms.domain.user.entities.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    User findByLoginId(@Param("loginId") String loginId);
    User findByPk(int id);
    int updatePassword(
            @Param("id") int id,
            @Param("password") String password
    );

}

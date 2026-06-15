package com.msa4lms.domain.user.mapper;

import com.msa4lms.domain.user.entities.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    User findByUserNo(@Param("userNo") String userNo);
}

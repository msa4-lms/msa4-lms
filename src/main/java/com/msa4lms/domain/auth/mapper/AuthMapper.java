package com.msa4lms.domain.auth.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AuthMapper {
    int updateRefreshToken(
            @Param("id") int id,
            @Param("refreshToken") String refreshToken
    );
}

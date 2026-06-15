package com.msa4lms.domain.auth.responses;

import com.msa4lms.domain.user.responses.UserRes;
import lombok.Builder;

@Builder
public record AuthRes(
        UserRes user
        ,String accessToken
) {
}

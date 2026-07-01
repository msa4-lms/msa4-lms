package com.msa4lms.domain.dashboard.responses;

public record NoticeRes(
        Long id
        , String title
        , String content
        , String targetRole
        , String createdAt
) {
}

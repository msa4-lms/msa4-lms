package com.msa4lms.domain.grade.requests;

public record ReplyObjectionReq(
    Boolean approve,
    String reply,
    GradeSaveDto newScores
) {}

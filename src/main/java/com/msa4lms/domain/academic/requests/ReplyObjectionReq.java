package com.msa4lms.domain.academic.requests;

public record ReplyObjectionReq(
    Boolean approve,
    String reply,
    GradeSaveDto newScores
) {}

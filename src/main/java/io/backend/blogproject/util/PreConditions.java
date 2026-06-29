package io.backend.blogproject.util;

import io.backend.blogproject.common.CustomException;
import io.backend.blogproject.constant.ErrorCode;

public final class PreConditions {
    public static void validate(boolean expression, ErrorCode errorCode){
        if (!expression) throw new CustomException(errorCode);
    }
}

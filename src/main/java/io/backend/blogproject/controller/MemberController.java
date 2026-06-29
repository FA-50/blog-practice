package io.backend.blogproject.controller;

import io.backend.blogproject.common.ApiResult;
import io.backend.blogproject.constant.SuccessCode;
import io.backend.blogproject.domain.dto.UserCreateRequest;
import io.backend.blogproject.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController  {

    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<ApiResult<Void>> signUp(
            @RequestBody UserCreateRequest request
    ){
        memberService.signUp(request);
        return ApiResult.empty(
                SuccessCode.USER_CREATE_SUCCESS
        );
    }
}

package io.backend.blogproject.service;

import io.backend.blogproject.constant.ErrorCode;
import io.backend.blogproject.domain.dto.KeyPair;
import io.backend.blogproject.domain.dto.UserLoginRequest;
import io.backend.blogproject.domain.entity.Members;
import io.backend.blogproject.repository.MemberRepository;
import io.backend.blogproject.util.PreConditions;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;


    @Transactional
    public KeyPair signIn(UserLoginRequest request){

        Members foundedMember = memberRepository.findByLoginIdOrThrow(request.userId());

        PreConditions.validate(
                passwordEncoder.matches(request.password(), foundedMember.getPassword()),
                ErrorCode.AUTHENTICATION_INCORRECT
        );

        return tokenProvider.issueKeyPair(foundedMember.getMemberId());
    }
}


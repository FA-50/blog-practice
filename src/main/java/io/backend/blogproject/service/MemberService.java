package io.backend.blogproject.service;

import io.backend.blogproject.constant.ErrorCode;
import io.backend.blogproject.domain.dto.UserCreateRequest;
import io.backend.blogproject.domain.entity.DefaultCurrentUser;
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
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public DefaultCurrentUser loadUserDetailsFromMember(Long userId){
        return DefaultCurrentUser.from(
                memberRepository.findByIdOrThrow(userId)
        );
    }

    @Transactional
    public void signUp(UserCreateRequest request){
        String loginId = request.id() == null ? null : request.id().trim();
        String name = request.name() == null ? null : request.name().trim();

        PreConditions.validate(
                loginId != null && loginId.matches("[A-Za-z0-9_-]{4,30}"),
                ErrorCode.INVALID_USER_ID
        );
        PreConditions.validate(
                name != null && !name.isBlank() && name.length() <= 10,
                ErrorCode.INVALID_USER_NAME
        );
        PreConditions.validate(
                request.password() != null
                        && request.password().length() >= 8
                        && request.password().length() <= 72,
                ErrorCode.INVALID_USER_PASSWORD
        );
        PreConditions.validate(
                !memberRepository.existsByLoginId(loginId),
                ErrorCode.USER_ID_ALREADY_EXISTS
        );

        memberRepository.save(
                Members.builder()
                        .name(name)
                        .password(passwordEncoder.encode(request.password()))
                        .loginId(loginId)
                        .build()
        );
    }
}

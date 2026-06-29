package io.backend.blogproject.repository;

import io.backend.blogproject.common.CustomException;
import io.backend.blogproject.constant.ErrorCode;
import io.backend.blogproject.domain.entity.Members;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Members, Long> {
    default Members findByIdOrThrow(Long memberId){
        return findById(memberId).orElseThrow(
                ()-> new CustomException(ErrorCode.MEMBER_NOT_FOUND)
        );
    }

    Boolean existsByLoginId(String loginId);

    Optional<Members> findByLoginId(String loginId);

    default Members findByLoginIdOrThrow(String loginId){
        return findByLoginId(loginId).orElseThrow(
                ()-> new CustomException(ErrorCode.MEMBER_NOT_FOUND)
        );
    }
}

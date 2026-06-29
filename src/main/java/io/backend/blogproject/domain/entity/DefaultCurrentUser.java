package io.backend.blogproject.domain.entity;

import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@Accessors(chain = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DefaultCurrentUser implements UserDetails {
    @Setter
    private Long userId;
    private String username;
    private String password;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Builder
    private DefaultCurrentUser(
            String username,
            String password
    ){
        this.username = username;
        this.password = password;
    }

    public static DefaultCurrentUser from(Members member){
        return new DefaultCurrentUser(
                member.getLoginId(),
                member.getPassword()
        ).setUserId(member.getMemberId());
    }
}

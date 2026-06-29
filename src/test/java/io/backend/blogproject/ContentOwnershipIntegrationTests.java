package io.backend.blogproject;

import io.backend.blogproject.common.CustomException;
import io.backend.blogproject.constant.ErrorCode;
import io.backend.blogproject.constant.Visibility;
import io.backend.blogproject.domain.dto.CommentRequest;
import io.backend.blogproject.domain.dto.PostRequest;
import io.backend.blogproject.domain.entity.Comment;
import io.backend.blogproject.domain.entity.Members;
import io.backend.blogproject.repository.CommentRepository;
import io.backend.blogproject.repository.MemberRepository;
import io.backend.blogproject.repository.PostRepository;
import io.backend.blogproject.service.CommentService;
import io.backend.blogproject.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:ownership-test;MODE=MySQL;DB_CLOSE_DELAY=-1",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.sql.init.mode=never",
        "custom.BASE_URL=http://localhost:8080",
        "custom.appkey=01234567890123456789012345678901",
        "custom.validations.access=3600000",
        "custom.validations.refresh=86400000"
})
@Transactional
@AutoConfigureMockMvc
class ContentOwnershipIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostService postService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Members owner;
    private Members otherMember;
    private Long postId;

    @BeforeEach
    void setUp() {
        commentRepository.deleteAll();
        postRepository.deleteAll();
        memberRepository.deleteAll();

        owner = memberRepository.save(member("owner", "owner-login"));
        otherMember = memberRepository.save(member("other", "other-login"));
        postId = postService.createPost(
                owner.getMemberId(),
                new PostRequest.Create("title", "content", Visibility.PUBLIC, null)
        );
    }

    @Test
    void onlyPostOwnerCanUpdateAndDelete() {
        PostRequest.Update update = new PostRequest.Update(
                "updated", "updated content", Visibility.PUBLIC, null
        );

        assertDoesNotThrow(() -> postService.updatePost(owner.getMemberId(), postId, update));
        assertEquals("updated", postRepository.findById(postId).orElseThrow().getTitle());

        CustomException updateException = assertThrows(
                CustomException.class,
                () -> postService.updatePost(otherMember.getMemberId(), postId, update)
        );
        assertEquals(ErrorCode.POST_ACCESS_DENIED, updateException.getErrorCode());

        CustomException deleteException = assertThrows(
                CustomException.class,
                () -> postService.deletePost(otherMember.getMemberId(), postId)
        );
        assertEquals(ErrorCode.POST_ACCESS_DENIED, deleteException.getErrorCode());

        assertDoesNotThrow(() -> postService.deletePost(owner.getMemberId(), postId));
    }

    @Test
    void onlyCommentOwnerCanUpdateAndDelete() {
        commentService.createComment(
                owner.getMemberId(),
                postId,
                new CommentRequest.Create("comment")
        );
        Comment comment = commentRepository.findAll().getFirst();

        CustomException updateException = assertThrows(
                CustomException.class,
                () -> commentService.updateComment(
                        otherMember.getMemberId(),
                        comment.getId(),
                        new CommentRequest.Update("hacked")
                )
        );
        assertEquals(ErrorCode.COMMENT_ACCESS_DENIED, updateException.getErrorCode());

        assertDoesNotThrow(() -> commentService.updateComment(
                owner.getMemberId(),
                comment.getId(),
                new CommentRequest.Update("updated comment")
        ));
        assertEquals("updated comment", commentRepository.findById(comment.getId()).orElseThrow().getContent());

        CustomException deleteException = assertThrows(
                CustomException.class,
                () -> commentService.deleteComment(otherMember.getMemberId(), comment.getId())
        );
        assertEquals(ErrorCode.COMMENT_ACCESS_DENIED, deleteException.getErrorCode());

        assertDoesNotThrow(() -> commentService.deleteComment(owner.getMemberId(), comment.getId()));
    }

    @Test
    void postUpdateEndpointReturnsForbiddenForNonOwner() throws Exception {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                io.backend.blogproject.domain.entity.DefaultCurrentUser.from(otherMember),
                null,
                List.of()
        );

        mockMvc.perform(put("/posts/{postId}", postId)
                        .with(authentication(authentication))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "title": "hacked",
                                  "content": "hacked",
                                  "visibility": "PUBLIC",
                                  "categoryId": null
                                }
                                """))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value(ErrorCode.POST_ACCESS_DENIED.getMessage()));
    }

    @Test
    void anonymousHtmlRequestRedirectsToLogin() throws Exception {
        mockMvc.perform(get("/login").accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk());

        mockMvc.perform(get("/posts/{postId}", postId)
                        .accept(MediaType.TEXT_HTML))
                .andExpect(status().isFound())
                .andExpect(result -> assertEquals(
                        "/login?redirect=%2Fposts%2F" + postId,
                        result.getResponse().getRedirectedUrl()
                ));
    }

    @Test
    void loginIssuesJwtCookieAndCookieAuthenticatesPageRequest() throws Exception {
        MvcResult loginResult = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "userId": "owner-login",
                                  "password": "password"
                                }
                                """))
                .andExpect(status().isOk())
                .andReturn();

        jakarta.servlet.http.Cookie accessToken = loginResult.getResponse().getCookie("access_token");
        org.junit.jupiter.api.Assertions.assertNotNull(accessToken);
        org.junit.jupiter.api.Assertions.assertTrue(accessToken.isHttpOnly());

        mockMvc.perform(get("/posts").cookie(accessToken).accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/auth/logout").cookie(accessToken))
                .andExpect(status().isSeeOther())
                .andExpect(result -> {
                    jakarta.servlet.http.Cookie expired = result.getResponse().getCookie("access_token");
                    org.junit.jupiter.api.Assertions.assertNotNull(expired);
                    assertEquals(0, expired.getMaxAge());
                });
    }

    @Test
    void signupPageCreatesEncodedMemberAndNewMemberCanLogin() throws Exception {
        mockMvc.perform(get("/signup").accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "id": "new-member",
                                  "password": "new-password-123",
                                  "name": "새회원"
                                }
                                """))
                .andExpect(status().isCreated());

        Members createdMember = memberRepository.findByLoginId("new-member").orElseThrow();
        org.junit.jupiter.api.Assertions.assertTrue(
                passwordEncoder.matches("new-password-123", createdMember.getPassword())
        );

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "userId": "new-member",
                                  "password": "new-password-123"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(result -> org.junit.jupiter.api.Assertions.assertNotNull(
                        result.getResponse().getCookie("access_token")
                ));

        mockMvc.perform(post("/api/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "id": "new-member",
                                  "password": "new-password-123",
                                  "name": "다른이름"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(ErrorCode.USER_ID_ALREADY_EXISTS.getMessage()));
    }

    @Test
    void signupRejectsInvalidInput() throws Exception {
        mockMvc.perform(post("/api/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "id": "a",
                                  "password": "short",
                                  "name": ""
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(ErrorCode.INVALID_USER_ID.getMessage()));
    }

    private Members member(String name, String loginId) {
        return Members.builder()
                .name(name)
                .loginId(loginId)
                .password(passwordEncoder.encode("password"))
                .build();
    }
}

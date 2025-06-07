package chloe.godokbang.dto.request;

import chloe.godokbang.domain.User;
import chloe.godokbang.domain.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class JoinRequest {

    @NotBlank(message = "이메일이 입력되지 않았습니다.")
    @Email(message = "이메일 형식에 맞지 않습니다.")
    private String email;

    @NotBlank(message = "비밀번호가 입력되지 않았습니다.")
    private String password;

    @NotBlank(message = "비밀번호 확인이 입력되지 않았습니다.")
    private String passwordCheck;

    @NotBlank(message = "닉네임이 입력되지 않았습니다.")
    private String nickname;

    @Builder
    public JoinRequest(String email, String password, String passwordCheck, String nickname) {
        this.email = email;
        this.password = password;
        this.passwordCheck = passwordCheck;
        this.nickname = nickname;
    }

    public User toEntity(String encodedPassword) {
        return User.builder()
                .email(this.email)
                .passwordHash(encodedPassword)
                .nickname(this.nickname)
                .role(UserRole.USER)
                .build();
    }
}

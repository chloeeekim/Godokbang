package chloe.godokbang.dto.request;

import chloe.godokbang.domain.User;
import chloe.godokbang.domain.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class JoinRequest {

    @NotBlank(message = "Email is required.")
    @Email(message = "Email has to be an email address.")
    private String email;

    @NotBlank(message = "Password is required.")
    private String password;

    @NotBlank(message = "Password confirm is required.")
    private String passwordCheck;

    @NotBlank(message = "Nickname is required.")
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

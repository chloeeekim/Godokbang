package chloe.godokbang.service;

import chloe.godokbang.domain.User;
import chloe.godokbang.dto.request.JoinRequest;
import chloe.godokbang.dto.request.LoginRequest;
import chloe.godokbang.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    /**
     * email 중복 체크
     * @param email 이메일
     * @return 이메일 중복 여부
     */
    public boolean checkLoginIdExists(String email) {
        return userRepository.existsByEmail(email);
    }

    /**
     * nickname 중복 체크
     * @param nickname 닉네임
     * @return 닉네임 중복 여부
     */
    public boolean checkNicknameExists(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    /**
     * 회원가입 (비밀번호 암호화)
     * @param request 회원가입 request dto
     */
    public void join(JoinRequest request) {
        userRepository.save(request.toEntity(encoder.encode(request.getPassword())));
    }

    /**
     * 로그인
     * @param request 로그인 request dto
     * @return User; email이 존재하지 않거나 password가 일치하지 않는 경우, null
     */
    public User login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail()).get();
        String encodedPassword = (user == null) ? "" : user.getPasswordHash();

        if (user == null || encoder.matches(request.getPassword(), encodedPassword)) {
            return null;
        }

        return user;
    }
}

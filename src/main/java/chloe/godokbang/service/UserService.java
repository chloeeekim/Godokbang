package chloe.godokbang.service;

import chloe.godokbang.domain.User;
import chloe.godokbang.dto.request.JoinRequest;
import chloe.godokbang.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

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
        String[] defaultProfiles = {"red", "pink", "yellow", "purple", "blue", "green"};

        Random random = new Random();
        int idx = random.nextInt(defaultProfiles.length);
        String selected = String.format("https://chloe-godokbang.s3.ap-southeast-2.amazonaws.com/profile/profile-%s.jpg", defaultProfiles[idx]);

        User user = request.toEntity(encoder.encode(request.getPassword()));
        user.updateUserProfile(selected);

        userRepository.save(user);
    }
}

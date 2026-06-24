package com.example.Shop.domain; //이미 가입된 이메일인지 확인하고, 중복이 없다면 DB에 저장
import com.example.Shop.dto.UserSignupRequest;
import com.example.Shop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    /**
     * 회원가입
     */
    @Transactional
    public Long signUp(UserSignupRequest request) {
        // 1. 이메일 중복 검증
        userRepository.findByEmail(request.getEmail())
                .ifPresent(user -> {
                    throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
                });

        // 2. 유저 엔티티 생성 및 저장 (실무에서는 비밀번호를 암호화해야 하지만, 우선 흐름을 보기 위해 평문으로 저장)
        User user = User.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .name(request.getName())
                .build();

        User savedUser = userRepository.save(user);
        return savedUser.getId();
    }
    public boolean login(String email, String password) {
        // Optional에서 값을 꺼내는 과정.
        // 이메일로 찾고, 없으면 empty(비어있는) User 객체를 반환.
        return userRepository.findByEmail(email)
                .map(user -> user.getPassword().equals(password)) // 사용자가 있으면 비번 비교
                .orElse(false); // 사용자가 아예 없으면 false 반환
    }

    public User getUserByEmail(String email){
        return userRepository.findByEmail(email).orElse(null); // 이메일로 유저를 찾고 없으면 null 반환
    }
}
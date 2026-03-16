package com.example.Back_end.validation;

import com.example.Back_end.repository.MemberRepository;
import com.example.Back_end.repository.StaffRepository;
import com.example.Back_end.repository.SupporterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserRoleValidator {
    private final MemberRepository memberRepository;
    private final StaffRepository staffRepository;
    private final SupporterRepository supporterRepository;

    /**
     * Kiểm tra nếu user đã được gán bất kỳ vai trò nào khác.
     * @throws IllegalStateException nếu user đã có vai trò khác
     */
    public void ensureUserHasNoOtherRole(Long userId, String currentRole) {
        boolean isMember = memberRepository.existsByUser_UserId(userId);
        boolean isStaff = staffRepository.existsByUser_UserId(userId);
        boolean isSupporter = supporterRepository.existsByUser_UserId(userId);

        switch (currentRole.toLowerCase()) {
            case "member" -> {
                if (isStaff || isSupporter)
                    throw new IllegalStateException("User already has another role (Staff/Supporter)");
            }
            case "staff" -> {
                if (isMember || isSupporter)
                    throw new IllegalStateException("User already has another role (Member/Supporter)");
            }
            case "supporter" -> {
                if (isMember || isStaff)
                    throw new IllegalStateException("User already has another role (Member/Staff)");
            }
        }
    }
}

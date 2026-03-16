package com.example.Back_end.service.impl;

import com.example.Back_end.dto.MemberRequestDTO;
import com.example.Back_end.dto.MemberResponseDTO;
import com.example.Back_end.dto.UserAssignDTO;
import com.example.Back_end.entity.Lab;
import com.example.Back_end.entity.Member;
import com.example.Back_end.entity.User;
import com.example.Back_end.repository.LabRepository;
import com.example.Back_end.repository.MemberRepository;
import com.example.Back_end.repository.UserRepository;
import com.example.Back_end.service.interf.LabService;
import com.example.Back_end.service.interf.MemberService;
import com.example.Back_end.validation.UserRoleValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final UserRepository userRepository;
    private final LabRepository labRepository;
    private final UserRoleValidator userRoleValidator;

    @Override
    public MemberResponseDTO create(MemberRequestDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Lab lab = labRepository.findById(dto.getLabId())
                .orElseThrow(() -> new RuntimeException("Lab not found"));

        Member member = new Member();
        member.setUser(user);
        member.setLabs(List.of(lab)); // gán 1 lab nhưng entity vẫn ManyToMany
        member.setMemberCode(dto.getMemberCode());
        member.setRole(dto.getRole());
        member.setJoinedAt(LocalDateTime.now());

        memberRepository.save(member);
        return toDto(member);
    }

    @Override
    public MemberResponseDTO getById(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Member not found"));
        return toDto(member);
    }

    @Override
    public List<MemberResponseDTO> getAll() {
        return memberRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public MemberResponseDTO update(Long id, MemberRequestDTO dto) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        if (dto.getLabId() != null) {
            Lab lab = labRepository.findById(dto.getLabId())
                    .orElseThrow(() -> new RuntimeException("Lab not found"));
            member.setLabs(List.of(lab));
        }

        member.setMemberCode(dto.getMemberCode());
        member.setRole(dto.getRole());

        memberRepository.save(member);
        return toDto(member);
    }

    @Override
    public void delete(Long id) {
        memberRepository.deleteById(id);
    }

    private MemberResponseDTO toDto(Member member) {
        MemberResponseDTO dto = new MemberResponseDTO();
        dto.setMemberId(member.getMemberId());
        dto.setUserId(member.getUser().getUserId());
        dto.setLabIds(
                member.getLabs() != null
                        ? member.getLabs().stream().map(Lab::getLabId).toList()
                        : List.of()
        );
        dto.setMemberCode(member.getMemberCode());
        dto.setRole(member.getRole());
        dto.setJoinedAt(member.getJoinedAt());
        return dto;
    }

    @Transactional
    @Override
    public MemberResponseDTO assignUserToLab(UserAssignDTO request) {
        userRoleValidator.ensureUserHasNoOtherRole(request.getUserId(), "member");

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Lab lab = labRepository.findById(request.getLabId())
                .orElseThrow(() -> new RuntimeException("Lab not found"));

        Member member = new Member();
        member.setUser(user);
        member.setLabs(List.of(lab));
        member.setMemberCode("MEM-" + System.currentTimeMillis());
        member.setRole("Member");
        member.setJoinedAt(LocalDateTime.now());

        Member saved = memberRepository.save(member);
        return toDto(saved);
    }

}


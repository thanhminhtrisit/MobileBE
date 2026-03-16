package com.example.Back_end.controller;

import com.example.Back_end.dto.MemberResponseDTO;
import com.example.Back_end.dto.StaffResponseDTO;
import com.example.Back_end.dto.SupporterResponseDTO;
import com.example.Back_end.dto.UserAssignDTO;
import com.example.Back_end.service.interf.MemberService;
import com.example.Back_end.service.interf.StaffService;
import com.example.Back_end.service.interf.SupporterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/assign")
@RequiredArgsConstructor
public class AssignController {

    private final MemberService memberService;
    private final StaffService staffService;
    private final SupporterService supporterService;

    @PostMapping("/member")
    public ResponseEntity<MemberResponseDTO> assignMember(@RequestBody UserAssignDTO request) {
        return ResponseEntity.ok(memberService.assignUserToLab(request));
    }

    @PostMapping("/staff")
    public ResponseEntity<StaffResponseDTO> assignStaff(@RequestBody UserAssignDTO request) {
        return ResponseEntity.ok(staffService.assignUserToLab(request));
    }

    @PostMapping("/supporter")
    public ResponseEntity<SupporterResponseDTO> assignSupporter(@RequestBody UserAssignDTO request) {
        return ResponseEntity.ok(supporterService.assignUser(request));
    }
}

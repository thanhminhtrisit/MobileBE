package com.example.Back_end.controller;

import com.example.Back_end.dto.MemberRequestDTO;
import com.example.Back_end.dto.MemberResponseDTO;
import com.example.Back_end.service.interf.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    public MemberResponseDTO create(@RequestBody MemberRequestDTO dto) {
        return memberService.create(dto);
    }

    @GetMapping("/{id}")
    public MemberResponseDTO get(@PathVariable Long id) {
        return memberService.getById(id);
    }

    @GetMapping
    public List<MemberResponseDTO> getAll() {
        return memberService.getAll();
    }

    @PutMapping("/{id}")
    public MemberResponseDTO update(@PathVariable Long id, @RequestBody MemberRequestDTO dto) {
        return memberService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        memberService.delete(id);
    }
}


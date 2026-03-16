package com.example.Back_end.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class MemberResponseDTO {
    private Long memberId;
    private Long userId;
    private List<Long> labIds;
    private String memberCode;
    private String role;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime joinedAt;
}

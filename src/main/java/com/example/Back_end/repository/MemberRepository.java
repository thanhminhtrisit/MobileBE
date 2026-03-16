package com.example.Back_end.repository;

import com.example.Back_end.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByUser_UserId(Long userId);
}


package com.example.Back_end.repository;

import com.example.Back_end.entity.Supporter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SupporterRepository extends JpaRepository<Supporter, Long> {
    boolean existsByUser_UserId(Long userId);
}

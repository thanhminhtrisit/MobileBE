package com.example.Back_end.repository;

import com.example.Back_end.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByMember_MemberId(Long memberId);
    List<Notification> findByStaff_StaffId(Long staffId);
    List<Notification> findBySupporter_SupporterId(Long supporterId);
}

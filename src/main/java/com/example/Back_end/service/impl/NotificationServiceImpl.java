package com.example.Back_end.service.impl;

import com.example.Back_end.dto.NotificationDTO;
import com.example.Back_end.entity.*;
import com.example.Back_end.repository.NotificationRepository;
import com.example.Back_end.repository.UserRepository;
import com.example.Back_end.service.interf.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepo;
    private final UserRepository userRepo;

    @Override
    public void notifyMember(Member member, String title, String message) {
        if (member == null) return;
        Notification n = new Notification();
        n.setTitle(title);
        n.setMessage(message);
        n.setMember(member);
        notificationRepo.save(n);
    }

    @Override
    public void notifyStaff(Staff staff, String title, String message) {
        if (staff == null) return;
        Notification n = new Notification();
        n.setTitle(title);
        n.setMessage(message);
        n.setStaff(staff);
        notificationRepo.save(n);
    }

    @Override
    public void notifySupporter(Supporter supporter, String title, String message) {
        if (supporter == null) return;
        Notification n = new Notification();
        n.setTitle(title);
        n.setMessage(message);
        n.setSupporter(supporter);
        notificationRepo.save(n);
    }

    @Override
    public List<NotificationDTO> getMemberNotifications(Long memberId) {
        return notificationRepo.findByMember_MemberId(memberId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<NotificationDTO> getStaffNotifications(Long staffId) {
        return notificationRepo.findByStaff_StaffId(staffId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<NotificationDTO> getSupporterNotifications(Long supporterId) {
        return notificationRepo.findBySupporter_SupporterId(supporterId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<NotificationDTO> getUserNotifications(Long userId) {
        User user = userRepo.findById(userId).orElse(null);
        if (user == null) return Collections.emptyList();

        if (user.getMember() != null) {
            return notificationRepo.findByMember_MemberId(user.getMember().getMemberId())
                    .stream().map(this::toDTO).collect(Collectors.toList());
        }
        if (user.getStaff() != null) {
            return notificationRepo.findByStaff_StaffId(user.getStaff().getStaffId())
                    .stream().map(this::toDTO).collect(Collectors.toList());
        }
        if (user.getSupporter() != null) {
            return notificationRepo.findBySupporter_SupporterId(user.getSupporter().getSupporterId())
                    .stream().map(this::toDTO).collect(Collectors.toList());
        }

        return Collections.emptyList();
    }

    private NotificationDTO toDTO(Notification n) {
        NotificationDTO dto = new NotificationDTO();
        dto.setNotificationId(n.getNotificationId());
        dto.setTitle(n.getTitle());
        dto.setMessage(n.getMessage());
        dto.setIsRead(n.getIsRead());
        dto.setCreatedAt(n.getCreatedAt());
        dto.setMemberId(n.getMember() != null ? n.getMember().getMemberId() : null);
        dto.setStaffId(n.getStaff() != null ? n.getStaff().getStaffId() : null);
        dto.setSupporterId(n.getSupporter() != null ? n.getSupporter().getSupporterId() : null);
        return dto;
    }

}

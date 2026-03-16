package com.example.Back_end.service.interf;

import com.example.Back_end.dto.NotificationDTO;
import com.example.Back_end.entity.Member;
import com.example.Back_end.entity.Staff;
import com.example.Back_end.entity.Supporter;

import java.util.List;

public interface NotificationService {
    void notifyMember(Member member, String title, String message);
    void notifyStaff(Staff staff, String title, String message);
    void notifySupporter(Supporter supporter, String title, String message);

    List<NotificationDTO> getMemberNotifications(Long memberId);
    List<NotificationDTO> getStaffNotifications(Long staffId);
    List<NotificationDTO> getSupporterNotifications(Long supporterId);
    List<NotificationDTO> getUserNotifications(Long userId);

}

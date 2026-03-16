package com.example.Back_end.enums;

public enum RequestStatus {
    PENDING,        // Đang chờ duyệt
    APPROVED,       // Đã duyệt
    REJECTED,       // Bị từ chối
    IN_PROGRESS,    // Đang xử lý
    COMPLETED,      // Đã hoàn tất
    CANCELLED       // Đã hủy
}

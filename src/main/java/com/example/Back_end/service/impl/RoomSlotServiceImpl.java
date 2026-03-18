package com.example.Back_end.service.impl;

import com.example.Back_end.dto.RoomSlotRequestDTO;
import com.example.Back_end.dto.RoomSlotResponseDTO;
import com.example.Back_end.entity.RoomSlot;
import com.example.Back_end.repository.RoomSlotRepository;
import com.example.Back_end.service.interf.RoomSlotService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomSlotServiceImpl implements RoomSlotService {

    private final RoomSlotRepository roomSlotRepository;


    @Override
    public List<RoomSlotResponseDTO> getDistinctSlotTemplates() {
        // Lấy tất cả slots (tận dụng hàm getAll có sẵn)
        List<RoomSlotResponseDTO> allSlots = getAll();

        // Gom nhóm theo slotName + startTime + endTime (loại bỏ trùng)
        return allSlots.stream()
                .collect(Collectors.collectingAndThen(
                        Collectors.toMap(
                                s -> s.getSlotName() + s.getStartTime() + s.getEndTime(), // key duy nhất
                                s -> s,
                                (s1, s2) -> s1 // nếu trùng thì giữ cái đầu tiên
                        ),
                        map -> map.values().stream()
                                .sorted(Comparator.comparing(RoomSlotResponseDTO::getStartTime))
                                .toList()
                ));
    }


    @Override
    public List<RoomSlotResponseDTO> getAll() {
        return roomSlotRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public RoomSlotResponseDTO getById(Long id) {
        RoomSlot rs = roomSlotRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("RoomSlot not found"));
        return toResponse(rs);
    }

    @Override
    public RoomSlotResponseDTO create(RoomSlotRequestDTO dto) {
        RoomSlot rs = new RoomSlot();
        rs.setSlotName(dto.getSlotName());
        rs.setStartTime(dto.getStartTime());
        rs.setEndTime(dto.getEndTime());
        rs.setBookingDate(dto.getBookingDate());
        rs.setStatus(dto.getStatus());
        rs.setIsAvailable(dto.getIsAvailable() != null ? dto.getIsAvailable() : true);
        return toResponse(roomSlotRepository.save(rs));
    }

    @Override
    public RoomSlotResponseDTO update(Long id, RoomSlotRequestDTO dto) {
        RoomSlot rs = roomSlotRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("RoomSlot not found"));

        rs.setSlotName(dto.getSlotName());
        rs.setStartTime(dto.getStartTime());
        rs.setEndTime(dto.getEndTime());
        rs.setBookingDate(dto.getBookingDate());
        rs.setStatus(dto.getStatus());
        rs.setIsAvailable(dto.getIsAvailable());
        return toResponse(roomSlotRepository.save(rs));
    }

    @Override
    public void delete(Long id) {
        roomSlotRepository.deleteById(id);
    }

    @Override
    public List<RoomSlotResponseDTO> getAvailableSlots() {
        return roomSlotRepository.findByIsAvailableTrue()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ✅ Sinh room slot cho năm bất kỳ
    @Override
    @Transactional
    public String generateRoomSlotsForYear(int year) {
        LocalDate startDate = LocalDate.of(year, 1, 1);
        LocalDate endDate = LocalDate.of(year, 12, 31);

        // Kiểm tra đã tồn tại chưa
        if (roomSlotRepository.existsByBookingDateBetween(startDate, endDate)) {
            return "Warning: Năm " + year + " đã có RoomSlot trong DB!";
        }

        List<RoomSlot> slotsToSave = new ArrayList<>();
        String[] slotNames = {"Ca 1", "Ca 2", "Ca 3", "Ca 4", "Ca 5"};
        LocalTime[] startTimes = {LocalTime.of(7,0), LocalTime.of(9,0), LocalTime.of(13,0), LocalTime.of(15,0), LocalTime.of(18,0)};
        LocalTime[] endTimes   = {LocalTime.of(9,0), LocalTime.of(11,0), LocalTime.of(15,0), LocalTime.of(17,0), LocalTime.of(20,0)};

        System.out.println("🔄 Đang tạo RoomSlot cho năm " + year + "...");

        int count = 0;
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            for (int i = 0; i < 5; i++) {
                RoomSlot rs = new RoomSlot();
                rs.setSlotName(slotNames[i]);
                rs.setStartTime(startTimes[i]);
                rs.setEndTime(endTimes[i]);
                rs.setBookingDate(date);
                rs.setStatus("AVAILABLE");
                rs.setIsAvailable(true);
                slotsToSave.add(rs);

                count++;
                if (count % 500 == 0) {
                    System.out.println("✅ Đã tạo " + count + " slots...");
                }
            }
        }

        // Batch insert an toàn
        roomSlotRepository.saveAll(slotsToSave);
        roomSlotRepository.flush();

        System.out.println("🎉 Hoàn thành! Đã tạo " + slotsToSave.size() + " RoomSlot cho năm " + year);
        return "Success: Đã tạo " + slotsToSave.size() + " RoomSlots cho năm " + year + "!";
    }

    @Override
    public String updateStatusByDate(LocalDate date, String status) {
        List<RoomSlot> slots = roomSlotRepository.findByBookingDate(date);

        if (slots.isEmpty()) {
            return "⚠️ Không tìm thấy RoomSlot nào cho ngày " + date;
        }

        for (RoomSlot rs : slots) {
            rs.setStatus(status);
            // Nếu status không phải AVAILABLE thì set isAvailable = false
            rs.setIsAvailable("AVAILABLE".equalsIgnoreCase(status));
        }

        roomSlotRepository.saveAll(slots);
        return "✅ Đã cập nhật " + slots.size() + " RoomSlots cho ngày " + date + " thành trạng thái " + status + "!";
    }

    // Helper
    private RoomSlotResponseDTO toResponse(RoomSlot rs) {
        RoomSlotResponseDTO dto = new RoomSlotResponseDTO();
        dto.setRoomSlotId(rs.getRoomSlotId());
        dto.setSlotName(rs.getSlotName());
        dto.setStartTime(rs.getStartTime());
        dto.setEndTime(rs.getEndTime());
        dto.setBookingDate(rs.getBookingDate());
        dto.setStatus(rs.getStatus());
        dto.setIsAvailable(rs.getIsAvailable());
        return dto;
    }
}

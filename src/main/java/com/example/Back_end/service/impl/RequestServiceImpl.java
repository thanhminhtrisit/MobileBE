package com.example.Back_end.service.impl;

import com.example.Back_end.dto.RequestRequestDTO;
import com.example.Back_end.dto.RequestResponseDTO;
import com.example.Back_end.entity.*;
import com.example.Back_end.enums.RequestStatus;
import com.example.Back_end.enums.RequestTypeEnum;
import com.example.Back_end.repository.*;
import com.example.Back_end.service.interf.NotificationService;
import com.example.Back_end.service.interf.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepo;
    private final MemberRepository memberRepo;
    private final StaffRepository staffRepo;
    private final LabRepository labRepo;
    private final RoomRepository roomRepo;
    private final RoomSlotRepository roomSlotRepo;
    private final SupporterShiftRepository supporterShiftRepo;
    private final NotificationService notificationService;
    private final UserRepository userRepo;

    // =======================================================================
    //                               CRUD
    // =======================================================================

    @Override
    public List<RequestResponseDTO> getAll() {
        return requestRepo.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public RequestResponseDTO getById(Long id) {
        return requestRepo.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new RuntimeException("Request not found"));
    }

    @Override
    public RequestResponseDTO create(RequestRequestDTO dto) {
        Request request = new Request();

        request.setTitle(dto.getTitle());
        request.setDescription(dto.getDescription());
        request.setRequestedAt(LocalDateTime.now());
        request.setStatus(RequestStatus.PENDING);

        // ✅ Parse enum from string
        try {
            request.setRequestType(RequestTypeEnum.valueOf(dto.getRequestType().toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid request type: " + dto.getRequestType());
        }

        request.setMember(memberRepo.findById(dto.getMemberId())
                .orElseThrow(() -> new RuntimeException("Member not found")));
        request.setLab(labRepo.findById(dto.getLabId())
                .orElseThrow(() -> new RuntimeException("Lab not found")));

        // ------------------------------------------------------------
        // CASE 1: BOOKING
        // ------------------------------------------------------------
        if (request.getRequestType() == RequestTypeEnum.BOOKING && dto.getRoomSlotIds() != null) {
            request.setRoomSlots(dto.getRoomSlotIds().stream()
                    .map(id -> roomSlotRepo.findById(id)
                            .orElseThrow(() -> new RuntimeException("RoomSlot not found")))
                    .collect(Collectors.toList()));

            Staff staff = staffRepo.findFirstByLabs_LabId(request.getLab().getLabId()).orElse(null);
            if (staff != null) {
                request.setStaff(staff);
            }
        }

        // ------------------------------------------------------------
        // CASE 2: OPEN_DOOR
        // ------------------------------------------------------------
        else if (request.getRequestType() == RequestTypeEnum.OPEN_DOOR
                && dto.getRoomSlotIds() != null && !dto.getRoomSlotIds().isEmpty()) {

            RoomSlot rs = roomSlotRepo.findById(dto.getRoomSlotIds().get(0))
                    .orElseThrow(() -> new RuntimeException("RoomSlot not found"));

            Supporter supporter = findAvailableSupporterForRoomSlot(rs);
            if (supporter != null) {
                request.setSupporter(supporter);
            }
        }

        // ✅ Notify staff
        Staff notifyStaff = staffRepo.findFirstByLabs_LabId(request.getLab().getLabId()).orElse(null);
        if (notifyStaff != null) {
            notificationService.notifyStaff(
                    notifyStaff,
                    "New Request from " + request.getMember().getMemberCode(),
                    "Request Type: " + request.getRequestType() + " - " + request.getTitle()
            );
        }

        return toResponse(requestRepo.save(request));
    }

    @Override
    public RequestResponseDTO update(Long id, RequestRequestDTO dto) {
        Request request = requestRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        RequestStatus newStatus = RequestStatus.valueOf(dto.getStatus().toUpperCase());
        request.setStatus(newStatus);

        // ------------------------------------------------------------
        // APPROVED
        // ------------------------------------------------------------
        if (newStatus == RequestStatus.APPROVED) {
            request.setApprovedAt(LocalDateTime.now());

            if (request.getRequestType() == RequestTypeEnum.BOOKING) {
                if (dto.getRoomId() != null) {
                    Room room = roomRepo.findById(dto.getRoomId())
                            .orElseThrow(() -> new RuntimeException("Room not found"));

                    if ("IN_USE".equalsIgnoreCase(room.getStatus())) {
                        throw new RuntimeException("This room is currently in use. Cannot approve another request!");
                    }

                    if (dto.getRoomSlotIds() != null && !dto.getRoomSlotIds().isEmpty()) {
                        RoomSlot rs = roomSlotRepo.findById(dto.getRoomSlotIds().get(0))
                                .orElseThrow(() -> new RuntimeException("RoomSlot not found"));

                        Supporter supporter = findAvailableSupporterForRoomSlot(rs);
                        if (supporter == null) {
                            throw new RuntimeException("No supporter available for this slot — approval failed!");
                        }
                        request.setSupporter(supporter);
                    }

                    room.setStatus("IN_USE");
                    roomRepo.save(room);

                    request.setRoom(room);
                }

                Staff staff = staffRepo.findFirstByLabs_LabId(request.getLab().getLabId()).orElse(null);
                if (staff != null) {
                    request.setStaff(staff);
                }
            }
            else if (request.getRequestType() == RequestTypeEnum.OPEN_DOOR
                    && dto.getRoomSlotIds() != null && !dto.getRoomSlotIds().isEmpty()) {

                RoomSlot rs = roomSlotRepo.findById(dto.getRoomSlotIds().get(0))
                        .orElseThrow(() -> new RuntimeException("RoomSlot not found"));

                Supporter supporter = findAvailableSupporterForRoomSlot(rs);
                if (supporter != null) {
                    request.setSupporter(supporter);
                }
            }
        }

        // ------------------------------------------------------------
        // REJECTED
        // ------------------------------------------------------------
        else if (newStatus == RequestStatus.REJECTED) {
            if (request.getRoom() != null) {
                Room room = request.getRoom();
                room.setStatus("ACTIVE"); // ✅ Release room
                roomRepo.save(room);
            }
            request.setRoom(null);
            request.setSupporter(null);
        }

        // ------------------------------------------------------------
        // COMPLETED
        // ------------------------------------------------------------
        else if (newStatus == RequestStatus.COMPLETED) {
            request.setCompletedAt(LocalDateTime.now());

            if (request.getRoom() != null) {
                Room room = request.getRoom();
                room.setStatus("ACTIVE");
                roomRepo.save(room);
            }
        }

        return toResponse(requestRepo.save(request));
    }

    @Override
    public void delete(Long id) {
        requestRepo.deleteById(id);
    }

    // =======================================================================
    //                           STAFF APPROVAL API
    // =======================================================================

    public RequestResponseDTO approveByStaff(Long staffId, Long requestId) {
        Request request = requestRepo.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        Staff staff = staffRepo.findById(staffId)
                .orElseThrow(() -> new RuntimeException("Staff not found"));

        boolean hasPermission = staff.getLabs().stream()
                .anyMatch(lab -> lab.getLabId().equals(request.getLab().getLabId()));
        if (!hasPermission) {
            throw new RuntimeException("Staff does not have permission to approve this request");
        }

        if (request.getRequestType() != RequestTypeEnum.BOOKING) {
            throw new RuntimeException("Only BOOKING type requests can be approved");
        }

        request.setStatus(RequestStatus.APPROVED);
        request.setApprovedAt(LocalDateTime.now());
        request.setStaff(staff);

        Room activeRoom = roomRepo.findFirstByLabAndStatus(request.getLab(), "ACTIVE")
                .orElseThrow(() -> new RuntimeException("No ACTIVE room available in the lab"));

        activeRoom.setStatus("IN_USE");
        roomRepo.save(activeRoom);

        request.setRoom(activeRoom);

        Supporter supporter = findAvailableSupporterForRequest(request);
        if (supporter == null) {
            throw new RuntimeException("No supporter available for this shift — approval failed!");
        }

        request.setSupporter(supporter);

        notificationService.notifySupporter(
                supporter,
                "Room access request",
                "A request has been approved for room " + activeRoom.getRoomName() +
                        " (" + request.getTitle() + ")"
        );

        notificationService.notifyMember(
                request.getMember(),
                "Your request has been approved",
                "Request '" + request.getTitle() + "' has been approved. Room: " + activeRoom.getRoomName()
        );

        return toResponse(requestRepo.save(request));
    }

    // =======================================================================
    //                           HELPER FUNCTIONS
    // =======================================================================

    private Supporter findAvailableSupporterForRequest(Request request) {
        if (request.getRoomSlots() == null || request.getRoomSlots().isEmpty()) return null;

        var bookingDate = request.getRoomSlots().get(0).getBookingDate();
        var minStart = request.getRoomSlots().stream()
                .map(RoomSlot::getStartTime)
                .min(LocalTime::compareTo)
                .orElse(null);
        var maxEnd = request.getRoomSlots().stream()
                .map(RoomSlot::getEndTime)
                .max(LocalTime::compareTo)
                .orElse(null);

        var shifts = supporterShiftRepo.findAvailableShiftsForSlot(bookingDate, minStart, maxEnd);
        if (shifts.isEmpty()) return null;

        var shift = shifts.get(0);
        if (shift.getSupporters() != null && !shift.getSupporters().isEmpty()) {
            return shift.getSupporters().get(0);
        }
        return null;
    }

    private Supporter findAvailableSupporterForRoomSlot(RoomSlot rs) {
        var shifts = supporterShiftRepo.findAvailableShiftsForSlot(
                rs.getBookingDate(), rs.getStartTime(), rs.getEndTime());
        if (shifts.isEmpty()) return null;

        var shift = shifts.get(0);
        if (shift.getSupporters() != null && !shift.getSupporters().isEmpty()) {
            return shift.getSupporters().get(0);
        }
        return null;
    }

    private RequestResponseDTO toResponse(Request r) {
        RequestResponseDTO dto = new RequestResponseDTO();
        dto.setRequestId(r.getRequestId());
        dto.setRequestType(r.getRequestType() != null ? r.getRequestType().name() : null);
        dto.setMemberName(r.getMember() != null ? r.getMember().getMemberCode() : null);
        dto.setStaffName(r.getStaff() != null ? r.getStaff().getStaffCode() : null);
        dto.setSupporterName(r.getSupporter() != null ? r.getSupporter().getSupporterCode() : null);
        dto.setLabName(r.getLab() != null ? r.getLab().getLabName() : null);
        dto.setRoomName(r.getRoom() != null ? r.getRoom().getRoomName() : null);
        dto.setTitle(r.getTitle());
        dto.setDescription(r.getDescription());
        dto.setStatus(r.getStatus() != null ? r.getStatus().name() : null);
        dto.setRequestedAt(r.getRequestedAt());
        dto.setApprovedAt(r.getApprovedAt());
        dto.setCompletedAt(r.getCompletedAt());

        dto.setRoomSlots(r.getRoomSlots() != null
                ? r.getRoomSlots().stream()
                .map(rs -> rs.getSlotName() + " (" + rs.getStartTime() + " - " + rs.getEndTime() + ") - " + rs.getBookingDate())
                .collect(Collectors.toList())
                : null);

        return dto;
    }

    @Override
    public RequestResponseDTO rejectByUser(Long userId, Long requestId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Request request = requestRepo.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        Staff staff = user.getStaff();
        Member member = user.getMember();
        Supporter supporter = user.getSupporter();

        if (staff == null && member == null && supporter == null) {
            throw new RuntimeException("User does not belong to any role: staff/member/supporter");
        }

        // ✅ STAFF
        if (staff != null) {
            boolean hasPermission = staff.getLabs().stream()
                    .anyMatch(lab -> lab.getLabId().equals(request.getLab().getLabId()));
            if (!hasPermission) {
                throw new RuntimeException("Staff has no permission to reject this request");
            }

            request.setStatus(RequestStatus.REJECTED);
            request.setApprovedAt(LocalDateTime.now());
            request.setStaff(staff);

            if (request.getRoom() != null) {
                Room room = request.getRoom();
                room.setStatus("ACTIVE");
                roomRepo.save(room);
                request.setRoom(null);
            }

            request.setSupporter(null);

            notificationService.notifyMember(
                    request.getMember(),
                    "Request rejected",
                    "Your request '" + request.getTitle() + "' was rejected by " + staff.getStaffCode()
            );

            return toResponse(requestRepo.save(request));
        }

        // ✅ MEMBER
        if (member != null) {
            if (!request.getMember().getMemberId().equals(member.getMemberId())) {
                throw new RuntimeException("You cannot cancel a request that does not belong to you");
            }

            request.setStatus(RequestStatus.CANCELLED);
            request.setCompletedAt(LocalDateTime.now());

            if (request.getRoom() != null) {
                Room room = request.getRoom();
                room.setStatus("ACTIVE");
                roomRepo.save(room);
            }

            notificationService.notifyStaff(
                    request.getStaff(),
                    "Request cancelled by member",
                    "Request '" + request.getTitle() + "' was cancelled by " + member.getMemberCode()
            );

            return toResponse(requestRepo.save(request));
        }

        // ✅ SUPPORTER
        if (supporter != null) {
            if (request.getSupporter() == null ||
                    !request.getSupporter().getSupporterId().equals(supporter.getSupporterId())) {
                throw new RuntimeException("You cannot reject a request not assigned to you");
            }

            request.setStatus(RequestStatus.REJECTED);
            request.setCompletedAt(LocalDateTime.now());

            notificationService.notifyStaff(
                    request.getStaff(),
                    "Request rejected by supporter",
                    "Supporter " + supporter.getSupporterCode() +
                            " has rejected request '" + request.getTitle() + "'"
            );

            return toResponse(requestRepo.save(request));
        }

        throw new RuntimeException("Unable to determine user role");
    }
}

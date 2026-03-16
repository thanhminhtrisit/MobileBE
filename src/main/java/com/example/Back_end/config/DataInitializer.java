package com.example.Back_end.config;

import com.example.Back_end.entity.Lab;
import com.example.Back_end.entity.Room;
import com.example.Back_end.enums.LabStatus;
import com.example.Back_end.repository.LabRepository;
import com.example.Back_end.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final LabRepository labRepository;
    private final RoomRepository roomRepository;

    @Override
    public void run(String... args) {

        // ✅ Seed Labs only if empty
        if (labRepository.count() == 0) {
            List<Lab> labs = List.of(
                    createLab("AI Research Lab", "AI-001", "Building A, Floor 3", "Lab for AI and Robotics research"),
                    createLab("BioTech Innovation Center", "BIO-002", "Building B, Floor 1", "Focused on biotechnology and genetic engineering experiments"),
                    createLab("Cybersecurity Operations Lab", "SEC-003", "Building C, Floor 2", "Practical security analysis, penetration testing and defense simulation"),
                    createLab("Green Energy Research Unit", "ENG-004", "Building D, Floor 4", "Developing sustainable and renewable energy systems"),
                    createLab("Data Science and Analytics Hub", "DS-005", "Building E, Floor 3", "Working on machine learning, AI analytics, and big data visualization"),
                    createLab("Human-Computer Interaction Studio", "HCI-006", "Building F, Floor 1", "Experimenting with UX/UI, AR/VR interfaces, and user experience testing"),
                    createLab("Quantum Computing Lab", "QC-007", "Building G, Floor 2", "Exploring quantum algorithms and quantum circuit simulations"),
                    createLab("Robotics and Automation Lab", "ROB-008", "Building H, Floor 5", "Developing autonomous robots and industrial automation systems"),
                    createLab("Environmental Monitoring Lab", "ENV-009", "Building I, Floor 2", "Analyzing air, water, and soil quality using sensor networks"),
                    createLab("Software Engineering Lab", "SWE-010", "Building J, Floor 4", "Focusing on agile development, DevOps, and software quality assurance")
            );

            labRepository.saveAll(labs);
            System.out.println("✅ 10 Labs seeded successfully!");
        } else {
            System.out.println("ℹ️ Labs already exist, skipping seed.");
        }

        // ✅ Seed Rooms only if empty
        if (roomRepository.count() == 0) {
            List<Lab> savedLabs = labRepository.findAll();
            if (savedLabs.size() < 10) {
                System.out.println("⚠️ Not enough labs to seed rooms.");
                return;
            }

            Lab lab1 = savedLabs.get(0);
            Lab lab2 = savedLabs.get(1);
            Lab lab3 = savedLabs.get(2);
            Lab lab4 = savedLabs.get(3);
            Lab lab5 = savedLabs.get(4);
            Lab lab6 = savedLabs.get(5);
            Lab lab7 = savedLabs.get(6);
            Lab lab8 = savedLabs.get(7);
            Lab lab9 = savedLabs.get(8);
            Lab lab10 = savedLabs.get(9);

            List<Room> rooms = new ArrayList<>();

            // Lab 1 - AI Research Lab
            rooms.add(createRoom(lab1, "Phòng Thực hành 101", "R101", 40));
            rooms.add(createRoom(lab1, "Phòng Thực hành 102", "R102", 25));
            rooms.add(createRoom(lab1, "Phòng Họp 103", "R103", 15));

            // Lab 2 - BioTech Innovation Center
            rooms.add(createRoom(lab2, "Phòng Thí nghiệm 201", "R201", 30));
            rooms.add(createRoom(lab2, "Phòng Thí nghiệm 202", "R202", 20));
            rooms.add(createRoom(lab2, "Phòng Họp 203", "R203", 12));

            // Lab 3 - Cybersecurity Operations Lab
            rooms.add(createRoom(lab3, "Phòng Bảo mật 301", "R301", 25));
            rooms.add(createRoom(lab3, "Phòng Kiểm thử 302", "R302", 18));
            rooms.add(createRoom(lab3, "Phòng Phân tích 303", "R303", 10));

            // Lab 4 - Green Energy Research Unit
            rooms.add(createRoom(lab4, "Phòng Năng lượng 401", "R401", 35));
            rooms.add(createRoom(lab4, "Phòng Kỹ thuật 402", "R402", 20));
            rooms.add(createRoom(lab4, "Phòng Thảo luận 403", "R403", 10));

            // Lab 5 - Data Science and Analytics Hub
            rooms.add(createRoom(lab5, "Phòng Data 501", "R501", 45));
            rooms.add(createRoom(lab5, "Phòng Máy chủ 502", "R502", 20));
            rooms.add(createRoom(lab5, "Phòng Họp 503", "R503", 12));

            // Lab 6 - Human-Computer Interaction Studio
            rooms.add(createRoom(lab6, "Phòng UX 601", "R601", 20));
            rooms.add(createRoom(lab6, "Phòng AR/VR 602", "R602", 10));
            rooms.add(createRoom(lab6, "Phòng Test UI 603", "R603", 8));

            // Lab 7 - Quantum Computing Lab
            rooms.add(createRoom(lab7, "Phòng Quantum 701", "R701", 15));
            rooms.add(createRoom(lab7, "Phòng Mạch Qubit 702", "R702", 10));
            rooms.add(createRoom(lab7, "Phòng Nghiên cứu 703", "R703", 6));

            // Lab 8 - Robotics and Automation Lab
            rooms.add(createRoom(lab8, "Phòng Robotics 801", "R801", 30));
            rooms.add(createRoom(lab8, "Phòng Cánh tay Robot 802", "R802", 20));
            rooms.add(createRoom(lab8, "Phòng Điều khiển 803", "R803", 12));

            // Lab 9 - Environmental Monitoring Lab
            rooms.add(createRoom(lab9, "Phòng Môi trường 901", "R901", 20));
            rooms.add(createRoom(lab9, "Phòng Cảm biến 902", "R902", 12));
            rooms.add(createRoom(lab9, "Phòng Phân tích 903", "R903", 10));

            // Lab 10 - Software Engineering Lab
            rooms.add(createRoom(lab10, "Phòng Software 1001", "R1001", 50));
            rooms.add(createRoom(lab10, "Phòng DevOps 1002", "R1002", 20));
            rooms.add(createRoom(lab10, "Phòng QA 1003", "R1003", 15));

            roomRepository.saveAll(rooms);
            System.out.println("✅ 30 Rooms seeded successfully!");
        } else {
            System.out.println("ℹ️ Rooms already exist, skipping seed.");
        }
    }

    private Lab createLab(String name, String code, String location, String description) {
        Lab lab = new Lab();
        lab.setLabName(name);
        lab.setLabCode(code);
        lab.setLocation(location);
        lab.setDescription(description);
        lab.setStatus(LabStatus.ACTIVE);
        lab.setRooms(null);
        lab.setRoomSlots(null);
        lab.setMembers(null);
        lab.setStaffs(null);
        return lab;
    }

    private Room createRoom(Lab lab, String roomName, String roomCode, int capacity) {
        Room room = new Room();
        room.setLab(lab);
        room.setRoomName(roomName);
        room.setRoomCode(roomCode);
        room.setCapacity(capacity);
        room.setStatus("ACTIVE");
        return room;
    }
}

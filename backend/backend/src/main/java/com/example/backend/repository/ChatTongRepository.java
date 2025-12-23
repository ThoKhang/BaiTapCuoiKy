package com.example.backend.repository;

import com.example.backend.entity.ChatTong;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChatTongRepository extends JpaRepository<ChatTong, Long> {

    // Lấy tin mới nhất (desc) với limit bằng Pageable
    @Query("SELECT c FROM ChatTong c ORDER BY c.id DESC")
    List<ChatTong> findLatest(Pageable pageable);

    // Load dạng chat history theo id (phân trang kiểu infinite scroll)
    @Query("SELECT c FROM ChatTong c WHERE c.id < :beforeId ORDER BY c.id DESC")
    List<ChatTong> findLatestBeforeId(Long beforeId, Pageable pageable);
}

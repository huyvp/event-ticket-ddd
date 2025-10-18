package com.source.infrastructure.persistence.mapper;

import com.source.domain.model.entity.TicketDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TicketDetailMapper extends JpaRepository<TicketDetail, Long> {
    @Override
    Optional<TicketDetail> findById(Long id);
}

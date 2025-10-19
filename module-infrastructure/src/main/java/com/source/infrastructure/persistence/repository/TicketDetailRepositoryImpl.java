package com.source.infrastructure.persistence.repository;

import com.source.domain.model.entity.TicketDetail;
import com.source.domain.repository.TicketDetailRepository;
import com.source.infrastructure.persistence.mapper.TicketDetailJPAlMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class TicketDetailRepositoryImpl implements TicketDetailRepository {

    private final TicketDetailJPAlMapper ticketDetailJPAlMapper;

    @Override
    public Optional<TicketDetail> findById(Long id) {
        return ticketDetailJPAlMapper.findById(id);
    }
}

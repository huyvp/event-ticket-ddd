package com.source.domain.service.impl;

import com.source.domain.model.entity.TicketDetail;
import com.source.domain.repository.TicketDetailRepository;
import com.source.domain.service.TicketDetailDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class TicketDetailDomainServiceImpl implements TicketDetailDomainService {

    private final TicketDetailRepository ticketDetailRepository;

    @Override
    public TicketDetail getTicketDetailById(Long id) {
        return ticketDetailRepository.findById(id).orElse(null);
    }
}

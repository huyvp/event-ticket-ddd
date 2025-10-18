package com.source.application.service.ticket.impl;

import com.source.application.model.TicketDetailDTO;
import com.source.application.service.ticket.TicketDetailAppService;
import com.source.domain.service.TicketDetailDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class TicketDetailAppServiceImpl implements TicketDetailAppService {

    private final TicketDetailDomainService ticketDetailDomainService;

    @Override
    public TicketDetailDTO getTicketDetailById(Long ticketId, Long version) {
        return null;
    }

    @Override
    public boolean orderTicketByUser(Long ticketId) {
        return false;
    }
}

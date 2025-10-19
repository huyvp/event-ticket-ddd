package com.source.application.service.ticket.impl;

import com.source.application.mapper.TicketDetailMapper;
import com.source.application.model.TicketDetailDTO;
import com.source.application.model.cache.TicketDetailCache;
import com.source.application.service.ticket.TicketDetailAppService;
import com.source.application.service.ticket.cache.TicketDetailCacheServiceRefactor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class TicketDetailAppServiceImpl implements TicketDetailAppService {

    private final TicketDetailCacheServiceRefactor ticketDetailCacheServiceRefactor;

    @Override
    public TicketDetailDTO getTicketDetailById(Long ticketId, Long version) {
        log.info("Implement Application: {}, {}", ticketId, version);
        TicketDetailCache ticketDetailCache = ticketDetailCacheServiceRefactor.getTicketDetailCache(ticketId, version);

        TicketDetailDTO ticketDetailDTO = TicketDetailMapper.toTicketDetailDTO(ticketDetailCache.getTicketDetail());
        ticketDetailDTO.setVersion(version);
        return ticketDetailDTO;
    }

    @Override
    public boolean orderTicketByUser(Long ticketId) {
        return ticketDetailCacheServiceRefactor.orderTicketByUser(ticketId);
    }
}

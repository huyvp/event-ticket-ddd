package com.source.application.service.ticket;

import com.source.application.model.TicketDetailDTO;

public interface TicketDetailAppService {

    TicketDetailDTO getTicketDetailById(Long ticketId, Long version);

    boolean orderTicketByUser(Long ticketId);
}

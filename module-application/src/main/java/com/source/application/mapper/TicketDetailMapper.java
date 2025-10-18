package com.source.application.mapper;

import com.source.application.model.TicketDetailDTO;
import com.source.domain.model.entity.TicketDetail;
import org.springframework.beans.BeanUtils;

public class TicketDetailMapper {
    public static TicketDetailDTO toTicketDetailDTO(TicketDetail ticketDetail) {
        if (ticketDetail == null) return null;
        TicketDetailDTO ticketDetailDTO = new TicketDetailDTO();
        BeanUtils.copyProperties(ticketDetail, ticketDetailDTO);
        return ticketDetailDTO;
    }
}

package com.source.controller.resource;

import com.source.application.model.TicketDetailDTO;
import com.source.application.service.ticket.TicketDetailAppService;
import com.source.controller.model.CommonResponse;
import com.source.controller.utils.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/ticket")
@RequiredArgsConstructor
public class TicketController {

    private final TicketDetailAppService ticketDetailAppService;

    @GetMapping(path = "/ping/java")
    public ResponseEntity<Object> ping() throws InterruptedException {
        Thread.sleep(1000);
        return ResponseEntity.status(HttpStatus.OK)
                .body("OK");
    }

    @GetMapping(path = "/{ticketId}/detail/{detailId}")
    public CommonResponse<TicketDetailDTO> getTicketDetail(
            @PathVariable(name = "ticketId") Long ticketId,
            @PathVariable(name = "detailId") Long detailId,
            @RequestParam(name = "version", required = false) Long version
    ) {
        return ResponseUtil.data(ticketDetailAppService.getTicketDetailById(detailId, version));
    }

    @GetMapping(path = "/{ticketId}/detail/{detailId}/order")
    public boolean orderTicketByUser(
            @PathVariable(name = "ticketId") Long ticketId,
            @PathVariable(name = "detailId") Long detailId
    ) {
        return ticketDetailAppService.orderTicketByUser(detailId);
    }
}

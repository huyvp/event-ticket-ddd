package com.source.controller.resource;

import com.source.application.service.event.EventService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/hello")
@RequiredArgsConstructor
public class TestController {

    private final EventService eventService;

    @GetMapping(path = "/hi")
    @RateLimiter(name = "backendA", fallbackMethod = "fallbackMethod")
    public String hello() {
        return "hello";
    }

    private String fallbackMethod() {
        return "fallback";
    }
}

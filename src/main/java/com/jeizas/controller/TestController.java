package com.jeizas.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * @Author jeizas
 * @Date 2018 8/19/18 4:51 PM
 */
@RestController
public class TestController {

    @GetMapping(value = "/get")
    public Mono<String> get() {
        return Mono.just("hollo world")
                .flatMap(i -> Mono.just(sleep(1000L)));
//        throw new ServerException(new ErrorCode(100, "业务异常"));
//        throw new NullPointerException();
    }

    @PostMapping("/post")
    public Mono<String> post(@RequestBody String value) {
        return Mono.just(value);
    }

    private static String sleep(long mill) {
        try {
            Thread.sleep(mill);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "hollo world";
    }

    @GetMapping(value = "/favicon.ico")
    public Mono<String> favicon() {
        return Mono.empty();
    }
}

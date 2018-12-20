package com.jeizas.filter;

import com.jeizas.decorator.PayloadServerWebExchangeDecorator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * @author jeizas
 * @date 2018-12-19 19:14
 */
@Component
@Order(2)
@Slf4j
public class RequestFilter implements WebFilter{
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        long startTime = System.currentTimeMillis();
        return chain.filter(new PayloadServerWebExchangeDecorator(exchange))
                .doOnSuccess((done) -> success(startTime));
    }

    private void success(long startTime) {
        log.info("接口耗时：{}ms", (System.currentTimeMillis() - startTime) / 1000.0);
    }

//    @Bean
////    @Order(Ordered.HIGHEST_PRECEDENCE) //过滤器顺序
//    public WebFilter webFilter() {
//        return (exchange, chain) -> chain.filter(new PayloadServerWebExchangeDecorator(exchange));
//    }

}
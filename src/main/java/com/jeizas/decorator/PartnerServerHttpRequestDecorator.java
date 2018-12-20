package com.jeizas.decorator;

import com.jeizas.utils.LogUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;

import java.util.Optional;
import java.util.stream.Collectors;

import static reactor.core.scheduler.Schedulers.single;

/**
 * @author jeizas
 * @date 2018-12-19 19:11
 */
@Slf4j
public class PartnerServerHttpRequestDecorator extends ServerHttpRequestDecorator {

    private Flux<DataBuffer> body;

    PartnerServerHttpRequestDecorator(ServerHttpRequest delegate) {
        super(delegate);
        final String path = delegate.getURI().getPath();
        final String query = delegate.getURI().getQuery();
        final String method = Optional.ofNullable(delegate.getMethod()).orElse(HttpMethod.GET).name();
        final String headers = delegate.getHeaders().entrySet()
                .stream()
                .map(entry -> "            " + entry.getKey() + ": [" + String.join(";", entry.getValue()) + "]")
                .collect(Collectors.joining("\n"));
        final MediaType contentType = delegate.getHeaders().getContentType();
        if (log.isDebugEnabled()) {
            log.info("\n" +
                    "HttpMethod : {}\n" +
                    "Uri        : {}\n" +
                    "Headers    : \n" +
                    "{}", method, path + (StringUtils.isEmpty(query) ? "" : "?" + query), headers);
        }
        Flux<DataBuffer> flux = super.getBody();
        if (LogUtils.legalLogMediaTypes.contains(contentType)) {
            body = flux.publishOn(single()).map(dataBuffer -> LogUtils.loggingRequest(log, dataBuffer));
        } else {
            body = flux;
        }
    }

    @Override
    public Flux<DataBuffer> getBody() {
        return body;
    }

}

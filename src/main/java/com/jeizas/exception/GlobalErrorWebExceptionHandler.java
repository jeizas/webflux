//package com.jeizas.exception;
//
//import org.springframework.boot.autoconfigure.web.ResourceProperties;
//import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
//import org.springframework.boot.web.reactive.error.ErrorAttributes;
//import org.springframework.context.ApplicationContext;
//import org.springframework.core.annotation.Order;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.stereotype.Component;
//import org.springframework.web.reactive.function.BodyInserters;
//import org.springframework.web.reactive.function.server.RequestPredicates;
//import org.springframework.web.reactive.function.server.RouterFunction;
//import org.springframework.web.reactive.function.server.RouterFunctions;
//import org.springframework.web.reactive.function.server.ServerRequest;
//import org.springframework.web.reactive.function.server.ServerResponse;
//import reactor.core.publisher.Mono;
//
//import java.util.Map;
//
//import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;
//
///**
// * @author jeizas
// * @date 2018-12-19 15:10
// */
//@Component
//@Order(-2)
//public class GlobalErrorWebExceptionHandler extends AbstractErrorWebExceptionHandler {
//
//    public GlobalErrorWebExceptionHandler(ErrorAttributes errorAttributes,
//                                          ResourceProperties resourceProperties,
//                                          ApplicationContext applicationContext) {
//        super(errorAttributes, resourceProperties, applicationContext);
//    }
//
//    @Override
//    protected RouterFunction<ServerResponse> getRoutingFunction(final ErrorAttributes errorAttributes) {
//        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
//    }
//
//    private Mono<ServerResponse> renderErrorResponse(final ServerRequest request) {
//
//        final Map<String, Object> errorPropertiesMap = getErrorAttributes(request, true);
//
//        return ServerResponse.status(HttpStatus.OK)
//                .contentType(MediaType.APPLICATION_JSON_UTF8)
//                .body(BodyInserters.fromObject(errorPropertiesMap));
//    }
//}
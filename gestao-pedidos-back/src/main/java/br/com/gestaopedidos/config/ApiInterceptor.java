package br.com.gestaopedidos.config;

import br.com.gestaopedidos.core.util.JsonUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Log4j2
@Aspect
@Component
@RequiredArgsConstructor
public class ApiInterceptor {

    private final JsonUtil jsonUtil;

    @Pointcut("@within(org.springframework.web.bind.annotation.RestController)")
    public void metodosRestController() {
    }

    @Around("metodosRestController()")
    public Object logApiExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();

        Map<String, String> headers = new HashMap<>();
        Collections.list(request.getHeaderNames())
                .forEach(headerName -> headers.put(headerName, request.getHeader(headerName)));

        log.info("==================== INÍCIO DA REQUISIÇÃO ====================");
        log.info("Endpoint: {} {}", request.getMethod(), request.getRequestURI());
        log.info("Headers: {}", jsonUtil.objetosArrayParaJsonString(headers));
        log.info("Método: {}.{}", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
        log.info("Argumentos: {}", jsonUtil.objetosArrayParaJsonString(joinPoint.getArgs()));

        Object response;
        try {
            response = joinPoint.proceed();
        } catch (Throwable throwable) {
            log.info("==================== FIM DA REQUISIÇÃO COM ERRO ====================");
            throw throwable;
        }

        long endTime = System.currentTimeMillis();
        long totalExecutionTime = endTime - startTime;

        log.info("Resposta: {}", jsonUtil.objetoParaJsonString(response));
        log.info("Tempo de execução: {} ms", totalExecutionTime);
        log.info("==================== FIM DA REQUISIÇÃO ====================");

        return response;
    }

}

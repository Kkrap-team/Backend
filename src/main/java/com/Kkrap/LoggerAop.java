package com.Kkrap;

import org.aspectj.lang.ProceedingJoinPoint; // AOP에서 사용하는 ProceedingJoinPoint 클래스 임포트
import org.aspectj.lang.annotation.Around; // AOP의 Around 어노테이션 임포트
import org.aspectj.lang.annotation.Aspect; // AOP의 Aspect 어노테이션 임포트
import org.slf4j.Logger; // SLF4J의 Logger 인터페이스 임포트
import org.slf4j.LoggerFactory; // SLF4J의 LoggerFactory 클래스 임포트
import org.springframework.stereotype.Component; // Spring의 Component 어노테이션 임포트

import java.util.Arrays; // Arrays 유틸리티 클래스 임포트

@Component // Spring의 빈으로 등록
@Aspect // AOP의 어드바이스로 작동함을 명시
public class LoggerAop {

    // SLF4J Logger 객체 생성
    private static final Logger log = LoggerFactory.getLogger(LoggerAop.class);

    // @Around 어노테이션을 사용하여 특정 메서드 실행 전후에 로깅 수행
    @Around("execution(* com.spring.jpa.controller..*Controller.*(..))"
            +" || execution(* com.spring.jpa.service..*Service*.*(..))"
            +" || execution(* com.spring.jpa.repository..*Repository.*(..))")
    public Object logPrint(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        // 현재 실행 중인 메서드가 속한 클래스의 이름을 가져옴
        String type = proceedingJoinPoint.getSignature().getDeclaringTypeName();

        // 현재 실행 중인 메서드의 이름을 가져와서 로그 출력
        log.info("[[START]] " + type + "." + proceedingJoinPoint.getSignature().getName() + "() <=================");

        // 메서드에 전달된 파라미터들을 로그로 출력
        log.info("Argument/Parameter : " + Arrays.toString(proceedingJoinPoint.getArgs()));

        // 메서드 실행 후 종료 로그 출력
        log.info("================ [[END : " + proceedingJoinPoint.getSignature().getName() + "()]] ==================");

        // 실제 메서드 실행
        return proceedingJoinPoint.proceed();
    }

}

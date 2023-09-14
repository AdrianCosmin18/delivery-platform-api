package aspects;

import com.example.deliveryapp.system.annotations.MockSecurityContext;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@Aspect
@Component
public class MockSecurityContextAspect {





    public void setupMockSecurityContext(){

        String username ="test@gmail.cpm";

        Authentication auth = mock(Authentication.class);
        when(auth.getPrincipal()).thenReturn(username);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);
    }


    @Around("@annotation(MockSecurityContext)")
    public Object auth(ProceedingJoinPoint joinPoint) throws Throwable {
        setupMockSecurityContext();
        Object result = joinPoint.proceed();
        return result;
    }
}

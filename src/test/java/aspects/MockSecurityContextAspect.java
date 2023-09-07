package aspects;

import com.example.deliveryapp.system.annotations.MockSecurityContext;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@Aspect
@Component
public class MockSecurityContextAspect {

    @Before("@annotation(mockSecurityContext)")
    public void setupMockSecurityContext(MockSecurityContext mockSecurityContext){

        String username = mockSecurityContext.username();

        Authentication auth = mock(Authentication.class);
        when(auth.getPrincipal()).thenReturn(username);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);
    }
}

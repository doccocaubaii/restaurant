package vn.softdreams.easypos.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import vn.softdreams.easypos.dto.authorities.JwtDTO;
import vn.softdreams.easypos.repository.AuthorityRepository;
import vn.softdreams.easypos.service.UserService;
import vn.softdreams.easypos.web.rest.errors.AuthenticateException;
import vn.softdreams.easypos.web.rest.errors.ExceptionConstants;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Aspect
@Component
public class CheckAuthorizeAspect {

    private final AuthorityRepository authorityRepository;
    private final UserService userService;

    public CheckAuthorizeAspect(AuthorityRepository authorityRepository, UserService userService) {
        this.authorityRepository = authorityRepository;
        this.userService = userService;
    }

    @Pointcut("@annotation(vn.softdreams.easypos.aop.CheckAuthorize)") // full path to CustomAnnotation class
    public void checkAuthorizeByJwt() {}

    @Around("checkAuthorizeByJwt()")
    public Object aroundCheckAuthorizeByJwt(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) pjp.getStaticPart().getSignature();
        Method method = methodSignature.getMethod();
        CheckAuthorize myAnnotation = method.getAnnotation(CheckAuthorize.class);
        List<String> value = Arrays.asList(myAnnotation.value().split(","));
        JwtDTO jwtDTO = userService.getInfoJwt();

        Set<String> authority = authorityRepository.findAllAuthorityByUserIDAndCompanyId(jwtDTO.getId(), jwtDTO.getCompanyId(), value);
        if (authority.isEmpty()) {
            throw new AuthenticateException(
                ExceptionConstants.UNAUTHORIZED_VI,
                ExceptionConstants.UNAUTHORIZED,
                ExceptionConstants.UNAUTHORIZED
            );
        }
        return pjp.proceed();
    }
}

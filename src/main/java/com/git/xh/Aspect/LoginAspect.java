package com.git.xh.Aspect;

import com.git.xh.entity.MusicInfo;
import com.git.xh.entity.UserLiked;
import com.git.xh.service.MusicinfoService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.git.xh.Aspect.Annotation.loginCheck;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author heqi
 */
@Aspect
@Component
public class LoginAspect {

    @Autowired
    MusicinfoService musicinfoService;

    @Around("@annotation(loginCheck)")
    public List<MusicInfo> loginCheck(ProceedingJoinPoint pjp, loginCheck loginCheck) throws Throwable {
        List<MusicInfo> list = new ArrayList<>();
        try{
            RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
            ServletRequestAttributes attributes = (ServletRequestAttributes)requestAttributes;
            HttpServletRequest request = attributes.getRequest();
            String userId = request.getParameter("userId");
            list = (List<MusicInfo>)pjp.proceed();

            if (!StringUtils.isEmpty(userId)){
                list.forEach(info -> {
                    UserLiked liked = musicinfoService.getLiked(userId, info.getId());
                    if (liked!=null){
                        info.setLiked(true);
                    }
                });
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }
}

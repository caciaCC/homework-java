package com.db.homework.aspect;

import com.db.homework.entity.ExceptionLog;
import com.db.homework.entity.Log;
import com.db.homework.log.OperLog;
import com.db.homework.service.ExceptionLogService;
import com.db.homework.service.LogService;
import com.db.homework.utils.StringAndDate;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
public class WebLogAspect {
    @Autowired
    private LogService logService;
    @Autowired
    private ExceptionLogService exceptionLogService;

    private final Logger logger = LoggerFactory.getLogger(WebLogAspect.class);

    @Pointcut("execution(public * com.db.homework.controller..*.*(..))")//切入点描述 这个是controller包的切入点
    public void controllerLog(){}//签名，可以理解成这个切入点的一个名称

//    @Before("controllerLog()") //在切入点的方法run之前要干的
//    public void logBeforeController(JoinPoint joinPoint) {
//
//
//        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();//这个RequestContextHolder是Springmvc提供来获得请求的东西
//        HttpServletRequest request = ((ServletRequestAttributes)requestAttributes).getRequest();
//
//        // 记录下请求内容
//        logger.info("################URL : " + request.getRequestURL().toString());
//        logger.info("################HTTP_METHOD : " + request.getMethod());
//        logger.info("################IP : " + request.getRemoteAddr());
//        logger.info("################THE ARGS OF THE CONTROLLER : " + Arrays.toString(joinPoint.getArgs()));
//
//        //下面这个getSignature().getDeclaringTypeName()是获取包+类名的   然后后面的joinPoint.getSignature.getName()获取了方法名
//        logger.info("################CLASS_METHOD : " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
//        //logger.info("################TARGET: " + joinPoint.getTarget());//返回的是需要加强的目标类的对象
//        //logger.info("################THIS: " + joinPoint.getThis());//返回的是经过加强后的代理类的对象
//
//    }
    /**
     * 正常返回通知，拦截用户操作日志，连接点正常执行完成后执行， 如果连接点抛出异常，则不会执行
     *
     * @param joinPoint 切入点
     * @param keys      返回结果
     */
    @AfterReturning(value = "controllerLog()", returning = "keys")
    public void saveOperLog(JoinPoint joinPoint, Object keys) {
        // 获取RequestAttributes
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        // 从获取RequestAttributes中获取HttpServletRequest的信息
        HttpServletRequest request = (HttpServletRequest) requestAttributes
                .resolveReference(RequestAttributes.REFERENCE_REQUEST);

        Log operlog = new Log();
        try {
//            operlog.setOperId(UuidUtil.get32UUID()); // 主键ID

            // 从切面织入点处通过反射机制获取织入点处的方法
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            // 获取切入点所在的方法
            Method method = signature.getMethod();
            // 获取操作
            OperLog opLog = method.getAnnotation(OperLog.class);
            if (opLog != null) {
                String operModul = opLog.operModul();
                String operType = opLog.operType();
                String operDesc = opLog.operDesc();
                operlog.setModul(operModul);// 操作模块
                operlog.setType(operType); // 操作类型
                operlog.setDescp(operDesc); // 操作描述
            }
            // 获取请求的类名
            String className = joinPoint.getTarget().getClass().getName();
            // 获取请求的方法名
            String methodName = method.getName();
            methodName = className + "." + methodName;

            operlog.setMethod(methodName); // 请求方法

//            // 请求的参数
//            Map<String, String> rtnMap = converMap(request.getParameterMap());
//            // 将参数所在的数组转换成json
//            String params = JSON.toJSONString(rtnMap);
//
//            operlog.setOperRequParam(params); // 请求参数
//            operlog.setOperRespParam(JSON.toJSONString(keys)); // 返回结果
//            operlog.setOperUserId(UserShiroUtil.getCurrentUserLoginName()); // 请求用户ID
//            operlog.setOperUserName(UserShiroUtil.getCurrentUserName()); // 请求用户名称
            operlog.setIp(request.getRemoteAddr()); // 请求IP
            operlog.setUri(request.getRequestURI()); // 请求URI
            operlog.setTime(StringAndDate.getDetailedString(new Date())); // 创建时间
//            operlog.setOperVer(operVer); // 操作版本
            logService.insert(operlog);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 异常返回通知，用于拦截异常日志信息 连接点抛出异常后执行
     *
     * @param joinPoint 切入点
     * @param e         异常信息
     */
    @AfterThrowing(pointcut = "controllerLog()", throwing = "e")
    public void saveExceptionLog(JoinPoint joinPoint, Throwable e) {
        System.out.println(1111);
        // 获取RequestAttributes
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        // 从获取RequestAttributes中获取HttpServletRequest的信息
        HttpServletRequest request = (HttpServletRequest) requestAttributes
                .resolveReference(RequestAttributes.REFERENCE_REQUEST);

        ExceptionLog excepLog = new ExceptionLog();
        try {
            // 从切面织入点处通过反射机制获取织入点处的方法
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            // 获取切入点所在的方法
            Method method = signature.getMethod();
//            excepLog.setExcId(UuidUtil.get32UUID());
            // 获取请求的类名
            String className = joinPoint.getTarget().getClass().getName();
            // 获取请求的方法名
            String methodName = method.getName();
            System.out.println(methodName);
            methodName = className + "." + methodName;
//            // 请求的参数
//            Map<String, String> rtnMap = converMap(request.getParameterMap());
//            // 将参数所在的数组转换成json
//            String params = JSON.toJSONString(rtnMap);
//            excepLog.setExcRequParam(params); // 请求参数
            excepLog.setMethod(methodName); // 请求方法名
            excepLog.setName(e.getClass().getName()); // 异常名称
            excepLog.setMessage(stackTraceToString(e.getClass().getName(), e.getMessage(), e.getStackTrace())); // 异常信息
//            excepLog.setOperUserId(UserShiroUtil.getCurrentUserLoginName()); // 操作员ID
//            excepLog.setOperUserName(UserShiroUtil.getCurrentUserName()); // 操作员名称
             excepLog.setUri(request.getRequestURI()); // 操作URI
            excepLog.setIp(request.getRemoteAddr()); // 操作员IP
//            excepLog.setOperVer(operVer); // 操作版本号
            excepLog.setTime(StringAndDate.getDetailedString(new Date())); // 发生异常时间

            exceptionLogService.insert(excepLog);

        } catch (Exception e2) {
            e2.printStackTrace();
        }

    }


    /**
     * 转换request 请求参数
     *
     * @param paramMap request获取的参数数组
     */
    public Map<String, String> converMap(Map<String, String[]> paramMap) {
        Map<String, String> rtnMap = new HashMap<String, String>();
        for (String key : paramMap.keySet()) {
            rtnMap.put(key, paramMap.get(key)[0]);
        }
        return rtnMap;
    }

    /**
     * 转换异常信息为字符串
     *
     * @param exceptionName    异常名称
     * @param exceptionMessage 异常信息
     * @param elements         堆栈信息
     */
    public String stackTraceToString(String exceptionName, String exceptionMessage, StackTraceElement[] elements) {
        StringBuffer strbuff = new StringBuffer();
        for (StackTraceElement stet : elements) {
            strbuff.append(stet + "\n");
        }
        String message = exceptionName + ":" + exceptionMessage + "\n\t" + strbuff.toString();
        return message;
    }
}

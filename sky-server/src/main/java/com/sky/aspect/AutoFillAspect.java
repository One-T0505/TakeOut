package com.sky.aspect;

import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * ymy
 * 2023/7/29 - 21 : 54
 *
 * 自定义切面，实现公共属性字段的自动填充处理逻辑
 *
 **/

@Aspect
@Component
@Slf4j
public class AutoFillAspect {

    /**
     * 切入点：实际上就是指定何时做切面
     * 下面这种写法比较少见：第一部分就是常规的选中 com.sky.mapper 下所有类的所有方法
     * && 表示逻辑与  因为不是 com.sky.mapper 下所有类的所有方法都需要做切面
     * @annotation(com.sky.annotation.AutoFill) 表示加了 AutoFill 注解的方法
     * 所以，该切点表达式总体上就是选择 com.sky.mapper 下所有标记了 AutoFill 注解的方法
     **/
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
    public void autoFillPointCut() {}


    @Before("autoFillPointCut()")
    public void autoFill(JoinPoint joinPoint) {
        log.info("开始进行公共字段自动填充...");

        // 获取到当前被拦截的方法上的数据库操作类型 是 insert 还是 update
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        AutoFill autoFill = signature.getMethod().getAnnotation(AutoFill.class);
        OperationType operationType = autoFill.value();

        // 获取到当前被拦截的方法的参数，即实体对象
        Object[] args = joinPoint.getArgs();
        if (args == null || args.length == 0) {
            return;
        }
        Object entity = args[0]; // 默认实体对象在形参列表的第一个


        // 根据当前不同的操作类型，为对应的属性通过反射来赋值
        // 不管是 insert 还是 update  updateTime 和 updateUser 都必然要修改
        // update 时  createTime 和 createUser 不用修改
        if (operationType == OperationType.INSERT) {
            try {
                Method setCreateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                Method setCreateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);

                setCreateTime.invoke(entity, LocalDateTime.now());
                setCreateUser.invoke(entity, BaseContext.getCurrentId());

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        // 都必须要执行 update
        try {
            Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
            Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

            setUpdateTime.invoke(entity, LocalDateTime.now());
            setUpdateUser.invoke(entity, BaseContext.getCurrentId());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}

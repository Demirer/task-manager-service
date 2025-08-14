package com.taskmanager.validation;

import com.taskmanager.model.request.TaskListRequest;
import com.taskmanager.model.request.TaskRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class RequestValidatorAspect {

    private static final int MAX_LENGTH = 128;

    @Before("@annotation(com.taskmanager.validation.ValidateRequest)")
    public void validateRequest(JoinPoint joinPoint) {
        for (Object arg : joinPoint.getArgs()) {

            if (arg instanceof TaskRequest task) {
                if (task.getName() == null || task.getName().isBlank()) {
                    throw new IllegalArgumentException("Task name is required");
                }
                if (task.getName().length() > MAX_LENGTH) {
                    throw new IllegalArgumentException("Task name must be at most " + MAX_LENGTH + " characters");
                }
                if (task.getDescription() != null && task.getDescription().length() > MAX_LENGTH) {
                    throw new IllegalArgumentException("Task description must be at most " + MAX_LENGTH + " characters");
                }
            }

            if (arg instanceof TaskListRequest list) {
                if (list.getName() == null || list.getName().isBlank()) {
                    throw new IllegalArgumentException("List name is required");
                }
                if (list.getName().length() > MAX_LENGTH) {
                    throw new IllegalArgumentException("List name must be at most " + MAX_LENGTH + " characters");
                }
            }
        }
    }
}



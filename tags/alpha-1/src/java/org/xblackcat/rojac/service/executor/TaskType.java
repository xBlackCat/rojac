package org.xblackcat.rojac.service.executor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The annotation is used by {@link org.xblackcat.rojac.service.executor.IExecutor} implementation to determine which
 * thread type should be used for a task.
 *
 * @author xBlackCat
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface TaskType {
    TaskTypeEnum value();
}

package com.wilburomae.pezeshalms.auditlogs;

import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class DataSourceWrapperPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessAfterInitialization(@NotNull Object bean, @NotNull String beanName) {
        if (bean instanceof DataSource && !(bean instanceof AppUserDatasource)) {
            return new AppUserDatasource((DataSource) bean);
        }
        return bean;
    }
}
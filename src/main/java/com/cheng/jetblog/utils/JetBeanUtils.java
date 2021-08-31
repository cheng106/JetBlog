package com.cheng.jetblog.utils;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author cheng
 * @since 2021/8/31 22:48
 **/
public class JetBeanUtils {

    /**
     * 取得為null的屬性值
     *
     * @param source Bean
     * @return java.lang.String[]
     **/
    public static String[] getNullPropertyNames(Object source) {
        BeanWrapper wrapper = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = wrapper.getPropertyDescriptors();
        List<String> nullPropertyNames = new ArrayList<>();
        for (PropertyDescriptor pd : pds) {
            String property = pd.getName();
            if (wrapper.getPropertyValue(property) == null) {
                nullPropertyNames.add(property);
            }
        }

        return String.join(",", nullPropertyNames).split(",");
    }
}

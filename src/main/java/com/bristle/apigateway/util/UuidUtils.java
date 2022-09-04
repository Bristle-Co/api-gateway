package com.bristle.apigateway.util;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.UUID;
import java.util.regex.Pattern;

@Component
public class UuidUtils {

    public static final Pattern UUID_REGEX_TEMPLATE = Pattern.compile(
            "([A-F0-9]{8}(-[A-F0-9]{4}){3}-[A-F0-9]{12})",
            Pattern.CASE_INSENSITIVE);

    public String randomId() {
        return UUID.randomUUID().toString();
    }

    public boolean isValidUuid(String uuid) {
        return StringUtils.hasText(uuid) && UUID_REGEX_TEMPLATE.matcher(uuid).matches();
    }

}


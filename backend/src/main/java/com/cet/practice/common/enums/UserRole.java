package com.cet.practice.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserRole {

    STUDENT("STUDENT"),
    ADMIN("ADMIN");

    private final String value;
}

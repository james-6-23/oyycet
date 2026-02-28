package com.cet.practice.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaperStatus {

    DRAFT("DRAFT"),
    PUBLISHED("PUBLISHED");

    private final String value;
}

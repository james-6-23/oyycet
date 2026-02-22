package com.cet.practice.dto;

import lombok.Data;

import java.util.List;

@Data
public class PracticeRecordDetailDTO extends PracticeRecordDTO {
    private Long wrongCount;
    private List<PracticeRecordQuestionDTO> questions;
}


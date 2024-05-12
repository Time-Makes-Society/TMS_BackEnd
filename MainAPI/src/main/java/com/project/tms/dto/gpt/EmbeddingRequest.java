package com.project.tms.dto.gpt;


import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;


@Getter
@Setter
@NoArgsConstructor
public class EmbeddingRequest {

    private String input;

    private String model;
}

package com.project.tms.dto;


import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class SilmilarityDto {

    private UUID uuid;
    private double similarity;
    private String title;
}

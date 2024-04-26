package com.project.tms.domain.tag;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("ECONOMY")
public class Economy extends Tag {
}

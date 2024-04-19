package com.project.tms.domain.tag;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("POLITICS")
public class Politics extends Tag {
}

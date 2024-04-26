package com.project.tms.domain.tag;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("TECHNOLOGY")
public class Technology extends Tag {
}

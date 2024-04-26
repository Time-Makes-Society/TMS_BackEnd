package com.project.tms.domain.tag;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("SPORTS")
public class Sports extends Tag {
}

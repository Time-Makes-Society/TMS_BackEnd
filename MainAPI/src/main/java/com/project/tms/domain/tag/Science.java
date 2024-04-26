package com.project.tms.domain.tag;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("SCIENCE")
public class Science extends Tag {
}

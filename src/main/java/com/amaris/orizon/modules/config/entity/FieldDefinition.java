package com.amaris.orizon.modules.config.entity;

import com.amaris.orizon.shared.entity.BaseEntity;
import com.amaris.orizon.shared.enumeration.FieldType;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "field_definitions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FieldDefinition extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "module_code", nullable = false)
    private String moduleCode; // ex: "CRA"

    @Column(name = "team_code", nullable = false)
    private String teamCode;   // ex: "PDA_CAMUNDA"

    @Column(name = "field_key", nullable = false)
    private String fieldKey;   // ex: "ticket_jira_id"

    @Column(name = "field_label", nullable = false)
    private String fieldLabel; // ex: "ID Ticket Jira"

    @Enumerated(EnumType.STRING)
    @Column(name = "field_type", nullable = false)
    private FieldType type;

    @Column(name = "required", nullable = false)
    private boolean required;

    @Column(name = "validation_regex")
    private String validationRegex;

    // Dans FieldDefinition.java
    @OneToMany(mappedBy = "fieldDefinition", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<FieldOption> options;

}
package com.amaris.orizon.modules.config.entity;

import com.amaris.orizon.shared.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "field_options")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FieldOption extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "field_definition_id", nullable = false)
    private FieldDefinition fieldDefinition;

    @Column(nullable = false)
    private String optionValue; // La valeur technique (ex: "PHASE_DEV")

    @Column(nullable = false)
    private String optionLabel; // Ce que l'utilisateur voit (ex: "Phase de Développement")

    private Integer displayOrder; // Pour trier les options dans la liste
}
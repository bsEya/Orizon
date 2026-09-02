package com.amaris.orizon.modules.referentiel.entity;

import com.amaris.orizon.shared.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ref_perimetres")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Perimetre extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String code;

    @Column(nullable = false)
    private String label;

    @Builder.Default
    private boolean active = true;
}

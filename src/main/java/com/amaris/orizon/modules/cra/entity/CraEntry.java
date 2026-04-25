package com.amaris.orizon.modules.cra.entity;

import com.amaris.orizon.modules.user.entity.User;
import com.amaris.orizon.shared.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Map;

@Entity
@Table(name = "cra_entries")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CraEntry extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private Double duration;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activity_type_id", nullable = false)
    private ActivityType activityType;

    private String comment;

    /**
     * Stockage des champs dynamiques définis par l'équipe.
     * Table jointe : cra_entry_values (entry_id, field_key, field_value)
     */
    @ElementCollection
    @CollectionTable(name = "cra_entry_values", joinColumns = @JoinColumn(name = "entry_id"))
    @MapKeyColumn(name = "field_key")
    @Column(name = "field_value")
    private Map<String, String> dynamicFields;
}
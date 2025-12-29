package es.spb.englishmaster.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "english_levels")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnglishLevelEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "level_code", nullable = false, unique = true, length = 10)
    private String code;

    @Column(name = "level_name", nullable = false, length = 50)
    private String name;

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "order_index")
    private Integer orderIndex;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "english_level_categories",
            joinColumns = @JoinColumn(name = "english_level_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    @JsonIgnoreProperties("types")
    private Set<ExerciseCategoryEntity> availableCategories;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "english_level_exercise_types",
            joinColumns = @JoinColumn(name = "english_level_id"),
            inverseJoinColumns = @JoinColumn(name = "exercise_type_id")
    )
    @JsonIgnoreProperties("category")
    private Set<ExerciseTypeEntity> availableTypes;
}
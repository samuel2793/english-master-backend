package es.spb.englishmaster.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "exercise_categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseCategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // p.ej: "reading", "listening", "grammar-tests", "use-of-english"
    @Column(name = "code", nullable = false, unique = true, length = 40)
    private String code;

    // p.ej: "Reading", "Listening", "Grammar tests"
    @Column(name = "name", nullable = false, length = 60)
    private String name;

    @Column(name = "order_index")
    private Integer orderIndex;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    @JsonIgnoreProperties("category")
    private List<ExerciseTypeEntity> types;
}

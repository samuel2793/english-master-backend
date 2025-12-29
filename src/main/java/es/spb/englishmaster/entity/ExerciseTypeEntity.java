package es.spb.englishmaster.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "exercise_types")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseTypeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // p.ej: "long_text", "missing_sentences", "multiple_matching"
    @Column(name = "code", nullable = false, unique = true, length = 60)
    private String code;

    // p.ej: "Long Text", "Missing Sentences"
    @Column(name = "name", nullable = false, length = 80)
    private String name;

    @Column(name = "order_index")
    private Integer orderIndex;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    @JsonIgnoreProperties("types")
    private ExerciseCategoryEntity category;
}

package es.spb.englishmaster.config;

import es.spb.englishmaster.entity.EnglishLevelEntity;
import es.spb.englishmaster.entity.ExerciseCategoryEntity;
import es.spb.englishmaster.entity.ExerciseTypeEntity;
import es.spb.englishmaster.repository.EnglishLevelRepository;
import es.spb.englishmaster.repository.ExerciseCategoryRepository;
import es.spb.englishmaster.repository.ExerciseTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.*;

@Configuration
@RequiredArgsConstructor
public class DataSeedConfig {

    private final EnglishLevelRepository levelRepo;
    private final ExerciseCategoryRepository catRepo;
    private final ExerciseTypeRepository typeRepo;

    @Bean
    ApplicationRunner seedInitialData() {
        return args -> {

            // =========================
            // 1) NIVELES (solo estos 4)
            // =========================
            EnglishLevelEntity b1 = upsertLevel("B1", "B1", "Intermediate", 1);
            EnglishLevelEntity b2 = upsertLevel("B2", "B2", "Upper-intermediate", 2);
            EnglishLevelEntity c1 = upsertLevel("C1", "C1", "Advanced", 3);
            EnglishLevelEntity c2 = upsertLevel("C2", "C2", "Proficient", 4);

            // =========================
            // 2) CATEGORÍAS
            // =========================
            ExerciseCategoryEntity listening = upsertCategory("listening", "Listening", 1);
            ExerciseCategoryEntity reading = upsertCategory("reading", "Reading", 2);
            ExerciseCategoryEntity speaking = upsertCategory("speaking", "Speaking", 3);
            ExerciseCategoryEntity useOfEnglish = upsertCategory("use-of-english", "Use of English", 4);
            ExerciseCategoryEntity writing = upsertCategory("writing", "Writing", 5);
            ExerciseCategoryEntity grammarTests = upsertCategory("grammar-tests", "Grammar tests", 0);

            // Categorías comunes a TODOS
            Set<ExerciseCategoryEntity> commonCats = Set.of(listening, reading, speaking, useOfEnglish, writing);

            // B1: sin grammar-tests
            b1.setAvailableCategories(new HashSet<>(commonCats));

            // B2/C1/C2: con grammar-tests
            b2.setAvailableCategories(new HashSet<>(union(commonCats, Set.of(grammarTests))));
            c1.setAvailableCategories(new HashSet<>(union(commonCats, Set.of(grammarTests))));
            c2.setAvailableCategories(new HashSet<>(union(commonCats, Set.of(grammarTests))));

            levelRepo.save(b1);
            levelRepo.save(b2);
            levelRepo.save(c1);
            levelRepo.save(c2);

            // =========================
            // 3) TIPOS (subtipos) por categoría (globales)
            // =========================
            // --- LISTENING ---
            ExerciseTypeEntity lExtracts = upsertType("listening_extracts", "Extracts", 1, listening);
            ExerciseTypeEntity lGappedText = upsertType("listening_gapped_text", "Gapped Text", 2, listening);
            ExerciseTypeEntity lMatching = upsertType("listening_matching", "Matching", 3, listening);
            ExerciseTypeEntity lMultipleChoice = upsertType("listening_multiple_choice", "Multiple Choice", 4, listening);
            ExerciseTypeEntity lPictures = upsertType("listening_pictures", "Pictures", 5, listening);
            ExerciseTypeEntity lMultipleMatching = upsertType("listening_multiple_matching", "Multiple Matching", 6, listening);

            // --- READING ---
            ExerciseTypeEntity rLongText = upsertType("reading_long_text", "Long Text", 1, reading);
            ExerciseTypeEntity rMissingSentences = upsertType("reading_missing_sentences", "Missing Sentences", 2, reading);
            ExerciseTypeEntity rMatching = upsertType("reading_matching", "Matching", 3, reading);
            ExerciseTypeEntity rSigns = upsertType("reading_signs", "Signs", 4, reading);
            ExerciseTypeEntity rMissingParagraphs = upsertType("reading_missing_paragraphs", "Missing Paragraphs", 5, reading);
            ExerciseTypeEntity rMultipleMatching = upsertType("reading_multiple_matching", "Multiple Matching", 6, reading);
            ExerciseTypeEntity rCrossMatching = upsertType("reading_cross_matching", "Cross Matching", 7, reading);

            // --- SPEAKING ---
            ExerciseTypeEntity sPart1 = upsertType("speaking_part1_general_questions", "Part 1 - General Questions", 1, speaking);
            ExerciseTypeEntity sPart2Photo = upsertType("speaking_part2_photograph_description", "Part 2 - Photograph Description", 2, speaking);
            ExerciseTypeEntity sPart2Images = upsertType("speaking_part2_images", "Part 2 - Images", 3, speaking);
            ExerciseTypeEntity sPart2ImagesDiscussion = upsertType("speaking_part2_images_discussion", "Part 2 - Images Discussion", 4, speaking);
            ExerciseTypeEntity sPart3Paired = upsertType("speaking_part3_paired_discussion", "Part 3 - Paired Discussion", 5, speaking);
            ExerciseTypeEntity sPart3Bubbles = upsertType("speaking_part3_bubbles_discussion", "Part 3 - Bubbles Discussion", 6, speaking);
            ExerciseTypeEntity sPart3LongTurn = upsertType("speaking_part3_long_turn", "Part 3 - Long Turn", 7, speaking);
            ExerciseTypeEntity sPart4Final = upsertType("speaking_part4_final_discussion", "Part 4 - Final Discussion", 8, speaking);

            // --- USE OF ENGLISH ---
            ExerciseTypeEntity uMultipleChoice = upsertType("uoe_multiple_choice", "Multiple Choice", 1, useOfEnglish);
            ExerciseTypeEntity uOpenCloze = upsertType("uoe_open_cloze", "Open Cloze", 2, useOfEnglish);
            ExerciseTypeEntity uKeyWord = upsertType("uoe_key_word_transformations", "Key Word Transformations", 3, useOfEnglish);
            ExerciseTypeEntity uWordFormation = upsertType("uoe_word_formation", "Word Formation", 4, useOfEnglish);

            // --- WRITING ---
            ExerciseTypeEntity wPart1Email = upsertType("writing_part1_email", "part1_email", 1, writing);
            ExerciseTypeEntity wPart2ArticleStory = upsertType("writing_part2_article_or_story", "part2_article-or-story", 2, writing);

            ExerciseTypeEntity wPart1Essay = upsertType("writing_part1_essay", "part1_essay", 3, writing);
            ExerciseTypeEntity wPart2ReportReviewLetterArticle = upsertType("writing_part2_report_review_letter_or_article", "part2_report-review-letter-or-article", 4, writing);
            ExerciseTypeEntity wPart2ReportReviewLetterProposal = upsertType("writing_part2_report_review_letter_or_proposal", "part2_report-review-letter-or-proposal", 5, writing);

            // --- GRAMMAR TESTS ---
            ExerciseTypeEntity gBeginner = upsertType("grammar_beginner", "Beginner", 1, grammarTests);
            ExerciseTypeEntity gIntermediate = upsertType("grammar_intermediate", "Intermediate", 2, grammarTests);
            ExerciseTypeEntity gExpert = upsertType("grammar_expert", "Expert", 3, grammarTests);

            // =========================
            // 4) ASIGNAR TIPOS por NIVEL según tu dump
            // =========================

            // ----- B1 -----
            b1.setAvailableTypes(new HashSet<>(Set.of(
                    // Listening B1
                    lPictures, lExtracts, lMultipleChoice, lGappedText, lMatching,
                    // Reading B1
                    rLongText, rMissingSentences, rMatching, rSigns,
                    // Speaking B1
                    sPart1, sPart2Photo, sPart3Paired, sPart4Final,
                    // Use of English B1
                    uMultipleChoice, uOpenCloze,
                    // Writing B1
                    wPart1Email, wPart2ArticleStory
            )));
            levelRepo.save(b1);

            // ----- B2 -----
            b2.setAvailableTypes(new HashSet<>(Set.of(
                    // Grammar-tests B2
                    gBeginner,
                    // Listening B2
                    lPictures, lExtracts, lMultipleChoice, lGappedText, lMatching,
                    // Reading B2
                    rLongText, rMissingParagraphs, rMultipleMatching,
                    // Speaking B2
                    sPart1, sPart2Images, sPart3Bubbles, sPart4Final,
                    // Use of English B2
                    uKeyWord, uMultipleChoice, uOpenCloze, uWordFormation,
                    // Writing B2
                    wPart1Essay, wPart2ReportReviewLetterArticle
            )));
            levelRepo.save(b2);

            // ----- C1 -----
            c1.setAvailableTypes(new HashSet<>(Set.of(
                    // Grammar-tests C1
                    gIntermediate,
                    // Listening C1 (incluye Multiple Matching)
                    lPictures, lExtracts, lMultipleChoice, lGappedText, lMatching, lMultipleMatching,
                    // Reading C1 (incluye Cross Matching)
                    rCrossMatching, rLongText, rMissingParagraphs, rMultipleMatching,
                    // Speaking C1
                    sPart1, sPart2Images, sPart3Bubbles, sPart4Final,
                    // Use of English C1
                    uKeyWord, uMultipleChoice, uOpenCloze, uWordFormation,
                    // Writing C1
                    wPart1Essay, wPart2ReportReviewLetterProposal
            )));
            levelRepo.save(c1);

            // ----- C2 -----
            c2.setAvailableTypes(new HashSet<>(Set.of(
                    // Grammar-tests C2
                    gExpert,
                    // Listening C2 (incluye Multiple Matching)
                    lPictures, lExtracts, lMultipleChoice, lGappedText, lMatching, lMultipleMatching,
                    // Reading C2
                    rLongText, rMissingParagraphs, rMultipleMatching,
                    // Speaking C2 (solo 3 partes)
                    sPart1, sPart2ImagesDiscussion, sPart3LongTurn,
                    // Use of English C2
                    uKeyWord, uMultipleChoice, uOpenCloze, uWordFormation,
                    // Writing C2
                    wPart1Essay, wPart2ReportReviewLetterArticle
            )));
            levelRepo.save(c2);
        };
    }

    // =========================
    // Helpers (upsert idempotente)
    // =========================
    private EnglishLevelEntity upsertLevel(String code, String name, String description, Integer orderIndex) {
        return levelRepo.findByCode(code).map(existing -> {
            existing.setName(name);
            existing.setDescription(description);
            existing.setOrderIndex(orderIndex);
            return levelRepo.save(existing);
        }).orElseGet(() -> {
            var l = new EnglishLevelEntity();
            l.setCode(code);
            l.setName(name);
            l.setDescription(description);
            l.setOrderIndex(orderIndex);
            return levelRepo.save(l);
        });
    }

    private ExerciseCategoryEntity upsertCategory(String code, String name, Integer orderIndex) {
        return catRepo.findByCode(code).map(existing -> {
            existing.setName(name);
            existing.setOrderIndex(orderIndex);
            return catRepo.save(existing);
        }).orElseGet(() -> {
            var c = new ExerciseCategoryEntity();
            c.setCode(code);
            c.setName(name);
            c.setOrderIndex(orderIndex);
            return catRepo.save(c);
        });
    }

    private ExerciseTypeEntity upsertType(String code, String name, Integer orderIndex, ExerciseCategoryEntity category) {
        return typeRepo.findByCode(code).map(existing -> {
            existing.setName(name);
            existing.setOrderIndex(orderIndex);
            existing.setCategory(category);
            return typeRepo.save(existing);
        }).orElseGet(() -> {
            var t = new ExerciseTypeEntity();
            t.setCode(code);
            t.setName(name);
            t.setOrderIndex(orderIndex);
            t.setCategory(category);
            return typeRepo.save(t);
        });
    }

    private static <T> Set<T> union(Set<T> a, Set<T> b) {
        Set<T> out = new HashSet<>(a);
        out.addAll(b);
        return out;
    }
}

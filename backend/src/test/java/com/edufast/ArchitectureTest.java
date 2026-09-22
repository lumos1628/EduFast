package com.edufast;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import jakarta.persistence.Entity;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

/**
 * EL GUARDIÁN DE LA ARQUITECTURA.
 * Estos tests fallan si alguien (humano o agente de IA) rompe las reglas de capas:
 *
 *   infrastructure ──► application ──► domain
 *   (las dependencias solo apuntan hacia adentro)
 *
 * Si ./gradlew test pasa, la arquitectura está intacta.
 */
@AnalyzeClasses(packages = "com.edufast", importOptions = ImportOption.DoNotIncludeTests.class)
class ArchitectureTest {

    @ArchTest
    static final ArchRule el_dominio_no_depende_de_ningun_framework = noClasses()
            .that().resideInAPackage("..domain..")
            .should().dependOnClassesThat().resideInAnyPackage(
                    "..infrastructure..",
                    "jakarta..",
                    "org.springframework..")
            .as("El dominio debe ser puro: sin JPA, sin Spring, sin infraestructura");

    @ArchTest
    static final ArchRule los_modelos_de_dominio_no_son_entidades_jpa = noClasses()
            .that().resideInAPackage("..domain.model..")
            .should().beAnnotatedWith(Entity.class)
            .as("Las entidades JPA viven en infrastructure/persistence/entity");

    @ArchTest
    static final ArchRule application_no_depende_de_infrastructure = noClasses()
            .that().resideInAPackage("..application..")
            .should().dependOnClassesThat().resideInAPackage("..infrastructure..")
            .as("Los servicios solo usan puertos del dominio, nunca clases de infraestructura");

    @ArchTest
    static final ArchRule los_controllers_no_tocan_la_persistencia = noClasses()
            .that().resideInAPackage("..infrastructure.controller..")
            .should().dependOnClassesThat().resideInAPackage("..infrastructure.persistence..")
            .as("Los controllers hablan con servicios y DTOs, nunca con repositorios ni entidades JPA");
}

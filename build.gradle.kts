// Top-level build file where you can add configuration options common to all sub-projects/modules.
import javax.xml.parsers.DocumentBuilderFactory

val htmlReportFile = rootProject.buildDir.resolve("reports/test-summary/test-summary.html")

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.ktlint)
    alias(libs.plugins.hilt.android) apply false
    id("jacoco")
}

ktlint {
    version.set("1.2.1")
    enableExperimentalRules.set(true)
    additionalEditorconfig.set(mapOf("common" to project.file(".editorconfig").path))
}

buildscript {
    dependencies {
        classpath(libs.org.jacoco.core)
    }
}

jacoco {
    toolVersion = "0.8.10"
}

tasks.register<JacocoReport>("jacocoMergedReport") {
    group = "verification"
    description = "Generates a merged Jacoco coverage report for all modules."

    val modules = listOf("data", "app") // ajusta los módulos según tu estructura

    val executionDataFiles =
        files(
            modules.map {
                file("$it/build/jacoco/testDebugUnitTest.exec")
            },
        )

    val classDirs =
        files(
            modules.map {
                fileTree("$it/build/tmp/kotlin-classes/debug") {
                    exclude(
                        "**/R.class",
                        "**/R$*.class",
                        "**/BuildConfig.*",
                        "**/Manifest*.*",
                        "**/*Test*.*",
                        "**/Hilt*.*",
                        "**/*\$*",
                    )
                }
            },
        )

    val srcDirs = files(modules.map { "$it/src/main/java" })

    executionData.setFrom(executionDataFiles)
    sourceDirectories.setFrom(srcDirs)
    classDirectories.setFrom(classDirs)

    reports {
        html.required.set(true)
        html.outputLocation.set(layout.buildDirectory.dir("reports/jacoco/html"))
        xml.required.set(true)
        csv.required.set(false)
    }

    dependsOn(modules.map { ":$it:testDebugUnitTest" }) // asegura que se ejecuten los tests antes del reporte
}

tasks.register("generateTestSummaryHtml") {
    group = "verification"
    description = "Genera un reporte HTML con porcentaje de tests por clase"

    doLast {
        val reports = mutableListOf<TestClassResult>()

        rootProject.subprojects.forEach { subproject ->
            val reportsDir = subproject.buildDir.resolve("test-results/testDebugUnitTest")
            if (!reportsDir.exists()) return@forEach

            reportsDir.listFiles { file -> file.extension == "xml" }?.forEach { xmlFile ->
                val doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(xmlFile)
                val testsuite = doc.documentElement
                val className = testsuite.getAttribute("name")
                val tests = testsuite.getAttribute("tests").toIntOrNull() ?: 0
                val failures = testsuite.getAttribute("failures").toIntOrNull() ?: 0
                val errors = testsuite.getAttribute("errors").toIntOrNull() ?: 0
                val skipped = testsuite.getAttribute("skipped").toIntOrNull() ?: 0

                val passed = tests - failures - errors - skipped
                val successRate = if (tests > 0) (passed.toDouble() / tests) * 100 else 0.0

                reports.add(TestClassResult(className, tests, passed, failures + errors, skipped, successRate))
            }
        }

        htmlReportFile.parentFile.mkdirs()
        htmlReportFile.writeText(buildHtml(reports))
        println("✅ Reporte HTML generado: file://${htmlReportFile.absolutePath}")
    }
}

fun buildHtml(results: List<TestClassResult>): String =
    """
        <!DOCTYPE html>
        <html lang="en">
        <head>
            <meta charset="UTF-8">
            <title>Test Summary Report</title>
            <style>
                body { font-family: Arial, sans-serif; padding: 20px; }
                table { border-collapse: collapse; width: 100%; }
                th, td { border: 1px solid #ddd; padding: 8px; text-align: center; }
                th { background-color: #4CAF50; color: white; }
                tr:nth-child(even) { background-color: #f2f2f2; }
                .fail { color: red; font-weight: bold; }
                .pass { color: green; font-weight: bold; }
            </style>
        </head>
        <body>
            <h2>📊 Test Summary Report</h2>
            <table>
                <thead>
                    <tr>
                        <th>Test Class</th>
                        <th>Total</th>
                        <th>✅ Passed</th>
                        <th>❌ Failed</th>
                        <th>⚠️ Skipped</th>
                        <th>% Success</th>
                    </tr>
                </thead>
                <tbody>
                    ${
        results.joinToString("\n") {
            "<tr>" +
                "<td>${it.className}</td>" +
                "<td>${it.total}</td>" +
                "<td>${it.passed}</td>" +
                "<td class='${if (it.failed > 0) "fail" else ""}'>${it.failed}</td>" +
                "<td>${it.skipped}</td>" +
                "<td class='${if (it.successRate == 100.0) "pass" else "fail"}'>${"%.2f".format(it.successRate)}%</td>" +
                "</tr>"
        }
    }
                </tbody>
            </table>
        </body>
        </html>
    """.trimIndent()

data class TestClassResult(
    val className: String,
    val total: Int,
    val passed: Int,
    val failed: Int,
    val skipped: Int,
    val successRate: Double,
)

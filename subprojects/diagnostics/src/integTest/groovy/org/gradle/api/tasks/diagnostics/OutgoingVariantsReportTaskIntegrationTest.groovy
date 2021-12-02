/*
 * Copyright 2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gradle.api.tasks.diagnostics

import org.gradle.api.JavaVersion
import org.gradle.integtests.fixtures.AbstractIntegrationSpec
import org.gradle.integtests.fixtures.ToBeFixedForConfigurationCache

class OutgoingVariantsReportTaskIntegrationTest extends AbstractIntegrationSpec {
    def setup() {
        settingsFile << """
            rootProject.name = "myLib"
        """
    }

    @ToBeFixedForConfigurationCache(because = ":outgoingVariants")
    def "reports outgoing variants of a Java Library"() {
        buildFile << """
            plugins { id 'java-library' }
            group = 'org'
            version = '1.0'
        """

        when:
        run ':outgoingVariants'

        then:
        def jarPath = file('build/libs/myLib-1.0.jar').getRelativePathFromBase()
        def builtMainClassesPath = file('build/classes/java/main').getRelativePathFromBase()
        def builtMainResourcesPath = file('build/resources/main').getRelativePathFromBase()
        def sourceMainJavaPath = file('src/main/java').getRelativePathFromBase()
        def sourceMainResourcePath = file( 'src/main/resources').getRelativePathFromBase()
        def resultsBinPath = file('build/test-results/test/binary').getRelativePathFromBase()
        outputContains """> Task :outgoingVariants
--------------------------------------------------
Variant apiElements
--------------------------------------------------
Description = API elements for main.

Capabilities
    - org:myLib:1.0 (default capability)
Attributes
    - org.gradle.category            = library
    - org.gradle.dependency.bundling = external
    - org.gradle.jvm.version         = ${JavaVersion.current().majorVersion}
    - org.gradle.libraryelements     = jar
    - org.gradle.usage               = java-api

Artifacts
    - $jarPath (artifactType = jar)

Secondary variants (*)
    - Variant : classes
       - Attributes
          - org.gradle.category            = library
          - org.gradle.dependency.bundling = external
          - org.gradle.jvm.version         = ${JavaVersion.current().majorVersion}
          - org.gradle.libraryelements     = classes
          - org.gradle.usage               = java-api
       - Artifacts
          - $builtMainClassesPath (artifactType = java-classes-directory)

--------------------------------------------------
Variant mainSourceElements
--------------------------------------------------
Capabilities
    - org:myLib:1.0 (default capability)
Attributes
    - org.gradle.category            = verification
    - org.gradle.dependency.bundling = external
    - org.gradle.verificationtype    = main-sources

Artifacts
    - $sourceMainJavaPath (artifactType = directory)
    - $sourceMainResourcePath (artifactType = directory)

--------------------------------------------------
Variant runtimeElements
--------------------------------------------------
Description = Elements of runtime for main.

Capabilities
    - org:myLib:1.0 (default capability)
Attributes
    - org.gradle.category            = library
    - org.gradle.dependency.bundling = external
    - org.gradle.jvm.version         = ${JavaVersion.current().majorVersion}
    - org.gradle.libraryelements     = jar
    - org.gradle.usage               = java-runtime

Artifacts
    - $jarPath (artifactType = jar)

Secondary variants (*)
    - Variant : classes
       - Attributes
          - org.gradle.category            = library
          - org.gradle.dependency.bundling = external
          - org.gradle.jvm.version         = ${JavaVersion.current().majorVersion}
          - org.gradle.libraryelements     = classes
          - org.gradle.usage               = java-runtime
       - Artifacts
          - $builtMainClassesPath (artifactType = java-classes-directory)
    - Variant : resources
       - Attributes
          - org.gradle.category            = library
          - org.gradle.dependency.bundling = external
          - org.gradle.jvm.version         = ${JavaVersion.current().majorVersion}
          - org.gradle.libraryelements     = resources
          - org.gradle.usage               = java-runtime
       - Artifacts
          - $builtMainResourcesPath (artifactType = java-resources-directory)

--------------------------------------------------
Variant testResultsElementsForTest
--------------------------------------------------
Capabilities
    - org:myLib:1.0 (default capability)
Attributes
    - org.gradle.category              = verification
    - org.gradle.testsuite.name        = test
    - org.gradle.testsuite.target.name = test
    - org.gradle.testsuite.type        = unit-test
    - org.gradle.verificationtype      = test-results

Artifacts
    - $resultsBinPath (artifactType = directory)
"""
        and:
        doesNotHaveLegacyVariantsLegend()
        hasSecondaryVariantsLegend()
    }

    @ToBeFixedForConfigurationCache(because = ":outgoingVariants")
    def "reports outgoing variants of a Java Library with documentation"() {
        buildFile << """
            plugins { id 'java-library' }
            java {
                withJavadocJar()
                withSourcesJar()
            }
            group = 'org'
            version = '1.0'
        """

        when:
        run ':outgoingVariants'

        then:
        def jarPath = file('build/libs/myLib-1.0.jar').getRelativePathFromBase()
        def javadocJarPath = file('build/libs/myLib-1.0-javadoc.jar').getRelativePathFromBase()
        def sourcesJarPath = file('build/libs/myLib-1.0-sources.jar').getRelativePathFromBase()
        def builtMainClassesPath = file('build/classes/java/main').getRelativePathFromBase()
        def builtMainResourcesPath = file('build/resources/main').getRelativePathFromBase()
        def sourceMainJavaPath = file('src/main/java').getRelativePathFromBase()
        def sourceMainResourcePath = file('src/main/resources').getRelativePathFromBase()
        def resultsBinPath = file('build/test-results/test/binary').getRelativePathFromBase()
        outputContains """> Task :outgoingVariants
--------------------------------------------------
Variant apiElements
--------------------------------------------------
Description = API elements for main.

Capabilities
    - org:myLib:1.0 (default capability)
Attributes
    - org.gradle.category            = library
    - org.gradle.dependency.bundling = external
    - org.gradle.jvm.version         = ${JavaVersion.current().majorVersion}
    - org.gradle.libraryelements     = jar
    - org.gradle.usage               = java-api

Artifacts
    - $jarPath (artifactType = jar)

Secondary variants (*)
    - Variant : classes
       - Attributes
          - org.gradle.category            = library
          - org.gradle.dependency.bundling = external
          - org.gradle.jvm.version         = ${JavaVersion.current().majorVersion}
          - org.gradle.libraryelements     = classes
          - org.gradle.usage               = java-api
       - Artifacts
          - $builtMainClassesPath (artifactType = java-classes-directory)

--------------------------------------------------
Variant javadocElements
--------------------------------------------------
Description = javadoc elements for main.

Capabilities
    - org:myLib:1.0 (default capability)
Attributes
    - org.gradle.category            = documentation
    - org.gradle.dependency.bundling = external
    - org.gradle.docstype            = javadoc
    - org.gradle.usage               = java-runtime

Artifacts
    - $javadocJarPath (artifactType = jar)

--------------------------------------------------
Variant mainSourceElements
--------------------------------------------------
Capabilities
    - org:myLib:1.0 (default capability)
Attributes
    - org.gradle.category            = verification
    - org.gradle.dependency.bundling = external
    - org.gradle.verificationtype    = main-sources

Artifacts
    - $sourceMainJavaPath (artifactType = directory)
    - $sourceMainResourcePath (artifactType = directory)

--------------------------------------------------
Variant runtimeElements
--------------------------------------------------
Description = Elements of runtime for main.

Capabilities
    - org:myLib:1.0 (default capability)
Attributes
    - org.gradle.category            = library
    - org.gradle.dependency.bundling = external
    - org.gradle.jvm.version         = ${JavaVersion.current().majorVersion}
    - org.gradle.libraryelements     = jar
    - org.gradle.usage               = java-runtime

Artifacts
    - $jarPath (artifactType = jar)

Secondary variants (*)
    - Variant : classes
       - Attributes
          - org.gradle.category            = library
          - org.gradle.dependency.bundling = external
          - org.gradle.jvm.version         = ${JavaVersion.current().majorVersion}
          - org.gradle.libraryelements     = classes
          - org.gradle.usage               = java-runtime
       - Artifacts
          - $builtMainClassesPath (artifactType = java-classes-directory)
    - Variant : resources
       - Attributes
          - org.gradle.category            = library
          - org.gradle.dependency.bundling = external
          - org.gradle.jvm.version         = ${JavaVersion.current().majorVersion}
          - org.gradle.libraryelements     = resources
          - org.gradle.usage               = java-runtime
       - Artifacts
          - $builtMainResourcesPath (artifactType = java-resources-directory)

--------------------------------------------------
Variant sourcesElements
--------------------------------------------------
Description = sources elements for main.

Capabilities
    - org:myLib:1.0 (default capability)
Attributes
    - org.gradle.category            = documentation
    - org.gradle.dependency.bundling = external
    - org.gradle.docstype            = sources
    - org.gradle.usage               = java-runtime

Artifacts
    - $sourcesJarPath (artifactType = jar)

--------------------------------------------------
Variant testResultsElementsForTest
--------------------------------------------------
Capabilities
    - org:myLib:1.0 (default capability)
Attributes
    - org.gradle.category              = verification
    - org.gradle.testsuite.name        = test
    - org.gradle.testsuite.target.name = test
    - org.gradle.testsuite.type        = unit-test
    - org.gradle.verificationtype      = test-results

Artifacts
    - $resultsBinPath (artifactType = directory)
"""
        and:
        doesNotHaveLegacyVariantsLegend()
        hasSecondaryVariantsLegend()
    }

    @ToBeFixedForConfigurationCache(because = ":outgoingVariants")
    def "reports a single outgoing variant of a Java Library"() {
        buildFile << """
            plugins { id 'java-library' }
            group = 'org'
            version = '1.0'
        """

        when:
        run ':outgoingVariants', '--variant', 'runtimeElements'

        then:
        def jarPath = file('build/libs/myLib-1.0.jar').getRelativePathFromBase()
        def builtMainClassesPath = file('build/classes/java/main').getRelativePathFromBase()
        def builtMainResourcesPath = file('build/resources/main').getRelativePathFromBase()
        outputContains """> Task :outgoingVariants
--------------------------------------------------
Variant runtimeElements
--------------------------------------------------
Description = Elements of runtime for main.

Capabilities
    - org:myLib:1.0 (default capability)
Attributes
    - org.gradle.category            = library
    - org.gradle.dependency.bundling = external
    - org.gradle.jvm.version         = ${JavaVersion.current().majorVersion}
    - org.gradle.libraryelements     = jar
    - org.gradle.usage               = java-runtime

Artifacts
    - $jarPath (artifactType = jar)

Secondary variants (*)
    - Variant : classes
       - Attributes
          - org.gradle.category            = library
          - org.gradle.dependency.bundling = external
          - org.gradle.jvm.version         = ${JavaVersion.current().majorVersion}
          - org.gradle.libraryelements     = classes
          - org.gradle.usage               = java-runtime
       - Artifacts
          - $builtMainClassesPath (artifactType = java-classes-directory)
    - Variant : resources
       - Attributes
          - org.gradle.category            = library
          - org.gradle.dependency.bundling = external
          - org.gradle.jvm.version         = ${JavaVersion.current().majorVersion}
          - org.gradle.libraryelements     = resources
          - org.gradle.usage               = java-runtime
       - Artifacts
          - $builtMainResourcesPath (artifactType = java-resources-directory)
"""

        and:
        doesNotHaveLegacyVariantsLegend()
        hasSecondaryVariantsLegend()
    }

    @ToBeFixedForConfigurationCache(because = ":outgoingVariants")
    def "lists all variant names when using a wrong variant name"() {
        buildFile << """
            plugins { id 'java-library' }
        """

        when:
        run ':outgoingVariants', '--variant', 'nope'

        then:
        outputContains("""> Task :outgoingVariants
There is no variant named 'nope' defined on this project.
Here are the available outgoing variants: apiElements, archives, default, mainSourceElements, runtimeElements, testResultsElementsForTest
""")
        and:
        doesNotHaveLegacyVariantsLegend()
        doesNotHaveSecondaryVariantsLegend()

    }

    @ToBeFixedForConfigurationCache(because = ":outgoingVariants")
    def "can show all variants"() {
        buildFile << """
            plugins { id 'java-library' }
            group = 'org'
            version = '1.0'
        """

        when:
        executer.expectDeprecationWarning('(l) Legacy or deprecated configuration. Those are variants created for backwards compatibility which are both resolvable and consumable.')
        run ':outgoingVariants', '--all'

        then:
        def jarPath = file('build/libs/myLib-1.0.jar').getRelativePathFromBase()
        def builtMainClassesPath = file('build/classes/java/main').getRelativePathFromBase()
        def builtMainResourcesPath = file('build/resources/main').getRelativePathFromBase()
        def sourceMainJavaPath = file('src/main/java').getRelativePathFromBase()
        def sourceMainResourcePath = file('src/main/resources').getRelativePathFromBase()
        def resultsBinPath = file('build/test-results/test/binary').getRelativePathFromBase()
        outputContains """> Task :outgoingVariants
--------------------------------------------------
Variant apiElements
--------------------------------------------------
Description = API elements for main.

Capabilities
    - org:myLib:1.0 (default capability)
Attributes
    - org.gradle.category            = library
    - org.gradle.dependency.bundling = external
    - org.gradle.jvm.version         = ${JavaVersion.current().majorVersion}
    - org.gradle.libraryelements     = jar
    - org.gradle.usage               = java-api

Artifacts
    - $jarPath (artifactType = jar)

Secondary variants (*)
    - Variant : classes
       - Attributes
          - org.gradle.category            = library
          - org.gradle.dependency.bundling = external
          - org.gradle.jvm.version         = ${JavaVersion.current().majorVersion}
          - org.gradle.libraryelements     = classes
          - org.gradle.usage               = java-api
       - Artifacts
          - $builtMainClassesPath (artifactType = java-classes-directory)

--------------------------------------------------
Variant archives (l)
--------------------------------------------------
Description = Configuration for archive artifacts.

Artifacts
    - $jarPath (artifactType = jar)

--------------------------------------------------
Variant default (l)
--------------------------------------------------
Description = Configuration for default artifacts.

Artifacts
    - $jarPath (artifactType = jar)

--------------------------------------------------
Variant mainSourceElements
--------------------------------------------------
Capabilities
    - org:myLib:1.0 (default capability)
Attributes
    - org.gradle.category            = verification
    - org.gradle.dependency.bundling = external
    - org.gradle.verificationtype    = main-sources

Artifacts
    - $sourceMainJavaPath (artifactType = directory)
    - $sourceMainResourcePath (artifactType = directory)

--------------------------------------------------
Variant runtimeElements
--------------------------------------------------
Description = Elements of runtime for main.

Capabilities
    - org:myLib:1.0 (default capability)
Attributes
    - org.gradle.category            = library
    - org.gradle.dependency.bundling = external
    - org.gradle.jvm.version         = ${JavaVersion.current().majorVersion}
    - org.gradle.libraryelements     = jar
    - org.gradle.usage               = java-runtime

Artifacts
    - $jarPath (artifactType = jar)

Secondary variants (*)
    - Variant : classes
       - Attributes
          - org.gradle.category            = library
          - org.gradle.dependency.bundling = external
          - org.gradle.jvm.version         = ${JavaVersion.current().majorVersion}
          - org.gradle.libraryelements     = classes
          - org.gradle.usage               = java-runtime
       - Artifacts
          - $builtMainClassesPath (artifactType = java-classes-directory)
    - Variant : resources
       - Attributes
          - org.gradle.category            = library
          - org.gradle.dependency.bundling = external
          - org.gradle.jvm.version         = ${JavaVersion.current().majorVersion}
          - org.gradle.libraryelements     = resources
          - org.gradle.usage               = java-runtime
       - Artifacts
          - $builtMainResourcesPath (artifactType = java-resources-directory)

--------------------------------------------------
Variant testResultsElementsForTest
--------------------------------------------------
Capabilities
    - org:myLib:1.0 (default capability)
Attributes
    - org.gradle.category              = verification
    - org.gradle.testsuite.name        = test
    - org.gradle.testsuite.target.name = test
    - org.gradle.testsuite.type        = unit-test
    - org.gradle.verificationtype      = test-results

Artifacts
    - $resultsBinPath (artifactType = directory)
"""

        and:
        hasLegacyVariantsLegend()
        hasSecondaryVariantsLegend()
    }

    @ToBeFixedForConfigurationCache(because = ":outgoingVariants")
    def "prints explicit capabilities"() {
        buildFile << """
            plugins { id 'java-library' }

            configurations.runtimeElements.outgoing {
                capability("org.test:extra:1.0")
                capability("org.test:other:3.0")
            }
"""

        when:
        run ':outgoingVariants', '--variant', 'runtimeElements'

        then:
        outputContains """> Task :outgoingVariants
--------------------------------------------------
Variant runtimeElements
--------------------------------------------------
Description = Elements of runtime for main.

Capabilities
    - org.test:extra:1.0
    - org.test:other:3.0
"""
    }

    @ToBeFixedForConfigurationCache(because = ":outgoingVariants")
    def "reports artifacts without explicit type"() {
        buildFile << """
            plugins { id 'java-library' }

            group = 'org'
            version = '1.0'

            configurations.runtimeElements.outgoing.variants {
                classes {
                   artifact(file("foo"))
                }
            }
        """

        when:
        run ':outgoingVariants', '--variant', 'runtimeElements'

        then:
        def jarPath = file('build/libs/myLib-1.0.jar').getRelativePathFromBase()
        def builtMainClassesPath = file('build/classes/java/main').getRelativePathFromBase()
        def builtMainResourcesPath = file('build/resources/main').getRelativePathFromBase()
        outputContains """> Task :outgoingVariants
--------------------------------------------------
Variant runtimeElements
--------------------------------------------------
Description = Elements of runtime for main.

Capabilities
    - org:myLib:1.0 (default capability)
Attributes
    - org.gradle.category            = library
    - org.gradle.dependency.bundling = external
    - org.gradle.jvm.version         = ${JavaVersion.current().majorVersion}
    - org.gradle.libraryelements     = jar
    - org.gradle.usage               = java-runtime

Artifacts
    - $jarPath (artifactType = jar)

Secondary variants (*)
    - Variant : classes
       - Attributes
          - org.gradle.category            = library
          - org.gradle.dependency.bundling = external
          - org.gradle.jvm.version         = ${JavaVersion.current().majorVersion}
          - org.gradle.libraryelements     = classes
          - org.gradle.usage               = java-runtime
       - Artifacts
          - $builtMainClassesPath (artifactType = java-classes-directory)
          - foo
    - Variant : resources
       - Attributes
          - org.gradle.category            = library
          - org.gradle.dependency.bundling = external
          - org.gradle.jvm.version         = ${JavaVersion.current().majorVersion}
          - org.gradle.libraryelements     = resources
          - org.gradle.usage               = java-runtime
       - Artifacts
          - $builtMainResourcesPath (artifactType = java-resources-directory)
"""

        and:
        doesNotHaveLegacyVariantsLegend()
        hasSecondaryVariantsLegend()
    }

    private void hasSecondaryVariantsLegend() {
        outputContains("(*) Secondary variants are variants created via the Configuration#getOutgoing(): ConfigurationPublications API which also participate in selection, in addition to the configuration itself.")
    }

    private void doesNotHaveSecondaryVariantsLegend() {
        outputDoesNotContain("(*) Secondary variants are variants created via the Configuration#getOutgoing(): ConfigurationPublications API which also participate in selection, in addition to the configuration itself.")
    }

    private void hasLegacyVariantsLegend() {
        outputContains("(l) Legacy or deprecated configuration. Those are variants created for backwards compatibility which are both resolvable and consumable.")
    }

    private void doesNotHaveLegacyVariantsLegend() {
        outputDoesNotContain("(l) Legacy or deprecated configuration. Those are variants created for backwards compatibility which are both resolvable and consumable.")
    }
}

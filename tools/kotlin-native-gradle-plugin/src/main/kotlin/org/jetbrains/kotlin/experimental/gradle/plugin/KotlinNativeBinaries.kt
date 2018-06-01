package org.jetbrains.kotlin.experimental.gradle.plugin

import org.gradle.api.attributes.Attribute
import org.gradle.api.component.BuildableComponent
import org.gradle.api.component.PublishableComponent
import org.gradle.api.file.FileCollection
import org.gradle.api.file.RegularFile
import org.gradle.api.provider.Provider
import org.gradle.language.ComponentWithDependencies
import org.gradle.language.ComponentWithOutputs
import org.gradle.language.nativeplatform.ComponentWithLinkFile
import org.gradle.language.nativeplatform.ComponentWithLinkUsage
import org.gradle.language.nativeplatform.internal.ConfigurableComponentWithLinkUsage
import org.gradle.language.nativeplatform.internal.ConfigurableComponentWithRuntimeUsage
import org.jetbrains.kotlin.experimental.gradle.plugin.internal.KotlinNativePlatform
import org.jetbrains.kotlin.experimental.gradle.plugin.tasks.KotlinNativeCompile
import org.jetbrains.kotlin.konan.target.CompilerOutputKind
import org.jetbrains.kotlin.konan.target.KonanTarget

// TODO: implement ComponentWithObjectFiles when we build klibs as objects
interface KotlinNativeBinary: ComponentWithDependencies, BuildableComponent {

    /** Returns the source files of this binary. */
    val sources: FileCollection

    /**
     * Konan target the library is built for
     */
    val konanTarget: KonanTarget

    /**
     * Gradle NativePlatform object the binary is built for.
     */
    fun getTargetPlatform(): KotlinNativePlatform

    /** Compile task for this library */
    val compileTask: Provider<KotlinNativeCompile>

    // TODO: Support native link and runtime libraries here.
    // Looks like we need at least 3 file collections here: for klibs, for linktime native libraries and for runtime native libraries.
    /**
     * The link libraries (klibs only!) used to link this binary.
     * Includes the link libraries of the component's dependencies.
     */
    val klibraries: FileCollection

    /**
     * Binary kind in terms of the KN compiler (program, library, dynamic etc).
     */
    val kind: CompilerOutputKind

    // TODO: May be rework it a little.
    companion object {
        val KONAN_TARGET_ATTRIBUTE = Attribute.of("org.gradle.native.kotlin.platform", String::class.java)
    }
}

/**
 * Represents Kotlin/Native executable.
 */
// TODO: Consider implementing ComponentWithExecutable and ComponentWithInstallation.
interface KotlinNativeExecutable : KotlinNativeBinary,
        ComponentWithOutputs,
        PublishableComponent,
        ConfigurableComponentWithRuntimeUsage

/**
 *  A component representing a klibrary.
 */
// TODO: Consider implementing ComponentWithLinkFile.
interface KotlinNativeKLibrary : KotlinNativeBinary,
        ComponentWithOutputs,
        PublishableComponent,
        ConfigurableComponentWithLinkUsage

// TODO: Support.
interface KotlinNativeSharedLibrary {
}
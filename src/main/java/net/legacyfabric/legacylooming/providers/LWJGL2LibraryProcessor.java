package net.legacyfabric.legacylooming.providers;

import net.fabricmc.loom.configuration.providers.minecraft.library.Library;
import net.fabricmc.loom.configuration.providers.minecraft.library.LibraryContext;
import net.fabricmc.loom.configuration.providers.minecraft.library.LibraryProcessor;
import net.fabricmc.loom.util.Platform;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class LWJGL2LibraryProcessor extends LibraryProcessor {
    public static final String VERSION = "2.9.4+legacyfabric.5";
    public LWJGL2LibraryProcessor(Platform platform, LibraryContext context) {
        super(platform, context);
    }

    @Override
    public ApplicationResult getApplicationResult() {
        if (!context.usesLWJGL3()) {

            return ApplicationResult.MUST_APPLY;
        }

        return ApplicationResult.DONT_APPLY;
    }

    @Override
    public Predicate<Library> apply(Consumer<Library> dependencyConsumer) {
        return library -> {
            System.out.println(library.group() + " " + library.name() + " " + library.version() + " " + library.classifier() + " " + library.target().name());
            if (library.group().equals("org.lwjgl.lwjgl")) {
                final Library.Target target = library.target() == Library.Target.NATIVES ? Library.Target.NATIVES : Library.Target.RUNTIME;
                final Library upgradedLibrary = library.withVersion(VERSION).withTarget(target);
                dependencyConsumer.accept(upgradedLibrary);

//                return false;
            }

            return true;
        };
    }
}

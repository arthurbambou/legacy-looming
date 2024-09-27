package net.legacyfabric.legacylooming.providers;

import net.fabricmc.loom.LoomGradleExtension;
import net.fabricmc.loom.api.LoomGradleExtensionAPI;
import net.fabricmc.loom.configuration.providers.mappings.IntermediaryMappingsProvider;
import net.legacyfabric.legacylooming.Constants;
import net.legacyfabric.legacylooming.LegacyLoomingExtensionAPI;
import org.gradle.api.Project;
import org.gradle.api.provider.Provider;
import org.jetbrains.annotations.NotNull;

public abstract class LegacyFabricIntermediaryMappingsProvider extends IntermediaryMappingsProvider {
    private Project project;
    private String name;

    @Override
    public @NotNull String getName() {
        if (this.name == null) {
            this.name = LegacyLoomingExtensionAPI.get(project)
                .getIntermediaryVersion().map(Constants::getIntermediaryName).get();
        }

        return this.name;
    }

    private static Provider<String> getCorrectIntermediaryURL(boolean useLfIntermediary, LegacyLoomingExtensionAPI api) {
        if (useLfIntermediary) {
            return api.getIntermediaryVersion().map(Constants::getIntermediaryURL);
        }

        return api.getIntermediaryVersion().map(v -> "https://maven.fabricmc.net/net/fabricmc/intermediary/%1$s/intermediary-%1$s-v2.jar");
    }

    public void configure(Project project, LegacyLoomingExtensionAPI api, LoomGradleExtensionAPI loom) {
        this.project = project;

        this.getIntermediaryUrl()
            .convention(api.useLFIntermediary().flatMap(useLfIntermediary -> getCorrectIntermediaryURL(useLfIntermediary, api)))
            .finalizeValueOnRead();

        this.getRefreshDeps().set(project.provider(() -> LoomGradleExtension.get(project).refreshDeps()));
    }
}

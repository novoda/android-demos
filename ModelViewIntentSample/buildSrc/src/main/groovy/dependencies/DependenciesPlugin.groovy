package dependencies

import org.gradle.api.Plugin
import org.gradle.api.Project

class DependenciesPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.ext.libraries = new Libraries()
        project.ext.gradlePlugins = new Plugins()
    }
}

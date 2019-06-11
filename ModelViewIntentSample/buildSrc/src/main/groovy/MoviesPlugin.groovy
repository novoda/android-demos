import dependencies.DependenciesPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project

class MoviesPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        new DependenciesPlugin().apply(project)
    }
}

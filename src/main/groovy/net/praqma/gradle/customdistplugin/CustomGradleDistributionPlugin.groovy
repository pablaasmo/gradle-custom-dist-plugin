package net.praqma.gradle.customdistplugin

import org.gradle.api.Plugin
import org.gradle.api.Project

class CustomGradleDistributionPlugin implements Plugin<Project>{

	@Override
	public void apply(Project project) {
		project.apply(plugin: 'base')
		
		project.task('customGradleDistribution', type: CustomGradleDistribution) {
			description 'Customize a Gradle distribution'
			group 'Build Setup'
		}
	}
}


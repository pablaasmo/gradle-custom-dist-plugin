package net.praqma.gradle.customdistplugin;

import org.gradle.api.tasks.Input
import org.gradle.api.tasks.bundling.Zip
import org.gradle.wrapper.Download
import org.gradle.wrapper.IDownload
import org.gradle.wrapper.Install
import org.gradle.wrapper.PathAssembler
import org.gradle.wrapper.WrapperConfiguration

class CustomGradleDistribution extends Zip {

	/** URL of the Gradle distribution to customize */
	@Input
	String baseDistribution

	CustomGradleDistribution() {
		from { downloadGradleDistribution() }
	}

	/** Download and install a Gradle distribution */
	File downloadGradleDistribution() {
		PathAssembler pathAssembler = new PathAssembler(project.gradle.gradleUserHomeDir)
		IDownload download = new Download(project.name, project.version)
		Install install = new Install(download, pathAssembler)
		WrapperConfiguration conf = new WrapperConfiguration()
		conf.distribution = URI.create(baseDistribution)
		return install.createDist(conf)
	}
}

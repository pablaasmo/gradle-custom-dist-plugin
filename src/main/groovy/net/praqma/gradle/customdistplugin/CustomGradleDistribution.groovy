package net.praqma.gradle.customdistplugin;

import org.gradle.api.GradleException
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.bundling.Zip
import org.gradle.util.DistributionLocator
import org.gradle.util.GradleVersion
import org.gradle.wrapper.Download
import org.gradle.wrapper.IDownload
import org.gradle.wrapper.Install
import org.gradle.wrapper.PathAssembler
import org.gradle.wrapper.WrapperConfiguration

class CustomGradleDistribution extends Zip {

	/**
	 * URL of the Gradle distribution to customize.
	 * <p>
	 * Either baseDistribution or gradleVersion must be set, but not both.
	 */
	@Optional @Input
	String baseDistribution


	/** 
	 * Version of the base Gradle distribution.
	 * <p>
	 * Either baseDistribution or gradleVersion must be set, but not both.
	 */
	@Optional @Input
	String gradleVersion

	/**
	 * The name of the generated Gradle distribution.
	 * <p>
	 * It is use as the name of the top-level folder in the zip. It defaults to
	 * the archiveName with the extension removed.
	 */
	@Optional @Input
	String distributionName

	CustomGradleDistribution() {
		into {
			distributionName ?: archiveName.lastIndexOf('.').with {
				it == -1 ? archiveName : archiveName[0..<it]
			}
		}
		from { downloadGradleDistribution() }
	}

	/** Download and install a Gradle distribution */
	File downloadGradleDistribution() {
		WrapperConfiguration conf = new WrapperConfiguration()
		conf.distribution = getBaseDistributionUri()
		PathAssembler pathAssembler = new PathAssembler(project.gradle.gradleUserHomeDir)
		IDownload download = new Download(project.name, project.version)
		Install install = new Install(download, pathAssembler)
		return install.createDist(conf)
	}

	private URI getBaseDistributionUri() {
		if (baseDistribution != null && gradleVersion != null) {
			throw new GradleException("Both 'baseDistribution' and 'gradleVersion' is defined. Only set one of them.")
		}
		if (baseDistribution == null && gradleVersion == null) {
			throw new GradleException("Either 'baseDistribution' or 'gradleVersion' must be set.")
		}
		if (gradleVersion != null) {
			def locator = new DistributionLocator()
			return locator.getDistributionFor(GradleVersion.version(gradleVersion))
		} else {
			URI.create(baseDistribution)
		}
	}

}

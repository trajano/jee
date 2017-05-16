package net.trajano.jee.domain.nlp.test

import net.trajano.jee.nlp.impl.CoreNlp
import org.junit.AfterClass
import org.junit.Assert
import org.junit.BeforeClass
import org.junit.Test
import java.util.logging.LogManager

class CoreNlpTest {
	companion object {
		@BeforeClass @JvmStatic fun setup() {
			Thread.currentThread().getContextClassLoader().getResourceAsStream("test-logging.properties").use {
				LogManager.getLogManager().readConfiguration(it)
			}
		}

		@AfterClass @JvmStatic fun teardown() {
			LogManager.getLogManager().reset()
		}
	}

	/**
	 * Ensure Kotlin is setup correctly.
	 */
	@Test
	fun sanityTest() {
		Assert.assertTrue(1 == 1)
	}

	@Test
	fun testAnalyze() {
		val c = CoreNlp()
		val r = c.analyze("Archie loves Janet very much.  That much is QUITE true.");
		Assert.assertEquals(2, r.sentences.size)
	}
}
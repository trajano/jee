package net.trajano.jee.domain.dao.test

import org.junit.Assert
import org.junit.Test
import net.trajano.jee.nlp.impl.CoreNlp

class CoreNlpTest {
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
		c.analyze("Archie loves Janet very much.");
	}
}
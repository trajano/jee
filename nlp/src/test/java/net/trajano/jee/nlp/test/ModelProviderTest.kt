package net.trajano.jee.domain.nlp.test

import net.trajano.jee.nlp.impl.ModelProvider
import net.trajano.jee.nlp.test.FakeLobDAO
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test
import java.util.logging.LogManager

class ModelProviderTest {
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

	@Test
	fun testProvider() {
		val provider = ModelProvider()
		provider.setLobDAO(FakeLobDAO())
		provider.loadFromDatabase()

		// Reloading should not be an issue
		provider.loadFromDatabase()
		provider.persistCurrentNetwork()
	}
}
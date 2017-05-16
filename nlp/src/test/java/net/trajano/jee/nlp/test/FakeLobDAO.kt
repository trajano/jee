package net.trajano.jee.nlp.test

import net.trajano.jee.domain.dao.LobDAO
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.io.DataInputStream

/**
 * This is a non-persistent LobDAO implementation used for testing.
 *
 * @author Archimedes Trajano
 */
class FakeLobDAO : LobDAO {
	override fun remove(name: String) {
		store.remove(name)
	}

	override fun update(name: String, input: InputStream) {
		store.put(name, input.readBytes())
	}

	override fun getInputStream(name: String): InputStream? {
		if (store.contains(name)) {
			return ByteArrayInputStream(store.get(name))
		} else {
			return null
		}
	}

	private val store = mutableMapOf<String, ByteArray>();

}
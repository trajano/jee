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
	override fun update(id: Long, input: InputStream) {
		store.put(id, input.readBytes())
	}

	override fun getInputStream(id: Long): InputStream? {
		if (store.contains(id)) {
			return ByteArrayInputStream(get(id))
		} else {
			return null
		}
	}

	private val store = mutableMapOf<Long, ByteArray>();

	override fun get(id: Long): ByteArray? {
		return store.get(id)
	}

	override fun set(id: Long,
					 data: ByteArray) {
		store.put(id, data)
	}
}
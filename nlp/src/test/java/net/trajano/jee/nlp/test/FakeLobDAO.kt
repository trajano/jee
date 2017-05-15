package net.trajano.jee.nlp.test

import net.trajano.jee.domain.dao.LobDAO

/**
 * This is a non-persistent LobDAO implementation used for testing.
 *
 * @author Archimedes Trajano
 */
class FakeLobDAO : LobDAO {
	private val store = mutableMapOf<Long, ByteArray>();

	override fun get(id: Long): ByteArray? {
		return store.get(id)
	}

	override fun set(id: Long,
					 data: ByteArray) {
		store.put(id, data)
	}
}
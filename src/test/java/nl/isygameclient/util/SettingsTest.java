package nl.isygameclient.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class SettingsTest {

	@Test
	void ShouldGiveTitle() {
		assertEquals(String.class, SettingsHandler.getSettings().getTitle().getClass());
	}
}
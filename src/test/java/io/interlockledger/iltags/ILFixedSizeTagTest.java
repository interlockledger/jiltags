/*
 * Copyright 2019 InterlockLedger Network
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.interlockledger.iltags;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Random;

import org.junit.Test;

import io.interlockledger.iltags.io.ILMemoryTagDataReader;

public class ILFixedSizeTagTest {

	@Test
	public void testDeserializeValue() throws Exception {

		for (int i = 0; i < 64; i++) {
			ILTestFixedSizeTag t = new ILTestFixedSizeTag(i + 1, i);
			ILMemoryTagDataReader in = new ILMemoryTagDataReader(new byte[10]);
			t.deserializeValue(null, i, in);
		}

		for (int i = 0; i < 64; i++) {
			ILTestFixedSizeTag t = new ILTestFixedSizeTag(i + 1, i);
			ILMemoryTagDataReader in = new ILMemoryTagDataReader(new byte[10]);
			try {
				t.deserializeValue(null, i - 1, in);
				fail();
			} catch (ILTagException e) {
			}
			;
			try {
				t.deserializeValue(null, i + 1, in);
				fail();
			} catch (ILTagException e) {
			}
			;
		}
	}

	@Test
	public void testEquals() {
		ILTestFixedSizeTag t1 = new ILTestFixedSizeTag(16, 16);
		ILTestFixedSizeTag t2 = new ILTestFixedSizeTag(16, 16);
		ILTestFixedSizeTag t3 = new ILTestFixedSizeTag(15, 16);
		ILTestFixedSizeTag t4 = new ILTestFixedSizeTag(16, 15);

		assertTrue(t1.equals(t1));
		assertTrue(t1.equals(t2));
		assertFalse(t1.equals(null));
		assertFalse(t1.equals(t3));
		assertFalse(t1.equals(t4));
	}

	@Test
	public void testGetValueSize() {
		Random random = new Random();

		for (int i = 0; i < 32; i++) {
			int size = Math.abs(random.nextInt());
			ILTestFixedSizeTag t = new ILTestFixedSizeTag(i + 1, size);
			assertEquals(i + 1, t.getId());
			assertEquals(size, t.getValueSize());
		}
	}

	@Test
	public void testILFixedSizeTag() {

		for (int i = 0; i < 64; i++) {
			ILTestFixedSizeTag t = new ILTestFixedSizeTag(i + 1, i);
			assertEquals(i, t.getValueSize());
			assertEquals(i + 1, t.getId());
		}
	}
}

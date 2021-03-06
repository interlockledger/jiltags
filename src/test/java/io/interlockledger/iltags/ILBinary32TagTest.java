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

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.nio.ByteBuffer;

import org.junit.Test;

import io.interlockledger.iltags.io.ILMemoryTagDataReader;
import io.interlockledger.iltags.io.ILMemoryTagDataWriter;

public class ILBinary32TagTest {

	@Test
	public void testDeserializeValueCore() throws Exception {
		ByteBuffer b = ByteBuffer.allocate(4);

		ILBinary32Tag t = new ILBinary32Tag();
		float value = 1234.567f;
		b.putFloat(value);
		ILMemoryTagDataReader r = new ILMemoryTagDataReader(b.array());
		t.deserializeValueCore(null, r);
		assertEquals(value, t.getValue(), 0.0f);
	}

	@Test(expected = ILTagException.class)
	public void testDeserializeValueCoreFail() throws Exception {
		ByteBuffer b = ByteBuffer.allocate(3);

		ILBinary32Tag t = new ILBinary32Tag();
		ILMemoryTagDataReader r = new ILMemoryTagDataReader(b.array());
		t.deserializeValueCore(null, r);
	}

	@Test
	public void testEquals() {
		ILBinary32Tag t1 = new ILBinary32Tag();
		ILBinary32Tag t2 = new ILBinary32Tag();
		ILBinary32Tag t3 = new ILBinary32Tag();
		t3.setValue(1);
		ILBinary32Tag t4 = new ILBinary32Tag(15);

		assertTrue(t1.equals(t1));
		assertTrue(t1.equals(t2));
		assertFalse(t1.equals(null));
		assertFalse(t1.equals(t3));
		assertFalse(t1.equals(t4));
	}

	@Test
	public void testGetSetValue() {
		ILBinary32Tag t = new ILBinary32Tag();

		assertEquals(0.0f, t.getValue(), 0.0f);

		float value = 1234.567f;
		t.setValue(value);
		assertEquals(value, t.getValue(), 0.0f);
	}

	@Test
	public void testILBinary32Tag() {
		ILBinary32Tag t = new ILBinary32Tag();

		assertEquals(ILStandardTags.TAG_BINARY32.ordinal(), t.getId());
		assertEquals(0.0f, t.getValue(), 0.0f);
	}

	@Test
	public void testILBinary32TagLong() {
		ILBinary32Tag t = new ILBinary32Tag(1234);

		assertEquals(1234, t.getId());
		assertEquals(0.0f, t.getValue(), 0.0f);
	}

	@Test
	public void testSerializeValue() throws Exception {
		ByteBuffer b = ByteBuffer.allocate(4);

		float value = 1234.567f;
		ILBinary32Tag t = new ILBinary32Tag();
		b.putFloat(value);
		ILMemoryTagDataWriter w = new ILMemoryTagDataWriter();
		t.setValue(value);
		t.serializeValue(w);
		assertArrayEquals(b.array(), w.toByteArray());
	}

}

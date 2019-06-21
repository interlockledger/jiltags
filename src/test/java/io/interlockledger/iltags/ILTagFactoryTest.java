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

import static org.junit.Assert.*;

import org.junit.Test;

import io.interlockledger.iltags.io.ILMemoryTagDataReader;
import io.interlockledger.iltags.io.ILMemoryTagDataWriter;
import io.interlockledger.iltags.io.ILTagDataReader;

public class ILTagFactoryTest {
	
	@Test
	public void testCreate() {
		ILTagFactory f = new ILTagFactory();
		ILTag t;
		long tagId;
		
		tagId = ILStandardTags.TAG_NULL;
		t = f.create(tagId);
		assertTrue(t instanceof ILNullTag);
		assertEquals(tagId, t.getId());
		
		tagId = ILStandardTags.TAG_BOOL;
		t = f.create(tagId);
		assertTrue(t instanceof ILBooleanTag);
		assertEquals(tagId, t.getId());
		
		tagId = ILStandardTags.TAG_INT8;
		t = f.create(tagId);
		assertTrue(t instanceof ILInt8Tag);
		assertEquals(tagId, t.getId());
		
		tagId = ILStandardTags.TAG_UINT8;
		t = f.create(tagId);
		assertTrue(t instanceof ILUInt8Tag);
		assertEquals(tagId, t.getId());
		
		tagId = ILStandardTags.TAG_INT16;
		t = f.create(tagId);
		assertTrue(t instanceof ILInt16Tag);
		assertEquals(tagId, t.getId());

		tagId = ILStandardTags.TAG_UINT16;
		t = f.create(tagId);
		assertTrue(t instanceof ILUInt16Tag);
		assertEquals(tagId, t.getId());
		
		tagId = ILStandardTags.TAG_INT32;
		t = f.create(tagId);
		assertTrue(t instanceof ILInt32Tag);
		assertEquals(tagId, t.getId());
		
		tagId = ILStandardTags.TAG_UINT32;
		t = f.create(tagId);
		assertTrue(t instanceof ILUInt32Tag);
		assertEquals(tagId, t.getId());
		
		tagId = ILStandardTags.TAG_INT64;
		t = f.create(tagId);
		assertTrue(t instanceof ILInt64Tag);
		assertEquals(tagId, t.getId());
		
		tagId = ILStandardTags.TAG_UINT64;
		t = f.create(tagId);
		assertTrue(t instanceof ILUInt64Tag);
		assertEquals(tagId, t.getId());
		
		tagId = ILStandardTags.TAG_ILINT64;
		t = f.create(tagId);
		assertTrue(t instanceof ILILIntTag);
		assertEquals(tagId, t.getId());
		
		tagId = ILStandardTags.TAG_BINARY32;
		t = f.create(tagId);
		assertTrue(t instanceof ILBinary32Tag);
		assertEquals(tagId, t.getId());
		
		tagId = ILStandardTags.TAG_BINARY64;
		t = f.create(tagId);
		assertTrue(t instanceof ILBinary64Tag);
		assertEquals(tagId, t.getId());		
		
		tagId = ILStandardTags.TAG_BINARY128;
		t = f.create(tagId);
		assertTrue(t instanceof ILBinary128Tag);
		assertEquals(tagId, t.getId());		

		// Reserved
		for (long reserved = 14; reserved < 16; reserved++) {
			t = f.create(reserved);
			assertNull(t);
		}		
		
		tagId = ILStandardTags.TAG_BYTE_ARRAY;
		t = f.create(tagId);
		assertTrue(t instanceof ILByteArrayTag);
		assertEquals(tagId, t.getId());
		
		tagId = ILStandardTags.TAG_STRING;
		t = f.create(tagId);
		assertTrue(t instanceof ILStringTag);
		assertEquals(tagId, t.getId());
		
		tagId = ILStandardTags.TAG_BINT;
		t = f.create(tagId);
		assertTrue(t instanceof ILBigIntTag);
		assertEquals(tagId, t.getId());		

		tagId = ILStandardTags.TAG_BDEC;
		t = f.create(tagId);
		assertTrue(t instanceof ILBigDecimalTag);
		assertEquals(tagId, t.getId());
		
		tagId = ILStandardTags.TAG_ILINT64_ARRAY;
		t = f.create(tagId);
		assertTrue(t instanceof ILILIntArrayTag);
		assertEquals(tagId, t.getId());
		
		tagId = ILStandardTags.TAG_ILTAG_ARRAY;
		t = f.create(tagId);
		assertTrue(t instanceof ILTagArrayTag	);
		assertEquals(tagId, t.getId());
		
		tagId = ILStandardTags.TAG_ILTAG_SEQ;
		t = f.create(tagId);
		assertTrue(t instanceof ILTagSequenceTag);
		assertEquals(tagId, t.getId());
		
		tagId = ILStandardTags.TAG_RANGE;
		t = f.create(tagId);
		assertTrue(t instanceof ILRangeTag);
		assertEquals(tagId, t.getId());
		
		tagId = ILStandardTags.TAG_VERSION;
		t = f.create(tagId);
		assertTrue(t instanceof ILVersionTag);
		assertEquals(tagId, t.getId());
		
		tagId = ILStandardTags.TAG_OID;
		t = f.create(tagId);
		assertTrue(t instanceof ILOIDTag);
		assertEquals(tagId, t.getId());

		// Reserved
		for (long reserved = 26; reserved < 32; reserved++) {
			t = f.create(reserved);
			assertNull(t);
		}		
	}
	
	private static <T extends ILTag> T serializeAndDeserialize(T tag, Class<T> tagClass) throws ILTagException {
		ILTagFactory f = new ILTagFactory();
		ILMemoryTagDataWriter out = new ILMemoryTagDataWriter();
		tag.serialize(out);
		ILTagDataReader in = new ILMemoryTagDataReader(out.toByteArray());
		ILTag ret =  f.deserialize(in);
		assertNotSame(tag, ret);
		return tagClass.cast(ret);
	}

	@Test
	public void testDeserializeILNullTag() throws Exception {
		ILNullTag src = new ILNullTag();
		ILNullTag dst = serializeAndDeserialize(src, ILNullTag.class);
		assertNotNull(dst);
	}

	@Test
	public void testDeserializeILBooleanTag() throws Exception {
		ILBooleanTag src = new ILBooleanTag();
		src.setValue(true);
		ILBooleanTag dst = serializeAndDeserialize(src, ILBooleanTag.class);
		assertEquals(src.getValue(), dst.getValue());
	}
	
	@Test
	public void testDeserializeILInt8Tag() throws Exception {
		ILInt8Tag src = new ILInt8Tag();
		src.setValue((byte)0x12);
		ILInt8Tag dst = serializeAndDeserialize(src, ILInt8Tag.class);
		assertEquals(src.getValue(), dst.getValue());
	}
	
	@Test
	public void testDeserializeILUInt8Tag() throws Exception {
		ILUInt8Tag src = new ILUInt8Tag();
		src.setValue((byte)0x12);
		ILUInt8Tag dst = serializeAndDeserialize(src, ILUInt8Tag.class);
		assertEquals(src.getValue(), dst.getValue());
	}

	@Test
	public void testDeserializeILInt16Tag() throws Exception {
		ILInt16Tag src = new ILInt16Tag();
		src.setValue((short)0x1234);
		ILInt16Tag dst = serializeAndDeserialize(src, ILInt16Tag.class);
		assertEquals(src.getValue(), dst.getValue());
	}
	
	@Test
	public void testDeserializeILUInt16Tag() throws Exception {
		ILUInt16Tag src = new ILUInt16Tag();
		src.setValue((short)0x1234);
		ILUInt16Tag dst = serializeAndDeserialize(src, ILUInt16Tag.class);
		assertEquals(src.getValue(), dst.getValue());
	}

	@Test
	public void testDeserializeILInt32Tag() throws Exception {
		ILInt32Tag src = new ILInt32Tag();
		src.setValue(0x12345678);
		ILInt32Tag dst = serializeAndDeserialize(src, ILInt32Tag.class);
		assertEquals(src.getValue(), dst.getValue());
	}
	
	@Test
	public void testDeserializeILUInt32Tag() throws Exception {
		ILUInt32Tag src = new ILUInt32Tag();
		src.setValue(0x12345678);
		ILUInt32Tag dst = serializeAndDeserialize(src, ILUInt32Tag.class);
		assertEquals(src.getValue(), dst.getValue());
	}
	
	@Test
	public void testDeserializeILInt64Tag() throws Exception {
		ILInt64Tag src = new ILInt64Tag();
		src.setValue(0x1234567890ABCDEFl);
		ILInt64Tag dst = serializeAndDeserialize(src, ILInt64Tag.class);
		assertEquals(src.getValue(), dst.getValue());
	}
	
	@Test
	public void testDeserializeILUInt64Tag() throws Exception {
		ILUInt64Tag src = new ILUInt64Tag();
		src.setValue(0x1234567890ABCDEFl);
		ILUInt64Tag dst = serializeAndDeserialize(src, ILUInt64Tag.class);
		assertEquals(src.getValue(), dst.getValue());
	}
	
	@Test
	public void testDeserializeILILIntTag() throws Exception {
		ILILIntTag src = new ILILIntTag();
		src.setValue(0x1234567890ABCDEFl);
		ILILIntTag dst = serializeAndDeserialize(src, ILILIntTag.class);
		assertEquals(src.getValue(), dst.getValue());
	}	
	
	
	@Test
	public void testILTagFactory() {
		ILTagFactory f = new ILTagFactory();
		assertFalse(f.isStrictMode());
	}	
	
	@Test
	public void testIsSetStrictMode() {
		ILTagFactory f = new ILTagFactory();
		
		assertFalse(f.isStrictMode());
		f.setStrictMode(true);
		assertTrue(f.isStrictMode());
		f.setStrictMode(false);
		assertFalse(f.isStrictMode());
	}
}

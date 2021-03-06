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
package io.interlockledger.iltags.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;

import org.junit.Test;

public class TestInputStreamTest {

	@Test
	public void testIsCloseUsed() throws Exception {
		TestInputStream in = new TestInputStream(new ByteArrayInputStream(new byte[0]));

		assertFalse(in.isCloseUsed());
		in.close();
		assertTrue(in.isCloseUsed());
	}

	@Test
	public void testSkip() throws Exception {
		TestInputStream in = new TestInputStream(new ByteArrayInputStream(new byte[100]));

		for (int i = 0; i < 100; i++) {
			assertEquals(0, in.skip(i));
		}
		in.close();
	}
}

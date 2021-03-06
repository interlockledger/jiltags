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

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayDeque;
import java.util.Deque;

import io.interlockledger.iltags.ILTagException;
import io.interlockledger.iltags.ilint.ILIntCodec;
import io.interlockledger.iltags.ilint.ILIntException;

/**
 * Base class for ILTagDataReader implementations. It provides the
 * implementation for most of the methods of the interface, simplifying the
 * implementation of the subclasses.
 * 
 * @author Fabio Jun Takada Chino
 * @since 2019.06.14
 */
public abstract class ILBaseTagDataReader implements ILTagDataReader {

	private final ByteBuffer tmp;

	private final Deque<Long> limits = new ArrayDeque<>();

	private long offset = 0;

	// The value Long.MAX_VALUE will be used to denote the unlimited
	private long currentLimit = Long.MAX_VALUE;

	protected ILBaseTagDataReader() {
		tmp = ByteBuffer.allocate(8);
		tmp.order(ByteOrder.BIG_ENDIAN);
	}

	@Override
	public long getOffset() {
		return this.offset;
	}

	@Override
	public long getRemaining() {
		return this.currentLimit - this.offset;
	}

	@Override
	public boolean isLimited() {
		return (this.currentLimit != Long.MAX_VALUE);
	}

	@Override
	public void popLimit(boolean checkRemaining) throws ILTagException {

		if (this.limits.isEmpty()) {
			throw new IllegalStateException("No limits to pop.");
		}
		if ((checkRemaining) && (this.getRemaining() > 0)) {
			throw new ILTagTooMuchDataException(
					String.format("The reader still have %1$d unread bytes.", this.getRemaining()));
		}
		this.currentLimit = this.limits.pop();
	}

	@Override
	public void pushLimit(long size) {

		if (size < 0) {
			throw new IllegalArgumentException("The size cannot be negative.");
		}
		long newLimit = this.offset + size;
		if (newLimit < 0) {
			throw new IllegalArgumentException("Overflow.");
		}
		if ((this.isLimited()) && (newLimit > this.currentLimit)) {
			throw new IllegalArgumentException("The new size exceeds the available");
		}
		this.limits.push(this.currentLimit);
		this.currentLimit = newLimit;
	}

	@Override
	public byte readByte() throws ILTagException {
		updateOffset(1);
		return this.readByteCore();
	}

	/**
	 * Reads a byte from the data source. It is called by readByte() after the
	 * validation of read limits.
	 * 
	 * @return The byte read.
	 * @throws ILTagException In case of error.
	 */
	protected abstract byte readByteCore() throws ILTagException;

	@Override
	public void readBytes(byte[] v) throws ILTagException {
		readBytes(v, 0, v.length);
	}

	@Override
	public void readBytes(byte[] v, int off, int size) throws ILTagException {
		updateOffset(size);
		readBytesCore(v, off, size);
	}

	private void readBytes(ByteBuffer tmp, int size) throws ILTagException {
		readBytes(tmp.array(), 0, size);
		tmp.rewind();
	}

	/**
	 * Reads bytes from the data source. It is called by readBytes(byte[],int,int)
	 * after the validation of read limits.
	 * 
	 * <p>
	 * This method must succeed if and only if all bytes are read.
	 * </p>
	 * .
	 * 
	 * @param v    The buffer that will receive the data.
	 * @param off  The offset.
	 * @param size The number of bytes to read.
	 * @throws ILTagException In case of error.
	 */
	protected abstract void readBytesCore(byte[] v, int off, int size) throws ILTagException;

	@Override
	public double readDouble() throws ILTagException {
		readBytes(tmp, 8);
		return tmp.getDouble();
	}

	@Override
	public float readFloat() throws ILTagException {
		readBytes(tmp, 4);
		return tmp.getFloat();

	}

	@Override
	public long readILInt() throws ILTagException {
		try {
			return ILIntCodec.decode(this, ILTagDataReaderHandler.INSTANCE);
		} catch (ILIntException e) {
			if (e.getCause() instanceof ILTagNotEnoughDataException) {
				throw new ILTagNotEnoughDataException(e.getCause().getMessage(), e.getCause());
			} else {
				throw new ILTagException(e.getMessage(), e);
			}
		}
	}

	@Override
	public int readInt() throws ILTagException {
		readBytes(tmp, 4);
		return tmp.getInt();
	}

	@Override
	public long readLong() throws ILTagException {
		readBytes(tmp, 8);
		return tmp.getLong();

	}

	@Override
	public short readShort() throws ILTagException {
		readBytes(tmp, 2);
		return tmp.getShort();
	}

	@Override
	public String readString(long n) throws ILTagException {
		StringBuilder v = new StringBuilder();
		this.readString(n, v);
		return v.toString();
	}

	@Override
	public long readString(long n, Appendable v) throws ILTagException {

		if (n < 0) {
			throw new IllegalArgumentException("n cannot be negative.");
		}
		if (n == 0) {
			return 0;
		}
		try {
			this.pushLimit(n);
			int count = 0;
			char[] tmpChar = new char[2];
			do {
				this.tmp.array()[0] = this.readByte();
				int charLen = UTF8Utils.getUTF8EncodedCharSize(this.tmp.array()[0]);
				if (charLen > 1) {
					this.readBytes(this.tmp.array(), 1, charLen - 1);
				}
				int cp = UTF8Utils.toCodepoint(this.tmp.array(), charLen);
				int surrogateLen = Character.charCount(cp);
				Character.toChars(cp, tmpChar, 0);
				v.append(tmpChar[0]);
				if (surrogateLen == 2) {
					v.append(tmpChar[1]);
				}
				count += surrogateLen;
			} while (this.getRemaining() > 0);
			this.popLimit(true);
			return count;
		} catch (IOException | IllegalArgumentException e) {
			throw new ILTagException("Unable to read the string.", e);
		}
	}

	@Override
	public void skip(long n) throws ILTagException {
		updateOffset(n);
		skipCore(n);
	}

	/**
	 * Skips a certain number of bytes from the data source. It is called by
	 * skip(long) after the validation of read limits.
	 * 
	 * <p>
	 * This method must succeed if and only if all bytes are skipped.
	 * </p>
	 * .
	 * 
	 * @param n The number of bytes to skip.
	 * @throws ILTagException In case of error.
	 */
	protected abstract void skipCore(long n) throws ILTagException;

	/**
	 * Updates the offset by registering the use of n bytes. It also verifies if the
	 * specified number of bytes can be used based on the current limit.
	 * 
	 * @param n The number of bytes to use.
	 * @throws ILTagNotEnoughDataException If there are not enough bytes to be read.
	 */
	protected void updateOffset(long n) throws ILTagNotEnoughDataException {

		if (n > this.getRemaining()) {
			throw new ILTagNotEnoughDataException(
					String.format("Trying to read %1$d bytes out of %2$d.", n, this.getRemaining()));
		}
		this.offset += n;
	}
}

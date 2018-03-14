package io;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

import util.Cheat;

public class SerializerBuffer {
	private static final int BUFFER_SIZE = 1024;
	
	private final ByteBuffer buffer;
	
	private SerializerBuffer(ByteBuffer buffer) {
		this.buffer = buffer;
	}
	
	public SerializerBuffer(int size) {
		this(ByteBuffer.allocate(size));
	}
	
	public SerializerBuffer() {
		this(BUFFER_SIZE);
	}
	
	public ByteBuffer getBuffer() {
		return buffer;
	}
	
	public int read(ReadableByteChannel channel) throws IOException {
		return channel.read(buffer);
	}
	
	public int write(WritableByteChannel channel) throws IOException {
		return channel.write(buffer);
	}
	
	public SerializerBuffer flip() {
		buffer.flip();
		return this;
	}
	
	public SerializerBuffer reset() {
		buffer.reset();
		return this;
	}
	
	public SerializerBuffer mark() {
		buffer.mark();
		return this;
	}
	
	public byte get() {
		return buffer.get();
	}

	public byte get(int index) {
		return buffer.get(index);
	}

	public ByteBuffer get(byte[] dst, int offset, int length) {
		return buffer.get(dst, offset, length);
	}

	public ByteBuffer get(byte[] dst) {
		return buffer.get(dst);
	}

	public char getChar() {
		return buffer.getChar();
	}

	public char getChar(int index) {
		return buffer.getChar(index);
	}

	public short getShort() {
		return buffer.getShort();
	}

	public short getShort(int index) {
		return buffer.getShort(index);
	}

	public int getInt() {
		return buffer.getInt();
	}

	public int getInt(int index) {
		return buffer.getInt(index);
	}

	public long getLong() {
		return buffer.getLong();
	}

	public long getLong(int index) {
		return buffer.getLong(index);
	}

	public float getFloat() {
		return buffer.getFloat();
	}

	public float getFloat(int index) {
		return buffer.getFloat(index);
	}

	public double getDouble() {
		return buffer.getDouble();
	}

	public double getDouble(int index) {
		return buffer.getDouble(index);
	}

	public SerializerBuffer clear() {
		buffer.clear();
		return this;
	}
	
	public SerializerBuffer slice() {
		return new SerializerBuffer(buffer.slice());
	}
	
	public void write(byte b) {
		buffer.put(b);
	}
	
	public void writeInt(int i) {
		buffer.putInt(i);
	}
	
	public void writeFloat(float f) {
		buffer.putFloat(f);
	}
	
	public void writeString(String s) {
		buffer.putInt(s.length());
		buffer.put(Cheat.CHARSET.encode(s));
	}
	
	public String getString() {
		int length = buffer.getInt();
		int limit = buffer.limit();
		buffer.limit(buffer.position()+length);
		String s = Cheat.CHARSET.decode(buffer).toString();
		buffer.limit(limit);
		return s;
	}
	
	@Override
	public String toString() {
		return Cheat.CHARSET.decode(buffer).toString();
	}
}

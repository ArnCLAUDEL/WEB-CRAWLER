package io;

import java.io.IOException;
import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

import util.Cheat;

public class SerializerBuffer {
	private static final int BUFFER_SIZE = 1024;
	
	private final ByteBuffer buffer;
	
	private Optional<Consumer<? super SerializerBuffer>> overflowCallback;
	
	private SerializerBuffer(ByteBuffer buffer) {
		this.buffer = buffer;
		this.overflowCallback = Optional.empty();
	}
	
	public SerializerBuffer(int size) {
		this(ByteBuffer.allocate(size));
	}
	
	public SerializerBuffer() {
		this(BUFFER_SIZE);
	}
	
	public void setOverflowCallback(Consumer<? super SerializerBuffer> callback) {
		this.overflowCallback = Optional.ofNullable(callback);
	}
	
	public <T> T handleFlowException(Supplier<T> supplier) {
		buffer.mark();
		try {
			return supplier.get();
		} catch (BufferOverflowException | BufferUnderflowException e) {
			buffer.reset();
			overflowCallback.orElseThrow(() -> e).accept(this);
			return handleFlowException(supplier);
		}
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
	
	public SerializerBuffer compact() {
		buffer.compact();
		return this;
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
	
	public final int position() {
		return buffer.position();
	}

	public final int remaining() {
		return buffer.remaining();
	}

	public SerializerBuffer put(byte b) {
		buffer.put(b);
		return this;
	}

	public SerializerBuffer put(int index, byte b) {
		buffer.put(index, b);
		return this;
	}

	public SerializerBuffer put(ByteBuffer src) {
		buffer.put(src);
		return this;
	}

	public SerializerBuffer put(byte[] src, int offset, int length) {
		buffer.put(src, offset, length);
		return this;
	}

	public final SerializerBuffer put(byte[] src) {
		buffer.put(src);
		return this;
	}

	public SerializerBuffer putChar(char value) {
		buffer.putChar(value);
		return this;
	}

	public SerializerBuffer putChar(int index, char value) {
		buffer.putChar(index, value);
		return this;
	}

	public SerializerBuffer putShort(short value) {
		buffer.putShort(value);
		return this;
	}

	public SerializerBuffer putShort(int index, short value) {
		buffer.putShort(index, value);
		return this;
	}

	public SerializerBuffer putInt(int value) {
		buffer.putInt(value);
		return this;
	}

	public SerializerBuffer putInt(int index, int value) {
		buffer.putInt(index, value);
		return this;
	}

	public SerializerBuffer putLong(long value) {
		buffer.putLong(value);
		return this;
	}

	public SerializerBuffer putLong(int index, long value) {
		buffer.putLong(index, value);
		return this;
	}

	public SerializerBuffer putFloat(float value) {
		buffer.putFloat(value);
		return this;
	}

	public SerializerBuffer putFloat(int index, float value) {
		buffer.putFloat(index, value);
		return this;
	}

	public SerializerBuffer putDouble(double value) {
		buffer.putDouble(value);
		return this;
	}

	public SerializerBuffer putDouble(int index, double value) {
		buffer.putDouble(index, value);
		return this;
	}

	// TODO
	public void putString(String s) {
		handleFlowException(() -> {
			buffer.putInt(s.length());
			buffer.put(Cheat.CHARSET.encode(s));
			return Void.TYPE;
		});
	}
	
	public String getString() {
		return handleFlowException(() -> {
			int length = buffer.getInt();
			int limit = buffer.limit();
			if(buffer.position()+length > buffer.limit())
				throw new BufferUnderflowException();
			buffer.limit(buffer.position()+length);
			String s = Cheat.CHARSET.decode(buffer).toString();
			buffer.limit(limit);
			return s;
		});		
	}
	
	@Override
	public String toString() {
		return Cheat.CHARSET.decode(buffer).toString();
	}
}

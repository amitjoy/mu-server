package ronin.muserver;

import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpHeaders;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Headers implements Iterable<Map.Entry<String, String>> {

	private final HttpHeaders entries;

	public Headers() {
		this(new DefaultHttpHeaders());
	}
	Headers(HttpHeaders entries) {
		this.entries = entries;
	}

	public String get(String name) {
		return entries.get(name);
	}

	public String get(CharSequence name) {
		return entries.get(name);
	}

	public String get(CharSequence name, String defaultValue) {
		return entries.get(name, defaultValue);
	}

	public Integer getInt(CharSequence name) {
		return entries.getInt(name);
	}

	public int getInt(CharSequence name, int defaultValue) {
		return entries.getInt(name, defaultValue);
	}

	public Short getShort(CharSequence name) {
		return entries.getShort(name);
	}

	public short getShort(CharSequence name, short defaultValue) {
		return entries.getShort(name, defaultValue);
	}

	public Long getTimeMillis(CharSequence name) {
		return entries.getTimeMillis(name);
	}

	public long getTimeMillis(CharSequence name, long defaultValue) {
		return entries.getTimeMillis(name, defaultValue);
	}

	public List<String> getAll(String name) {
		return entries.getAll(name);
	}

	public List<String> getAll(CharSequence name) {
		return entries.getAll(name);
	}

	public List<Map.Entry<String, String>> entries() {
		return entries.entries();
	}

	public boolean contains(String name) {
		return entries.contains(name);
	}

	public Iterator<Map.Entry<String, String>> iterator() {
		return entries.iterator();
	}

	public Iterator<Map.Entry<CharSequence, CharSequence>> iteratorCharSequence() {
		return entries.iteratorCharSequence();
	}

	public boolean contains(CharSequence name) {
		return entries.contains(name);
	}

	public boolean isEmpty() {
		return entries.isEmpty();
	}

	public int size() {
		return entries.size();
	}

	public Set<String> names() {
		return entries.names();
	}

	public Headers add(String name, Object value) {
		entries.add(name, value);
		return this;
	}

	public Headers add(CharSequence name, Object value) {
		entries.add(name, value);
		return this;
	}

	public Headers add(String name, Iterable<?> values) {
		entries.add(name, values);
		return this;
	}

	public Headers add(CharSequence name, Iterable<?> values) {
		entries.add(name, values);
		return this;
	}

	public Headers add(HttpHeaders headers) {
		entries.add(headers);
		return this;
	}

	public Headers addInt(CharSequence name, int value) {
		entries.addInt(name, value);
		return this;
	}

	public Headers addShort(CharSequence name, short value) {
		entries.addShort(name, value);
		return this;
	}

	public Headers set(String name, Object value) {
		entries.set(name, value);
		return this;
	}

	public Headers set(CharSequence name, Object value) {
		entries.set(name, value);
		return this;
	}

	public Headers set(String name, Iterable<?> values) {
		entries.set(name, values);
		return this;
	}

	public Headers set(CharSequence name, Iterable<?> values) {
		entries.set(name, values);
		return this;
	}

	public Headers set(HttpHeaders headers) {
		entries.set(headers);
		return this;
	}

	public Headers setAll(HttpHeaders headers) {
		entries.setAll(headers);
		return this;
	}

	public Headers setInt(CharSequence name, int value) {
		entries.setInt(name, value);
		return this;
	}

	public Headers setShort(CharSequence name, short value) {
		entries.setShort(name, value);
		return this;
	}

	public Headers remove(String name) {
		entries.remove(name);
		return this;
	}

	public Headers remove(CharSequence name) {
		entries.remove(name);
		return this;
	}

	public Headers clear() {
		entries.clear();
		return this;
	}

	public boolean contains(String name, String value, boolean ignoreCase) {
		return entries.contains(name, value, ignoreCase);
	}

	public boolean containsValue(CharSequence name, CharSequence value, boolean ignoreCase) {
		return entries.containsValue(name, value, ignoreCase);
	}

	public String getAsString(CharSequence name) {
		return entries.getAsString(name);
	}

	public List<String> getAllAsString(CharSequence name) {
		return entries.getAllAsString(name);
	}

	public Iterator<Map.Entry<String, String>> iteratorAsString() {
		return entries.iteratorAsString();
	}

	public boolean contains(CharSequence name, CharSequence value, boolean ignoreCase) {
		return entries.contains(name, value, ignoreCase);
	}

	public boolean equals(Object o) {
		return entries.equals(o);
	}

	public int hashCode() {
		return entries.hashCode();
	}

	HttpHeaders nettyHeaders() {
		return entries;
	}
}
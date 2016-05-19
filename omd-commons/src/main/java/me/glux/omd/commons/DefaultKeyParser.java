package me.glux.omd.commons;
public class DefaultKeyParser implements KeyParser {

	private Map<String,ParsedKey> cache=new HashMap<>();

	@Override
	public String parse(Map<String, Object> values, String pattern) {
		ParsedKey key = cache.get(pattern);
		if (null == key) {
			key = new ParsedKey(pattern);
			cache.put(pattern, key);
		}
		return key.evel(values);
	}

	private static class ParsedKey {
		private List<KeyElement> elements = new ArrayList<>(5);

		public ParsedKey(String pattern) {
			parse(pattern);
		}

		public String evel(Map<String, Object> values) {
			StringBuilder sb = new StringBuilder(256);
			for (KeyElement element : elements) {
				sb.append(element.getContent(values));
			}
			return sb.toString();
		}

		private void parse(String pattern) {
			Flag flag = Flag.Suffix;
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < pattern.length(); i++) {
				char ch = pattern.charAt(i);
				switch (flag) {
				case Suffix: {
					switch (ch) {
					case '$': {
						if (sb.length() > 0) {
							elements.add(new BlankString(sb.toString()));
						}
						sb.delete(0, sb.length());
						flag = Flag.StartFlag;
						break;
					}
					default: {
						sb.append(ch);
					}
					}
					break;
				}
				case StartFlag: {
					switch (ch) {
					case '{': {
						flag = Flag.Prefix;
						break;
					}
					default: {
						sb.append(ch);
						if (sb.length() > 0) {
							elements.add(new BlankString(sb.toString()));
						}
						sb.delete(0, sb.length());
						flag = Flag.Suffix;
					}
					}
					break;
				}
				case Prefix: {
					switch (ch) {
					case '}': {
						elements.add(new PlaceHolder(sb.toString()));
						flag = Flag.Suffix;
						sb.delete(0, sb.length());
						break;
					}
					default: {
						sb.append(ch);
					}
					}
					break;
				}
				}
			}
			if (sb.length() > 0) {
				elements.add(new BlankString(sb.toString()));
			}
		}
	}

	private enum Flag {
		Suffix('}'), StartFlag('$'), Prefix('{');
		private char ch;

		private Flag(char ch) {
			this.ch = ch;
		}

		public char getCh() {
			return ch;
		}
	}

	private static abstract class KeyElement {
		public abstract String getContent(Map<String, Object> values);
	}

	private static class BlankString extends KeyElement {
		private String value;

		public BlankString(String value) {
			this.value = value;
		}

		@Override
		public String getContent(Map<String, Object> values) {
			return value;
		}
	}

	private static class PlaceHolder extends KeyElement {
		private String name;

		public PlaceHolder(String name) {
			this.name = name;
		}

		@Override
		public String getContent(Map<String, Object> values) {
			Object value = values.get(name);
			if (null == value) {
				throw new MissingKeyPatternParameterException(name);
			}
			return value.toString();
		}
	}
}
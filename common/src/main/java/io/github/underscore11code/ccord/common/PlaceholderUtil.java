package io.github.underscore11code.ccord.common;

import java.util.Set;
import java.util.function.Supplier;

public final class PlaceholderUtil {
  private PlaceholderUtil() {
  }

  public static Placeholder simple(final String name, final String value) {
    return new SimplePlaceholder(name, value);
  }

  public static Placeholder lazy(final String name, final Supplier<String> value) {
    return new LazyPlaceholder(name, value);
  }

  public static String applyPlaceholders(String in, final Placeholder... placeholders) {
    for (final Placeholder p : placeholders) {
      if (!in.contains(p.angleBracketKey())) continue;
      in = in.replace(p.angleBracketKey(), p.value());
    }
    return in;
  }

  public static String applyPlaceholders(final String in, final Set<Placeholder> placeholders) {
    return applyPlaceholders(in, (Placeholder[]) placeholders.toArray());
  }

  public interface Placeholder {
    String key();

    default String angleBracketKey() {
      return "<" + this.key() + ">";
    }

    default String curlyBracketKey() {
      return "%" + this.key() + "%";
    }

    default String percentKey() {
      return "%" + this.key() + "%";
    }

    String value();
  }

  public static final class SimplePlaceholder implements Placeholder {
    private final String key;
    private final String value;

    private SimplePlaceholder(final String key, final String value) {
      this.key = key;
      this.value = value;
    }

    @Override
    public String key() {
      return this.key;
    }

    @Override
    public String value() {
      return this.value;
    }
  }

  public static final class LazyPlaceholder implements Placeholder {
    private final String key;
    private final Supplier<String> value;

    private LazyPlaceholder(final String key, final Supplier<String> value) {
      this.key = key;
      this.value = value;
    }

    @Override
    public String key() {
      return this.key;
    }

    @Override
    public String value() {
      return this.value.get();
    }
  }
}

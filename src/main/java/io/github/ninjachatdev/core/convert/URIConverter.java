package io.github.ninjachatdev.core.convert;

public abstract class URIConverter implements IConverter {
    /**
     * Get the URI.
     * URIを取得します。
     *
     * @return URI.
     */
    public abstract String getURI(String input);

    /**
     * Parse the input.
     * 入力を解析します。
     *
     * @param input input string.
     * @return output string.
     */
    public abstract String parse(String input);
}

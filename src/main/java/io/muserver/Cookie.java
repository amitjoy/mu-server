package io.muserver;

import io.netty.handler.codec.http.cookie.DefaultCookie;

import java.util.Set;
import java.util.stream.Collectors;


/**
 * A cookie
 */
public class Cookie {

    final DefaultCookie nettyCookie;

    /**
     * <p>Creates a new cookie with secure settings such as HttpOnly and Secure set to true.</p>
     * @param name The name of the cookie
     * @param value The value of the cookie
     * @return Returns a new cookie that can be sent to the response
     * @deprecated Please use {@link CookieBuilder#newSecureCookie()} instead
     */
    @Deprecated
    public static Cookie secureCookie(String name, String value) {
        return CookieBuilder.newSecureCookie()
            .withName(name)
            .withValue(value)
            .build();
    }

    /**
     * <p>Creates a new cookie with secure settings such as HttpOnly and Secure set to true.</p>
     * @param name The name of the cookie
     * @param value The value of the cookie
     * @deprecated Please use {@link CookieBuilder#newCookie()} instead
     */
    @Deprecated
    public Cookie(String name, String value) {
        nettyCookie = new DefaultCookie(name, value);
    }

    public String name() {
        return nettyCookie.name();
    }

    public String value() {
        return nettyCookie.value();
    }

    /**
     * Sets the value of the cookie.
     * @param value The value to set.
     * @deprecated Please create cookies with the {@link CookieBuilder}
     */
    @Deprecated
    public void setValue(String value) {
        nettyCookie.setValue(value);
    }

    public String domain() {
        return nettyCookie.domain();
    }

    /**
     *
     * @param domain domain
     * @deprecated Please create cookies with the {@link CookieBuilder}
     */
    @Deprecated
    public void setDomain(String domain) {
        nettyCookie.setDomain(domain);
    }

    public String path() {
        return nettyCookie.path();
    }

    /**
     *
     * @param path path
     * @deprecated Please create cookies with the {@link CookieBuilder}
     */
    @Deprecated
    public void setPath(String path) {
        nettyCookie.setPath(path);
    }

    public long maxAge() {
        return nettyCookie.maxAge();
    }

    /**
     *
     * @param maxAgeInSeconds max
     * @deprecated Please create cookies with the {@link CookieBuilder}
     */
    @Deprecated
    public void setMaxAge(long maxAgeInSeconds) {
        nettyCookie.setMaxAge(maxAgeInSeconds);
    }

    public boolean isSecure() {
        return nettyCookie.isSecure();
    }

    /**
     *
     * @param secure secure
     * @deprecated Please create cookies with the {@link CookieBuilder}
     */
    @Deprecated
    public void setSecure(boolean secure) {
        nettyCookie.setSecure(secure);
    }

    /**
     * @return Returns the HTTPOnly value
     */
    public boolean isHttpOnly() {
        return nettyCookie.isHttpOnly();
    }

    /**
     *
     * @param httpOnly httpOnly
     * @deprecated Please create cookies with the {@link CookieBuilder}
     */
    @Deprecated
    public void setHttpOnly(boolean httpOnly) {
        nettyCookie.setHttpOnly(httpOnly);
    }

    public int hashCode() {
        return nettyCookie.hashCode();
    }

    public boolean equals(Object o) {
        return (this == o) || ((o instanceof Cookie) && nettyCookie.equals(o));
    }

    public String toString() {
        return nettyCookie.toString();
    }

    static Set<Cookie> nettyToMu(Set<io.netty.handler.codec.http.cookie.Cookie> originals) {
        return originals.stream().map(n -> new Cookie(n.name(), n.value())).collect(Collectors.toSet());
    }
}

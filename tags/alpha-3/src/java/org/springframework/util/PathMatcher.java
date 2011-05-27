/*
 * Copyright 2002-2007 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.util;

/**
 * Strategy interface for <code>String</code>-based path matching.
 * 
 * <p>Used by {@link org.springframework.core.io.support.PathMatchingResourcePatternResolver}.
 *
 * <p>The default implementation is {@link AntPathMatcher}, supporting the
 * Ant-style pattern syntax.
 *
 * @author Juergen Hoeller
 * @since 1.2
 * @see AntPathMatcher
 */
public interface PathMatcher {

	/**
	 * Does the given <code>path</code> represent a pattern that can be matched
	 * by an implementation of this interface?
	 * <p>If the return value is <code>false</code>, then the {@link #match}
	 * method does not have to be used because direct equality comparisons
	 * on the static path Strings will lead to the same result.
	 * @param path the path String to check
	 * @return <code>true</code> if the given <code>path</code> represents a pattern
	 */
	boolean isPattern(String path);

	/**
	 * Match the given <code>path</code> against the given <code>pattern</code>,
	 * according to this PathMatcher's matching strategy.
	 * @param pattern the pattern to match against
	 * @param path the path String to test
	 * @return <code>true</code> if the supplied <code>path</code> matched,
	 * <code>false</code> if it didn't
	 */
	boolean match(String pattern, String path);

	/**
	 * Match the given <code>path</code> against the corresponding part of the given
	 * <code>pattern</code>, according to this PathMatcher's matching strategy.
	 * <p>Determines whether the pattern at least matches as far as the given base
	 * path goes, assuming that a full path may then match as well.
	 * @param pattern the pattern to match against
	 * @param path the path String to test
	 * @return <code>true</code> if the supplied <code>path</code> matched,
	 * <code>false</code> if it didn't
	 */
	boolean matchStart(String pattern, String path);
}

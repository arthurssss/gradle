/*
 * Copyright 2015 the original author or authors.
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

package org.gradle.play.internal.exceptions;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class DefaultExceptionAdapter implements ExceptionAdapter {
    private final ClassLoader classLoader;

    public DefaultExceptionAdapter(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    public Throwable adapt(String title, String description, Throwable cause) {
        try {
            Class<?> playExceptionClass = classLoader.loadClass("play.api.PlayException");
            Constructor<?> constructor = playExceptionClass.getConstructor(String.class, String.class, Throwable.class);
            return (Throwable) constructor.newInstance(title, description, cause);
        } catch (ClassNotFoundException e) {
            /* fall through */
        } catch (InvocationTargetException e) {
            /* fall through */
        } catch (NoSuchMethodException e) {
            /* fall through */
        } catch (InstantiationException e) {
            /* fall through */
        } catch (IllegalAccessException e) {
            /* fall through */
        }
        // cannot convert for some reason
        return cause;
    }
}

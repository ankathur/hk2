/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2011 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * http://glassfish.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */
package org.glassfish.hk2.tests.basic.scopes;

import org.glassfish.hk2.Provider;
import org.glassfish.hk2.Scope;
import org.glassfish.hk2.ScopeInstance;

import java.util.HashMap;
import java.util.Map;

public class CustomScope implements Scope {
    public static final String OUT_OF_SCOPE_MESSAGE = "Out of scope";
    private volatile ScopeInstance current = null;

    private ScopeInstance getScopeInstance() {
        return new ScopeInstance() {
            /**
             * A map of injectable instances in this scope
             */
            private final Map<Provider<?>, Object> store = new HashMap<Provider<?>, Object>();

            @Override
            @SuppressWarnings("unchecked")
            public <T> T get(Provider<T> inhabitant) {
                return (T) store.get(inhabitant);
            }

            @Override
            public <T> boolean contains(Provider<T> provider) {
                return store.containsKey(provider);
            }

            @Override
            @SuppressWarnings("unchecked")
            public <T> T put(Provider<T> inhabitant, T value) {
                return (T) store.put(inhabitant, value);
            }

            @Override
            public void release() {
                // TODO any other clean up?
                store.clear();
            }
        };
    }

    @Override
    public ScopeInstance current() {
        if (current != null) {
            return current;
        } else {
            throw new IllegalStateException(OUT_OF_SCOPE_MESSAGE);
        }
    }

    public void enter() {
        current = getScopeInstance();
    }

    public void leave() {
        current = null;
    }
}

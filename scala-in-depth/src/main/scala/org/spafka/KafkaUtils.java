/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.spafka;

import java.util.Collections;
import java.util.List;

public class KafkaUtils {


    /**
     * Returns an empty collection if this list is null
     * @param other
     * @return
     */
    public static <T> List<T> safe(List<T> other) {
        return other == null ? Collections.<T>emptyList() : other;
    }

    /**
     * Get the ClassLoader which loaded Kafka.
     */
    public static ClassLoader getKafkaClassLoader() {
        return KafkaUtils.class.getClassLoader();
    }

    /**
     * Get the Context ClassLoader on this thread or, if not present, the ClassLoader that
     * loaded Kafka.
     *
     * This should be used whenever passing a ClassLoader to Class.forName
     */
    public static ClassLoader getContextOrKafkaClassLoader() {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        if (cl == null) {
            return getKafkaClassLoader();
        }
        else {
            return cl;
        }
    }

}

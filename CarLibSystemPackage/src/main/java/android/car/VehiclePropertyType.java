/*
 * Copyright (C) 2018 The Android Open Source Project
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
package android.car;

import androidx.annotation.IntDef;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Value type of VehicleProperty.
 *
 * <p>Source: <a
 * href="https://android.googlesource.com/platform/packages/services/Car/+/refs/heads/android16-release/car-lib/src/android/car/VehiclePropertyType.java">VehiclePropertyType</a>
 *
 * @implNote The constants are simplified for LLMs, and unsupported types like BYTES and MIXED are
 *     removed.
 */
public class VehiclePropertyType {
  public static final int STRING = 1;
  public static final int BOOLEAN = 2;
  public static final int INT32 = 3;
  public static final int INT32_VEC = 4;
  public static final int INT64 = 5;
  public static final int INT64_VEC = 6;
  public static final int FLOAT = 7;
  public static final int FLOAT_VEC = 8;

  @Retention(RetentionPolicy.SOURCE)
  @IntDef({STRING, BOOLEAN, INT32, INT32_VEC, INT64, INT64_VEC, FLOAT, FLOAT_VEC})
  public @interface Enum {}

  private VehiclePropertyType() {}
}

/*
 * Copyright The OpenTelemetry Authors
 * SPDX-License-Identifier: Apache-2.0
 */

package io.opentelemetry.javaagent.tooling;

import io.opentelemetry.javaagent.bootstrap.AgentInitializer;
import io.opentelemetry.javaagent.bootstrap.OpenTelemetrySdkAccess;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.autoconfigure.AutoConfiguredOpenTelemetrySdk;
import io.opentelemetry.sdk.autoconfigure.AutoConfiguredOpenTelemetrySdkBuilder;
import io.opentelemetry.sdk.common.CompletableResultCode;
import java.util.Arrays;

public final class OpenTelemetryInstaller {

  /**
   * Install the {@link OpenTelemetrySdk} using autoconfigure, and return the {@link
   * AutoConfiguredOpenTelemetrySdk}.
   *
   * @return the {@link AutoConfiguredOpenTelemetrySdk}
   */
  public static AutoConfiguredOpenTelemetrySdk installOpenTelemetrySdk() {
    AutoConfiguredOpenTelemetrySdkBuilder builder =
        AutoConfiguredOpenTelemetrySdk.builder().setResultAsGlobal(true);

    ClassLoader classLoader = AgentInitializer.getExtensionsClassLoader();
    if (classLoader != null) {
      // May be null in unit tests.
      builder.setServiceClassLoader(classLoader);
    }

    AutoConfiguredOpenTelemetrySdk autoConfiguredSdk = builder.build();
    OpenTelemetrySdk sdk = autoConfiguredSdk.getOpenTelemetrySdk();

    OpenTelemetrySdkAccess.internalSetForceFlush(
        (timeout, unit) -> {
          CompletableResultCode traceResult = sdk.getSdkTracerProvider().forceFlush();
          CompletableResultCode metricsResult = sdk.getSdkMeterProvider().forceFlush();
          CompletableResultCode logsResult = sdk.getSdkLoggerProvider().forceFlush();
          CompletableResultCode.ofAll(Arrays.asList(traceResult, metricsResult, logsResult))
              .join(timeout, unit);
        });

    return autoConfiguredSdk;
  }

  private OpenTelemetryInstaller() {}
}

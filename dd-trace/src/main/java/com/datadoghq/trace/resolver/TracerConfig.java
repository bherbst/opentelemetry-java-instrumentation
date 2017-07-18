package com.datadoghq.trace.resolver;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.util.List;
import java.util.Map;

/** Tracer configuration */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TracerConfig {

  private String defaultServiceName;
  private WriterConfig writer;
  private SamplerConfig sampler;
  private List<DDSpanDecoratorConfig> decorators;

  public String getDefaultServiceName() {
    return defaultServiceName;
  }

  public void setDefaultServiceName(final String defaultServiceName) {
    this.defaultServiceName = defaultServiceName;
  }

  public WriterConfig getWriter() {
    return writer;
  }

  public void setWriter(final WriterConfig writer) {
    this.writer = writer;
  }

  public SamplerConfig getSampler() {
    return sampler;
  }

  public void setSampler(final SamplerConfig sampler) {
    this.sampler = sampler;
  }

  public List<DDSpanDecoratorConfig> getDecorators() {
    return decorators;
  }

  public void setDecorators(final List<DDSpanDecoratorConfig> decorators) {
    this.decorators = decorators;
  }

  @Override
  public String toString() {
    try {
      return new ObjectMapper(new YAMLFactory()).writeValueAsString(this);
    } catch (final JsonProcessingException e) {
      //FIXME better toString() while config object stabilized
      return null;
    }
  }
}

class SamplerConfig {

  private Double rate;
  private String type;
  private Map<String, String> skipTagsPatterns;

  public String getType() {
    return type;
  }

  public Double getRate() {
    return rate;
  }

  public void setRate(final Double rate) {
    this.rate = rate;
  }

  public void setType(final String type) {
    this.type = type;
  }

  public Map<String, String> getSkipTagsPatterns() {
    return skipTagsPatterns;
  }
}

class WriterConfig {

  private String host;
  private Integer port;
  private String type;

  public void setHost(final String host) {
    this.host = host;
  }

  public void setPort(final Integer port) {
    this.port = port;
  }

  public void setType(final String type) {
    this.type = type;
  }

  public String getHost() {
    return host;
  }

  public Integer getPort() {
    return port;
  }

  public String getType() {
    return type;
  }

  public String getHost(final String defaultHostname) {
    return host == null ? defaultHostname : host;
  }

  public Integer getPort(final int defaultPort) {
    return port == null ? defaultPort : port;
  }
}

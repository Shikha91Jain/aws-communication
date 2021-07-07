package com.aws.communication.model;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Response body for the error scenarios
 */
@ApiModel(description = "Response body for the error scenarios")
@Validated

public class ErrorResponse   {
  @JsonProperty("code")
  private String code = null;

  @JsonProperty("reason")
  private String reason = null;

  public ErrorResponse code(String code) {
    this.code = code;
    return this;
  }

  /**
   * Indicates the application related status code.
   * @return code
  **/
  @ApiModelProperty(value = "Indicates the application related status code.")


  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public ErrorResponse reason(String reason) {
    this.reason = reason;
    return this;
  }

  /**
   * Reason provides more information on the error code being returned
   * @return reason
  **/
  @ApiModelProperty(value = "Reason provides more information on the error code being returned")


  public String getReason() {
    return reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }
}


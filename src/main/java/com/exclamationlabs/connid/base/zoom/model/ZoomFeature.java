package com.exclamationlabs.connid.base.zoom.model;

import com.google.gson.annotations.SerializedName;

public class ZoomFeature {
  @SerializedName("zoom_one_type")
  private Long zoomOneType;

  @SerializedName("zoom_phone")
  private Boolean zoomPhone;

  public Long getZoomOneType() {
    return zoomOneType;
  }

  public Boolean getZoomPhone() {
    return zoomPhone;
  }

  public void setZoomOneType(Long zoomOneType) {
    this.zoomOneType = zoomOneType;
  }

  public void setZoomPhone(Boolean zoomPhone) {
    this.zoomPhone = zoomPhone;
  }
}

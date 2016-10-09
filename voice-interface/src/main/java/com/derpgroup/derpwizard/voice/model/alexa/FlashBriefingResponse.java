package com.derpgroup.derpwizard.voice.model.alexa;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class FlashBriefingResponse {
  
  public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'.0Z'");
  
  private String uid;
  private String titleText;
  private String mainText;
  @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss'.0Z'", timezone="UTC")
  private Date updateDate;
  private String redirectionUrl;
  
  public String getUid() {
    return uid;
  }
  
  public void setUid(String uid) {
    this.uid = uid;
  }

  public String getTitleText() {
    return titleText;
  }

  public void setTitleText(String titleText) {
    this.titleText = titleText;
  }

  public String getMainText() {
    return mainText;
  }

  public void setMainText(String mainText) {
    this.mainText = mainText;
  }

  public Date getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(Date updateDate) {
    this.updateDate = updateDate;
  }

  public String getRedirectionUrl() {
    return redirectionUrl;
  }

  public void setRedirectionUrl(String redirectionUrl) {
    this.redirectionUrl = redirectionUrl;
  }

  @Override
  public String toString() {
    return "FlashBriefingResponse [uid=" + uid + ", titleText=" + titleText
        + ", mainText=" + mainText + ", updateDate=" + updateDate
        + ", redirectionUrl=" + redirectionUrl + "]";
  }
}

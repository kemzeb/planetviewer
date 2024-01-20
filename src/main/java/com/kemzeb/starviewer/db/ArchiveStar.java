package com.kemzeb.starviewer.db;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
  "star_name",
  "hip_name",
  "hd_name",
  "gj_name",
  "tm_name",
  "st_exocatflag",
  "st_coronagflag",
  "st_starshadeflag",
  "st_wfirstflag",
  "st_lbtiflag",
  "st_rvflag",
  "st_ppnum",
  "rastr",
  "decstr",
  "st_dist",
  "st_disterr1",
  "st_disterr2",
  "st_vmag",
  "st_vmagerr",
  "st_vmagsrc",
  "st_bmv",
  "st_bmverr",
  "st_bmvsrc",
  "st_spttype",
  "st_lbol",
  "st_lbolerr",
  "st_lbolsrc"
})
@Generated("jsonschema2pojo")
public class ArchiveStar {

  @JsonProperty("star_name")
  public String starName;

  @JsonProperty("hip_name")
  public String hipName;

  @JsonProperty("hd_name")
  public String hdName;

  @JsonProperty("gj_name")
  public String gjName;

  @JsonProperty("tm_name")
  public String tmName;

  @JsonProperty("st_exocatflag")
  public Integer stExocatflag;

  @JsonProperty("st_coronagflag")
  public Integer stCoronagflag;

  @JsonProperty("st_starshadeflag")
  public Integer stStarshadeflag;

  @JsonProperty("st_wfirstflag")
  public Integer stWfirstflag;

  @JsonProperty("st_lbtiflag")
  public Integer stLbtiflag;

  @JsonProperty("st_rvflag")
  public Integer stRvflag;

  @JsonProperty("st_ppnum")
  public Integer stPpnum;

  @JsonProperty("rastr")
  public String rastr;

  @JsonProperty("decstr")
  public String decstr;

  @JsonProperty("st_dist")
  public Double stDist;

  @JsonProperty("st_disterr1")
  public Double stDisterr1;

  @JsonProperty("st_disterr2")
  public Double stDisterr2;

  @JsonProperty("st_vmag")
  public Double stVmag;

  @JsonProperty("st_vmagerr")
  public Double stVmagerr;

  @JsonProperty("st_vmagsrc")
  public String stVmagsrc;

  @JsonProperty("st_bmv")
  public Double stBmv;

  @JsonProperty("st_bmverr")
  public Double stBmverr;

  @JsonProperty("st_bmvsrc")
  public String stBmvsrc;

  @JsonProperty("st_spttype")
  public String stSpttype;

  @JsonProperty("st_lbol")
  public Double stLbol;

  @JsonProperty("st_lbolerr")
  public Double stLbolerr;

  @JsonProperty("st_lbolsrc")
  public String stLbolsrc;
}

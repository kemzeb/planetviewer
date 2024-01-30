package com.kemzeb.starviewer.db;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.annotation.Generated;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.Data;

/**
 * Represents a single row in the PS table in the NASA Exoplanet Archive.
 *
 * <p>Class generated by jsonschema2pojo.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
  "default_flag",
  "pl_name",
  "hostname",
  "sy_snum",
  "sy_pnum",
  "sy_mnum",
  "discoverymethod",
  "disc_year",
  "disc_facility",
  "pl_orbper",
  "pl_rade",
  "pl_bmasse",
  "pl_eqt",
  "st_spectype",
  "st_teff",
  "st_rad",
  "st_mass",
  "st_age",
  "sy_dist",
  "glat",
  "glon"
})
@Generated("jsonschema2pojo")
@Data
public class PsArchive {

  @JsonProperty("default_flag")
  public Boolean defaultFlag;

  @JsonProperty("pl_name")
  public String plName;

  @JsonProperty("hostname")
  public String stellarHost;

  @JsonProperty("sy_snum")
  public Integer sySnum;

  @JsonProperty("sy_pnum")
  public Integer syPnum;

  @JsonProperty("sy_mnum")
  public Integer syMnum;

  @JsonProperty("discoverymethod")
  public String discoverymethod;

  @JsonProperty("disc_year")
  public Integer discYear;

  @JsonProperty("disc_facility")
  public String discFacility;

  @JsonProperty("pl_orbper")
  public Double plOrbper;

  @JsonProperty("pl_rade")
  public Double plRade;

  @JsonProperty("pl_bmasse")
  public Double plBmasse;

  @JsonProperty("pl_eqt")
  public Double plEqt;

  @JsonProperty("st_spectype")
  public String stSpectype;

  @JsonProperty("st_teff")
  public Double stTeff;

  @JsonProperty("st_rad")
  public Double stRad;

  @JsonProperty("st_mass")
  public Double stMass;

  @JsonProperty("st_age")
  public Double stAge;

  @JsonProperty("sy_dist")
  public Double syDist;

  @JsonProperty("glat")
  public Double glat;

  @JsonProperty("glon")
  public Double glon;

  @JsonIgnore
  private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

  @JsonAnyGetter
  public Map<String, Object> getAdditionalProperties() {
    return this.additionalProperties;
  }

  @JsonAnySetter
  public void setAdditionalProperty(String name, Object value) {
    this.additionalProperties.put(name, value);
  }
}

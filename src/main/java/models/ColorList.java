package models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record ColorList(List<Colors> colorList) {}

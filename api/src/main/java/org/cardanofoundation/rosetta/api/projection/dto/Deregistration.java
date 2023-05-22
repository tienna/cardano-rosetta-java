package org.cardanofoundation.rosetta.api.projection.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Deregistration {

  private String stakeAddress;
  private String amount;
}

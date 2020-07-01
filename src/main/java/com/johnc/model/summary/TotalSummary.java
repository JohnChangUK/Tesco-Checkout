package com.johnc.model.summary;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class TotalSummary {

    private List<SavingsSummary> savingsSummaries;
}

package com.episen.ing3.fise.springbootlockauthentification.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class PageData {
    private int page;
    private int nbElements;

}

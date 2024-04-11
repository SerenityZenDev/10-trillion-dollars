package org.example.tentrilliondollars.kakaopay.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.util.LinkedMultiValueMap;

@Getter
@AllArgsConstructor
public class PayRequestDto {
    private String url;
    private LinkedMultiValueMap<String,String> map;
    private Long orderId;
}

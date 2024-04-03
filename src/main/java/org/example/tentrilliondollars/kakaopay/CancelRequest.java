package org.example.tentrilliondollars.kakaopay;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.util.LinkedMultiValueMap;
@Getter
@AllArgsConstructor
public class CancelRequest {
    private String url;
    private LinkedMultiValueMap<String,String> map;
}

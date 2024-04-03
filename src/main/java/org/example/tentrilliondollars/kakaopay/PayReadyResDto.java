package org.example.tentrilliondollars.kakaopay;

import lombok.Getter;

@Getter
public class PayReadyResDto {
    private String tid;
    private String next_redirect_pc_url;
    private String created_at;

}

package org.example.tentrilliondollars.kakaopay.controller;

import lombok.RequiredArgsConstructor;
import org.example.tentrilliondollars.kakaopay.CancelResDto;
import org.example.tentrilliondollars.kakaopay.KakaoPayService;
import org.example.tentrilliondollars.kakaopay.PayApproveResDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payment")
public class KakaoPayController {

    private final KakaoPayService kakaoPayService;

    @GetMapping("/ready/{orderId}")
    public ResponseEntity<?> getRedirectUrl(@PathVariable Long orderId) throws Exception {

           return ResponseEntity.status(HttpStatus.OK)
                    .body(kakaoPayService.getRedirectUrl(orderId));


    }

    @GetMapping("/success/{orderId}")
    public ResponseEntity<?> afterGetRedirectUrl(@PathVariable Long orderId,
                                                 @RequestParam("pg_token") String pgToken) throws Exception {

            PayApproveResDto kakaoApprove = kakaoPayService.getApprove(pgToken,orderId);

            return ResponseEntity.status(HttpStatus.OK)
                    .body(kakaoApprove);


    }
    @GetMapping("/cancel/{orderId}")
    public ResponseEntity<?> cancel(@PathVariable Long orderId) throws Exception {
        CancelResDto cancelResDto = kakaoPayService.kakaoCancel(orderId);
         return ResponseEntity.status(HttpStatus.OK)
                 .body(cancelResDto);
    }
    @GetMapping("/fail")
    public ResponseEntity<?> fail() {

        return null;

    }

}


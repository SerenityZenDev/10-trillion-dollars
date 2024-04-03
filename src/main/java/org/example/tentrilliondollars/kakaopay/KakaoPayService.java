package org.example.tentrilliondollars.kakaopay;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.tentrilliondollars.order.entity.OrderDetail;
import org.example.tentrilliondollars.order.entity.OrderState;
import org.example.tentrilliondollars.order.repository.OrderDetailRepository;
import org.example.tentrilliondollars.order.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class KakaoPayService {
    private final MakeRequest makeRequest;
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;

    @Value("${kakao.api.admin-key}")
    private String adminKey;



    @Transactional
    public PayReadyResDto getRedirectUrl(Long orderId)throws Exception{

        HttpHeaders headers=new HttpHeaders();
        String auth = "KakaoAK " + adminKey;
        headers.set("Content-type","application/x-www-form-urlencoded;charset=utf-8");
        headers.set("Authorization",auth);
        PayRequest payRequest= makeRequest.getReadyRequest(createPayInfo(orderId));
        HttpEntity<MultiValueMap<String, String>> urlRequest = new HttpEntity<>(payRequest.getMap(), headers);
        RestTemplate rt = new RestTemplate();
        PayReadyResDto payReadyResDto = rt.postForObject(payRequest.getUrl(), urlRequest, PayReadyResDto.class);
        orderRepository.getReferenceById(orderId).updateTid(payReadyResDto.getTid());
        return payReadyResDto;
    }

    @Transactional
    public PayApproveResDto getApprove(String pgToken, Long orderId)throws Exception{

        String tid= orderRepository.getReferenceById(orderId).getKakaoTid();

        HttpHeaders headers=new HttpHeaders();
        String auth = "KakaoAK " + adminKey;
        headers.set("Content-type","application/x-www-form-urlencoded;charset=utf-8");
        headers.set("Authorization",auth);

        PayRequest payRequest= makeRequest.getApproveRequest(tid,pgToken);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(payRequest.getMap(), headers);

        RestTemplate rt = new RestTemplate();
        PayApproveResDto payApproveResDto = rt.postForObject(payRequest.getUrl(), requestEntity, PayApproveResDto.class);
        orderRepository.getReferenceById(orderId).changeState(OrderState.PREPARING);
        return payApproveResDto;
    }
    public CancelResDto kakaoCancel(Long orderId){

        String tid= orderRepository.getReferenceById(orderId).getKakaoTid();

        HttpHeaders headers=new HttpHeaders();
        String auth = "KakaoAK " + adminKey;
        headers.set("Content-type","application/x-www-form-urlencoded;charset=utf-8");
        headers.set("Authorization",auth);

        CancelRequest cancelRequest = makeRequest.getCancelRequest(tid);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(cancelRequest.getMap(), headers);
        RestTemplate rt = new RestTemplate();
         CancelResDto cancelResDto = rt.postForObject(cancelRequest.getUrl(),requestEntity,CancelResDto.class);
         return cancelResDto;
    }

    public PayInfoDto createPayInfo(Long orderId){
        List<OrderDetail> ListofOrderDetail = orderDetailRepository.findOrderDetailsByOrder(orderRepository.getReferenceById(orderId));
        Long totalPrice=0L;
        for(OrderDetail orderDetail:ListofOrderDetail){
            totalPrice+=orderDetail.getPrice()*orderDetail.getQuantity();
        }
        PayInfoDto payInfoDto = new PayInfoDto();
        payInfoDto.setPrice(totalPrice);
        payInfoDto.setItemName("TenCompany");
        return payInfoDto;
    }




}
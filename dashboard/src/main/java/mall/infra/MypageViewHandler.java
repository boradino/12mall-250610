package mall.infra;

import mall.domain.*;
import mall.config.kafka.KafkaProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class MypageViewHandler {

//<<< DDD / CQRS
    @Autowired
    private MypageRepository mypageRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whenOrderPlaced_then_CREATE_1 (@Payload OrderPlaced orderPlaced) {
        try {

            if (!orderPlaced.validate()) return;

            // view 객체 생성
            Mypage mypage = new Mypage();
            // view 객체에 이벤트의 Value 를 set 함
            mypage.setOrderId(orderPlaced.getId());
            mypage.setCustomerId(orderPlaced.getCustomerId());
            mypage.setItemId(orderPlaced.getItemId());
            mypage.setQty(orderPlaced.getQty());
            mypage.setAddress(orderPlaced.getAddress());
            mypage.setOrderStatus(주문 완료);
            // view 레파지 토리에 save
            mypageRepository.save(mypage);

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @StreamListener(KafkaProcessor.INPUT)
    public void whenDeliveryCompleted_then_UPDATE_1(@Payload DeliveryCompleted deliveryCompleted) {
        try {
            if (!deliveryCompleted.validate()) return;
                // view 객체 조회
            Optional<Mypage> mypageOptional = mypageRepository.findByOrderId(Long.valueOf(deliveryCompleted.getOrderId()));

            if( mypageOptional.isPresent()) {
                 Mypage mypage = mypageOptional.get();
            // view 객체에 이벤트의 eventDirectValue 를 set 함
                mypage.setDeliveryStatus(배송 완료);    
                // view 레파지 토리에 save
                 mypageRepository.save(mypage);
                }


        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @StreamListener(KafkaProcessor.INPUT)
    public void whenOrderCancelled_then_UPDATE_2(@Payload OrderCancelled orderCancelled) {
        try {
            if (!orderCancelled.validate()) return;
                // view 객체 조회
            Optional<Mypage> mypageOptional = mypageRepository.findByOrderId(orderCancelled.getId());

            if( mypageOptional.isPresent()) {
                 Mypage mypage = mypageOptional.get();
            // view 객체에 이벤트의 eventDirectValue 를 set 함
                mypage.setOrderStatus(주문 취소);    
                // view 레파지 토리에 save
                 mypageRepository.save(mypage);
                }


        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @StreamListener(KafkaProcessor.INPUT)
    public void whenDeliveryReturned_then_UPDATE_3(@Payload DeliveryReturned deliveryReturned) {
        try {
            if (!deliveryReturned.validate()) return;
                // view 객체 조회
            Optional<Mypage> mypageOptional = mypageRepository.findByOrderId(Long.valueOf(deliveryReturned.getOrderId()));

            if( mypageOptional.isPresent()) {
                 Mypage mypage = mypageOptional.get();
            // view 객체에 이벤트의 eventDirectValue 를 set 함
                mypage.setDeliveryStatus(배송 반품됨);    
                // view 레파지 토리에 save
                 mypageRepository.save(mypage);
                }


        }catch (Exception e){
            e.printStackTrace();
        }
    }


//>>> DDD / CQRS
}


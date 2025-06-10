package mall.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.*;
import lombok.Data;
import mall.DeliveryApplication;
import mall.domain.DeliveryCompleted;
import mall.domain.DeliveryReturned;

@Entity
@Table(name = "Delivery_table")
@Data
//<<< DDD / Aggregate Root
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long orderId;

    private String customerId;

    private String itemId;

    private Integer qty;

    private String address;

    private String status;

    public static DeliveryRepository repository() {
        DeliveryRepository deliveryRepository = DeliveryApplication.applicationContext.getBean(
            DeliveryRepository.class
        );
        return deliveryRepository;
    }

    //<<< Clean Arch / Port Method
    public static void startDelivery(OrderPlaced orderPlaced) {
        //implement business logic here:

        Delivery delivery = new Delivery();
        delivery.setOrderId(orderPlaced.getId());
        delivery.setAddress(orderPlaced.getAddress());
        delivery.setCustomerId(orderPlaced.getCustomerId());
        delivery.setItemId(orderPlaced.getItemId());
        delivery.setQty(orderPlaced.getQty());
        delivery.setStatus("DELIVERY COMPLETED");
        repository().save(delivery);

        DeliveryCompleted deliveryCompleted = new DeliveryCompleted(delivery);
        deliveryCompleted.publishAfterCommit();
        
        /** 
        repository().findById(orderPlaced.getId()).ifPresent(delivery->{
            
            delivery.setId(orderPlaced.getId());
            repository().save(delivery);

            DeliveryCompleted deliveryCompleted = new DeliveryCompleted(delivery);
            deliveryCompleted.publishAfterCommit();

         });
         */

    }
    //>>> Clean Arch / Port Method

}
//>>> DDD / Aggregate Root

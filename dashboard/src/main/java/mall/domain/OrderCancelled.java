package mall.domain;

import java.time.LocalDate;
import java.util.*;
import lombok.Data;
import mall.infra.AbstractEvent;

@Data
public class OrderCancelled extends AbstractEvent {

    private Long id;
    private String customerId;
    private String itemId;
    private Integer qty;
    private String address;
}

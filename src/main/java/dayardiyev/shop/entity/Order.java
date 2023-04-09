package dayardiyev.shop.entity;


import dayardiyev.shop.entity.enumiration.Status;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private Status status;

    private String address;

    private LocalDateTime created_at;

    @OneToMany(mappedBy = "order")
    private List<OrderProduct> orderProducts;
}

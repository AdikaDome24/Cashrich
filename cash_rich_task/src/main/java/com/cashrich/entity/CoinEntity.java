package com.cashrich.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="coin")
public class CoinEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "coin_seq")
    @SequenceGenerator(name = "coin_seq", sequenceName = "coin_seq", allocationSize = 1)
    private Long id;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String requestUrl;

    @Column(columnDefinition = "TEXT")
    private String requestPayload;

    @Column(columnDefinition = "TEXT")
    private String responsePayload;

    @Column(nullable = false)
    private LocalDateTime timestamp;
}

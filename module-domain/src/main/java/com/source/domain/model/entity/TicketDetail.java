package com.source.domain.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ticket_detail")
public class TicketDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "stock_initial")
    private int stockInitial;

    @Column(name = "stock_available")
    private int stockAvailable;

    @Column(name = "is_stock_prepared")
    private boolean isStockPrepared;

    @Column(name = "price_original")
    private Long priceOriginal;

    @Column(name = "price_flash")
    private Long priceFlash;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "sale_start_time")
    private Date saleStartTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "sale_end_time")
    private Date saleEndTime;

    @Column(name = "status")
    private int status;

    @Column(name = "activity_id")
    private Long activityId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;
}

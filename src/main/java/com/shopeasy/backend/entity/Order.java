package com.shopeasy.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Each cart line becomes its own Order row at checkout time.
 * Product name / image / price are snapshotted so history never changes
 * if the catalog is later updated.
 */
@Entity
@Table(name = "orders", schema = "public")
@Getter @Setter @NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    /** Nullable FK – set to NULL if the product is later deleted from the catalog. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "product_image", nullable = false)
    private String productImage;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    private Integer qty;

    @Column(nullable = false)
    private Boolean premium = false;

    @Column(nullable = false)
    private String status = "Processing";

    /**
     * Stored as a JSONB array in Postgres, e.g.
     * ["Warehouse","Hub","Your City","Delivery"]
     */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false, columnDefinition = "jsonb")
    private List<String> route;

    @Column(name = "route_step", nullable = false)
    private Integer routeStep = 0;

    @Column(name = "order_date", nullable = false)
    private LocalDate orderDate;

    @Column(name = "created_at", updatable = false)
    private OffsetDateTime createdAt;

    @PrePersist
    private void prePersist() {
        if (createdAt == null) createdAt = OffsetDateTime.now();
        if (orderDate == null) orderDate = LocalDate.now();
        if (routeStep == null) routeStep = 0;
        if (status == null) status = "Processing";
    }
}

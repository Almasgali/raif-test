package ru.almasgali.raif_test.data.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "accounts", schema = "public")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(name = "Account ID.", example = "1")
    private long id;
    @Column(nullable = false)
    @Schema(name = "Account balance.", example = "123.45")
    private double balance;
    @Column(nullable = false)
    @Schema(name = "Account currency.", example = "RUB")
    private String currency;
}

package com.liveguard.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "currencies")
@NoArgsConstructor
@Getter
@Setter
public class Currency extends BaseEntity {

    @Column(nullable = false, length = 64)
    private String name;

    @Column(nullable = false, length = 3)
    private String symbol;

    @Column(nullable = false, length = 4)
    private String code;

    public Currency(String name, String symbol, String code) {
        super();
        this.name = name;
        this.symbol = symbol;
        this.code = code;
    }

    @Override
    public String toString() {
        return name + " - " + code + " - " + symbol;
    }
}

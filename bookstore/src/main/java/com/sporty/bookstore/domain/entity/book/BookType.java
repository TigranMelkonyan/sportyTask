package com.sporty.bookstore.domain.entity.book;

import lombok.Getter;

/**
 * Created by Tigran Melkonyan
 * Date: 4/16/25
 * Time: 9:13 PM
 */
@Getter
public enum BookType {
    
    NEW_RELEASE(1.0, 0.0),
    REGULAR(1.0, 0.10),
    OLD_EDITION(0.80, 0.05);

    private final double priceMultiplier;
    private final double bundleDiscount;

    BookType(double priceMultiplier, double bundleDiscount) {
        this.priceMultiplier = priceMultiplier;
        this.bundleDiscount = bundleDiscount;
    }

    public double calculateFinalPrice(double basePrice, int bundleSize) {
        double priceAfterBase = basePrice * priceMultiplier;
        double discount = priceAfterBase * (bundleSize >= 3 ? bundleDiscount : 0.0);
        return priceAfterBase - discount;
    }
}

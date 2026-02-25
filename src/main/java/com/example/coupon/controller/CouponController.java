package com.example.coupon.controller;

import com.example.coupon.application.dto.CreateCouponRequest;
import com.example.coupon.application.dto.CouponResponse;
import com.example.coupon.application.usecase.CreateCouponUseCase;
import com.example.coupon.application.usecase.DeleteCouponUseCase;
import com.example.coupon.application.usecase.ListCouponsUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/coupons")
@Tag(name = "Coupons", description = "Coupon management endpoints")
public class CouponController {

    private final CreateCouponUseCase createCouponUseCase;
    private final DeleteCouponUseCase deleteCouponUseCase;
    private final ListCouponsUseCase listCouponsUseCase;

    public CouponController(CreateCouponUseCase createCouponUseCase,
                            DeleteCouponUseCase deleteCouponUseCase,
                            ListCouponsUseCase listCouponsUseCase) {
        this.createCouponUseCase = createCouponUseCase;
        this.deleteCouponUseCase = deleteCouponUseCase;
        this.listCouponsUseCase = listCouponsUseCase;
    }

    @GetMapping
    @Operation(summary = "List all coupons", description = "Returns a list of all coupons")
    @ApiResponse(responseCode = "200", description = "List of coupons retrieved successfully")
    public ResponseEntity<List<CouponResponse>> listAll() {
        List<CouponResponse> coupons = listCouponsUseCase.execute();
        return ResponseEntity.ok(coupons);
    }

    @PostMapping
    @Operation(summary = "Create a new coupon", description = "Creates a coupon with business rules validation")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Coupon created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<CouponResponse> create(
            @Valid @RequestBody CreateCouponRequest request) {
        CouponResponse response = createCouponUseCase.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a coupon", description = "Performs soft delete on a coupon")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Coupon deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Coupon not found"),
        @ApiResponse(responseCode = "409", description = "Coupon already deleted")
    })
    public ResponseEntity<CouponResponse> delete(
            @Parameter(description = "Coupon ID", required = true)
            @PathVariable UUID id) {
        Optional<CouponResponse> response = deleteCouponUseCase.execute(id);
        return response
            .map(coupon -> ResponseEntity.ok().body(coupon))
            .orElseGet(() -> ResponseEntity.notFound().build());
    }
}

package com.ridenorth.module.payment

import com.ridenorth.common.dto.*
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/payments")
class PaymentController(private val paymentService: PaymentService) {

    @GetMapping("/wallet")
    @PreAuthorize("hasAnyRole('RIDER', 'DRIVER')")
    fun getMyWallet(): ResponseEntity<WalletDto> {
        val wallet = paymentService.getMyWallet()
        return ResponseEntity.ok(
            WalletDto(
                id = wallet.id.toString(),
                userId = wallet.user?.id.toString(),
                balance = wallet.balance.toDouble(),
                pendingBalance = wallet.pendingBalance.toDouble(),
                currency = wallet.currency
            )
        )
    }

    @GetMapping("/history")
    @PreAuthorize("hasAnyRole('RIDER', 'DRIVER')")
    fun getMyPayments(): ResponseEntity<List<PaymentDto>> {
        val payments = paymentService.getMyPayments()
        return ResponseEntity.ok(payments.map { toDto(it) })
    }

    @PostMapping("/mtn/initiate")
    @PreAuthorize("hasAnyRole('RIDER', 'DRIVER')")
    fun initiateMtnPayment(@Valid @RequestBody request: InitiatePaymentDto): ResponseEntity<PaymentDto> {
        val payment = paymentService.initiateMtnMomoPayment(
            org.springframework.security.core.context.SecurityContextHolder.getContext().authentication.name,
            request.amount
        )
        return ResponseEntity.ok(toDto(payment))
    }

    private fun toDto(payment: Payment): PaymentDto {
        return PaymentDto(
            id = payment.id.toString(),
            payerId = payment.payerId.toString(),
            payeeId = payment.payeeId.toString(),
            tripId = payment.tripId?.toString(),
            freightJobId = payment.freightJobId?.toString(),
            amount = payment.amount,
            commissionAmount = payment.commissionAmount,
            netAmount = payment.netAmount,
            paymentMethod = payment.paymentMethod,
            status = payment.status,
            providerTransactionId = payment.providerTransactionId,
            createdAt = payment.createdAt!!
        )
    }
}

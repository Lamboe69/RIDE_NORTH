package com.ridenorth.module.payment

import com.ridenorth.module.booking.Trip
import com.ridenorth.module.booking.TripRepository
import com.ridenorth.module.booking.TripStatus
import com.ridenorth.module.user.User
import com.ridenorth.module.user.UserRepository
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class PaymentService(
    private val paymentRepository: PaymentRepository,
    private val walletRepository: WalletRepository,
    private val tripRepository: TripRepository,
    private val userRepository: UserRepository
) {

    @Transactional
    fun createPaymentForTrip(trip: Trip): Payment {
        val rider = trip.rider!!
        val driver = trip.driver!!

        val payment = Payment(
            payerId = rider.id!!,
            payeeId = driver.user!!.id!!,
            tripId = trip.id!!,
            amount = trip.finalFare,
            commissionAmount = trip.commissionAmount,
            netAmount = trip.driverEarnings,
            paymentMethod = com.ridenorth.module.payment.PaymentMethod.valueOf(trip.paymentMethod),
            status = com.ridenorth.module.payment.PaymentStatus.PENDING,
            createdAt = java.time.Instant.now()
        )

        val saved = paymentRepository.save(payment)

        rider.id?.let {
            val riderWallet = walletRepository.findByUserId(it).orElseGet {
                val w = Wallet(user = rider)
                walletRepository.save(w)
            }
            riderWallet.balance = riderWallet.balance.subtract(java.math.BigDecimal.valueOf(trip.finalFare))
            walletRepository.save(riderWallet)
        }

        val driverWallet = walletRepository.findByUserId(driver.user!!.id!!).orElseGet {
            val w = Wallet(user = driver.user)
            walletRepository.save(w)
        }
        driverWallet.pendingBalance = driverWallet.pendingBalance.add(java.math.BigDecimal.valueOf(trip.driverEarnings))
        walletRepository.save(driverWallet)

        return saved
    }

    @Transactional(readOnly = true)
    fun getMyWallet(): Wallet {
        val phoneNumber = SecurityContextHolder.getContext().authentication?.name
            ?: throw IllegalStateException("Not authenticated")
        val user = userRepository.findByPhoneNumber(phoneNumber)
            .orElseThrow { IllegalArgumentException("User not found") }

        return walletRepository.findByUserId(user.id!!)
            .orElseGet {
                val wallet = Wallet(user = user)
                walletRepository.save(wallet)
            }
    }

    @Transactional(readOnly = true)
    fun getMyPayments(): List<Payment> {
        val phoneNumber = SecurityContextHolder.getContext().authentication?.name
            ?: throw IllegalStateException("Not authenticated")
        val user = userRepository.findByPhoneNumber(phoneNumber)
            .orElseThrow { IllegalArgumentException("User not found") }

        return paymentRepository.findByPayerId(user.id!!) + paymentRepository.findByPayeeId(user.id!!)
    }

    @Transactional
    fun initiateMtnMomoPayment(phoneNumber: String, amount: Double): Payment {
        val user = userRepository.findByPhoneNumber(phoneNumber)
            .orElseThrow { IllegalArgumentException("User not found") }

        val payment = Payment(
            payerId = user.id!!,
            payeeId = user.id,
            amount = amount,
            commissionAmount = 0.0,
            netAmount = amount,
            paymentMethod = com.ridenorth.module.payment.PaymentMethod.MTN_MOMO,
            status = com.ridenorth.module.payment.PaymentStatus.PROCESSING,
            createdAt = java.time.Instant.now()
        )
        return paymentRepository.save(payment)
    }
}

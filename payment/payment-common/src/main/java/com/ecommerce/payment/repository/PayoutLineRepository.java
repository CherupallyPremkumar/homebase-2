package com.ecommerce.payment.repository;

import com.ecommerce.payment.domain.PayoutLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PayoutLineRepository extends JpaRepository<PayoutLine, String> {

    List<PayoutLine> findByPayoutId(String payoutId);

    List<PayoutLine> findByInternalPaymentTransactionId(String internalPaymentTransactionId);

    long countByPayoutId(String payoutId);

    @Query("select count(pl) from PayoutLine pl where pl.payoutId = :payoutId and (pl.internalPaymentTransactionId is not null or pl.internalRefundId is not null)")
    long countMatchedByPayoutId(@Param("payoutId") String payoutId);
}

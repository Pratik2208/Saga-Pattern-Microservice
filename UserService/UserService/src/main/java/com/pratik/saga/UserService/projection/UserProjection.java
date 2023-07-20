package com.pratik.saga.UserService.projection;

import com.pratik.saga.CommonService.model.CardDetail;
import com.pratik.saga.CommonService.model.User;
import com.pratik.saga.CommonService.queries.GetUserPaymentDetailsQuery;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

@Component
public class UserProjection {

    @QueryHandler
    public User getUserPaymentDetails(GetUserPaymentDetailsQuery query){
        CardDetail cardDetail
                = CardDetail.
                builder()
                .name("Pratik Panchal")
                .validUntilYear(2024)
                .validUntilMonth(8)
                .cardNumber("123456789")
                .cvv(111)
                .build();

        return User.builder()
                .userId(query.getUserId())
                .firstName("Pratik")
                .lastName("Panchal")
                .cardDetail(cardDetail)
                .build();
    }
}

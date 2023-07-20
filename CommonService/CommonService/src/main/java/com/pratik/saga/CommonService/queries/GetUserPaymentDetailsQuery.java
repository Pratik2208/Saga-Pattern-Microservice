package com.pratik.saga.CommonService.queries;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetUserPaymentDetailsQuery {
    // Query for knowing which user id is associated with which card details
    // And this query should be handled by user service projection
    private String userId;
}

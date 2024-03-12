package com.hust.interviewmanagement.service;

import com.hust.interviewmanagement.entities.Offer;
import com.hust.interviewmanagement.web.request.OfferRequest;
import com.hust.interviewmanagement.web.request.SearchRequest;
import jakarta.mail.MessagingException;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;

public interface OfferService {
    Page<Offer> findAllOffer(SearchRequest searchRequest);
    void deleteOffer(Long id);
    List<Offer> findAllOfferByDate(LocalDate fromDate, LocalDate toDate);

    Offer saveOffer(OfferRequest offerRequest) throws MessagingException;

    Offer findOfferById(Long id);

    void approveOffer(Long id, String notes);
    void rejectOffer(Long id, String notes);
    Offer updateOffer(OfferRequest offerRequest);
}

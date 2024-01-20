package com.hust.interviewmanagement.repository;

import com.hust.interviewmanagement.entities.InterviewSchedule;
import com.hust.interviewmanagement.entities.Offer;
import com.hust.interviewmanagement.enums.EStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface OfferRepository extends JpaRepository<Offer, Long> {
    @Query("SELECT o FROM Offer o " +
            "JOIN o.department d " +
            "WHERE concat(o.id,'') like %?1% " +
            "AND d.name like %?2%")
    Page<Offer> findAll(String param, String department, Pageable pageable);

    @Query("SELECT o FROM Offer o " +
            "JOIN o.department d " +
            "WHERE concat(o.id,'') like %?1% " +
            "AND d.name like %?2% " +
            "AND o.status = ?3")
    Page<Offer> findAll(String param, String department, EStatus status, Pageable pageable);

    @Query("SELECT o FROM Offer o " +
            "WHERE o.createDate BETWEEN ?1 AND ?2")
    List<Offer> findOfferByDate(LocalDate fromDate, LocalDate toDate);


    @Modifying
    @Query("DELETE Offer where id in ?1")
    void deleteByOfferId(List<Long> ids);
}

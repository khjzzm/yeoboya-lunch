package com.yeoboya.guinGujik.api.v1._sample.repository;


import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface RetryRepository {


    @Transactional(readOnly = true) // slave db
    @Select("CALL c_radio.p_radio_mem_hphone_dupl_chk(#{memHphone})")
    int read(String memHpone);

}
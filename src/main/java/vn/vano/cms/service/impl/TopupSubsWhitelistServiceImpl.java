//package vn.vano.cms.service.impl;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import vn.vano.cms.jpa.TopupSubsWhitelist;
//import vn.vano.cms.repository.TopupSubsWhitelistRepo;
//import vn.vano.cms.service.TopupSubsWhitelistService;
//import vn.yotel.commons.bo.impl.GenericBoImpl;
//
//import java.util.List;
//
//@Service(value = "topupSubsWhitelistService")
//public class TopupSubsWhitelistServiceImpl extends GenericBoImpl<TopupSubsWhitelist, Long> implements TopupSubsWhitelistService {
//
//    @Autowired
//    TopupSubsWhitelistRepo subsWhitelistRepo;
//
//    @Override
//    public List<TopupSubsWhitelist> findByMsisdn(String msisdn) {
//        return getDAO().findByMsisdn(msisdn);
//    }
//
//    @Override
//    public TopupSubsWhitelistRepo getDAO() {
//        return subsWhitelistRepo;
//    }
//}

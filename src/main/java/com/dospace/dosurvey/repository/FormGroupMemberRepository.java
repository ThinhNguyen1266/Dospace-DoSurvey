package com.dospace.dosurvey.repository;

import com.dospace.dosurvey.entity.FormGroupMemberEntity;
import com.dospace.dosurvey.entity.enums.InvitationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FormGroupMemberRepository extends JpaRepository<FormGroupMemberEntity, String> {

    List<FormGroupMemberEntity> findByGroupIdAndStatus(String groupId, InvitationStatus status);

    List<FormGroupMemberEntity> findByGroupId(String groupId);

    Optional<FormGroupMemberEntity> findByGroupIdAndEmail(String groupId, String email);

    Optional<FormGroupMemberEntity> findByGroupIdAndAccountId(String groupId, String accountId);

    List<FormGroupMemberEntity> findByEmailAndStatus(String email, InvitationStatus status);

    List<FormGroupMemberEntity> findByAccountIdAndStatus(String accountId, InvitationStatus status);

    boolean existsByGroupIdAndEmail(String groupId, String email);

    @Query("SELECT m FROM FormGroupMemberEntity m " +
           "WHERE m.group.id = :groupId " +
           "AND (m.email = :email OR m.accountId = :accountId) " +
           "AND m.status = :status")
    Optional<FormGroupMemberEntity> findMemberByGroupAndEmailOrAccountId(
            @Param("groupId") String groupId,
            @Param("email") String email,
            @Param("accountId") String accountId,
            @Param("status") InvitationStatus status);

    @Query("SELECT CASE WHEN COUNT(m) > 0 THEN true ELSE false END FROM FormGroupMemberEntity m " +
           "WHERE m.group.id = :groupId " +
           "AND (m.email = :email OR m.accountId = :accountId) " +
           "AND m.status = 'ACCEPTED'")
    boolean isActiveMember(@Param("groupId") String groupId, @Param("email") String email, @Param("accountId") String accountId);
}

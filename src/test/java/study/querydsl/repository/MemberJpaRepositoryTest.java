package study.querydsl.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.querydsl.dto.MemberSearchCondition;
import study.querydsl.dto.MemberTeamDto;
import study.querydsl.entity.Member;
import study.querydsl.entity.Team;

import javax.persistence.EntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class MemberJpaRepositoryTest {

    @Autowired EntityManager em;
    @Autowired MemberJpaRepository memberJpaRepository;

    @Test
    public void basicTest() throws Exception {
        //given
        Member member = new Member("member1", 10);
        memberJpaRepository.save(member);

        //when
        Member findMember = memberJpaRepository.findById(member.getId()).get();
        List<Member> result1 = memberJpaRepository.findAll();
        List<Member> result2 = memberJpaRepository.findByUsername(member.getUsername());

        //then
        assertThat(findMember).isEqualTo(member);
        assertThat(result1).containsExactly(member);
        assertThat(result2).containsExactly(member);
    }

    @Test
public void basicQuerydslTest() throws Exception {
        //given
        Member member = new Member("member1", 10);
        memberJpaRepository.save(member);

        //when
        Member findMember = memberJpaRepository.findById(member.getId()).get();
        List<Member> result1 = memberJpaRepository.findAll_Querydsl();
        List<Member> result2 = memberJpaRepository.findByUsername_Querydsl(member.getUsername());

        //then
        assertThat(findMember).isEqualTo(member);
        assertThat(result1).containsExactly(member);
        assertThat(result2).containsExactly(member);
    }
    
    @Test
    public void searchByBuilderTest() throws Exception {
        //given
        Team teamA = new Team("teamA");
        em.persist(teamA);
        Team teamB = new Team("teamB");
        em.persist(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 20, teamA);
        em.persist(member1);
        em.persist(member2);

        Member member3 = new Member("member3", 30, teamB);
        Member member4 = new Member("member4", 40, teamB);
        em.persist(member3);
        em.persist(member4);

        //when
        MemberSearchCondition condition = new MemberSearchCondition();
//        condition.setAgeGoe(20);
//        condition.setAgeLoe(40);
        condition.setTeamName("teamB");

        List<MemberTeamDto> result = memberJpaRepository.searchByBuilder(condition);

        //then
        assertThat(result).extracting("username").containsExactly("member3", "member4");
    }

    @Test
    public void searchTest() throws Exception {
        //given
        Team teamA = new Team("teamA");
        em.persist(teamA);
        Team teamB = new Team("teamB");
        em.persist(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 20, teamA);
        em.persist(member1);
        em.persist(member2);

        Member member3 = new Member("member3", 30, teamB);
        Member member4 = new Member("member4", 40, teamB);
        em.persist(member3);
        em.persist(member4);

        //when
        MemberSearchCondition condition = new MemberSearchCondition();
//        condition.setAgeGoe(20);
//        condition.setAgeLoe(40);
        condition.setTeamName("teamB");

        List<MemberTeamDto> result = memberJpaRepository.search(condition);

        //then
        assertThat(result).extracting("username").containsExactly("member3", "member4");
    }
}
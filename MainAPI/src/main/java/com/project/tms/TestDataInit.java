package com.project.tms;

import com.project.tms.domain.tag.*;
import com.project.tms.dto.MemberDto;
import com.project.tms.service.LoginService;
import com.project.tms.tag.*;
import com.project.tms.service.MemberService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TestDataInit {

    private final MemberService memberService;
    private final LoginService loginService;

    private final CultureRepository cultureRepository;
    private final EconomyRepository economyRepository;
    private final EntertainRepository entertainRepository;
    private final PoliticsRepository politicsRepository;
    private final ScienceRepository scienceRepository;
    private final SocietyRepository societyRepository;
    private final SportsRepository sportsRepository;
    private final TechnologyRepository technologyRepository;
    private final WorldRepository worldRepository;

    @PostConstruct
    public void init() {
        createTestMember();
        createTestCulture();
        createTestEconomy();
        createTestEntertain();
        createTestPolitics();
        createTestScience();
        createTestSociety();
        createTestSports();
        createTestTechnology();
        createTestWorld();
    }

    private void createTestMember() {

        MemberDto memberDto = new MemberDto();
        memberDto.setLoginId("test");
        memberDto.setPassword("test!");
        memberDto.setMemberName("testUser");

        memberService.join(memberDto);
        loginService.login("test", "test!");
    }
    private void createTestCulture() {
        Culture culture = new Culture();
        // 필요한 Culture 엔티티의 속성 설정
        cultureRepository.save(culture);
    }
    private void createTestEconomy() {
        Economy economy = new Economy();
        // 필요한 Culture 엔티티의 속성 설정
        economyRepository.save(economy);
    }
    private void createTestEntertain() {
        Entertain entertain = new Entertain();
        // 필요한 Culture 엔티티의 속성 설정
        entertainRepository.save(entertain);
    }
    private void createTestPolitics() {
        Politics politics = new Politics();
        // 필요한 Culture 엔티티의 속성 설정
        politicsRepository.save(politics);
    }
    private void createTestScience() {
        Science science = new Science();
        // 필요한 Culture 엔티티의 속성 설정
        scienceRepository.save(science);
    }
    private void createTestSociety() {
        Society society = new Society();
        // 필요한 Culture 엔티티의 속성 설정
        societyRepository.save(society);
    }
    private void createTestSports() {
        Sports sports = new Sports();
        // 필요한 Culture 엔티티의 속성 설정
        sportsRepository.save(sports);
    }
    private void createTestTechnology() {
        Technology technology = new Technology();
        // 필요한 Culture 엔티티의 속성 설정
        technologyRepository.save(technology);
    }
    private void createTestWorld() {
        World world = new World();
        // 필요한 Culture 엔티티의 속성 설정
        worldRepository.save(world);
    }
}

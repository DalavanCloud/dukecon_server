package org.dukecon.server.service

import groovy.transform.TypeChecked
import groovy.util.logging.Slf4j
import org.dukecon.DukeConServerApplication
import org.dukecon.model.UserPreference
import org.springframework.boot.test.IntegrationTest
import org.springframework.boot.test.SpringApplicationContextLoader
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.web.WebAppConfiguration
import spock.lang.Specification

import javax.inject.Inject
import javax.ws.rs.core.Response
import java.security.Principal

/**
 * @author Gerd Aschemann, http://aschemann.net, @GerdAschemann
 */
@ContextConfiguration(loader = SpringApplicationContextLoader, classes = DukeConServerApplication)
@WebAppConfiguration
@IntegrationTest(["server.port=0"])
@Slf4j
@TypeChecked
class PreferencesServiceSpec extends Specification {
    @Inject
    PreferencesService preferencesService

    void "test get without authorization" () {
        SecurityContextHolder.getContext().authentication = null
        when:
            Response response = preferencesService.getPreferences()
            log.debug ("Response: {}", response)
        then:
            assert Response.Status.NOT_FOUND == response.getStatusInfo()
    }

    void "test get without principal" () {
        Authentication emptyAuthentication = [ getPrincipal : { null } ] as Authentication
        SecurityContextHolder.getContext().authentication = emptyAuthentication
        when:
            Response response = preferencesService.getPreferences()
            log.debug ("Response: {}", response)
        then:
            assert Response.Status.NOT_FOUND == response.getStatusInfo()
    }

    void "test get no preferences" () {
        Principal dummyPrincipal = [ toString : {"id has no talks"} ] as Principal
        Authentication dummyAuthentication = [ getPrincipal : { dummyPrincipal } ] as Authentication
        SecurityContextHolder.getContext().authentication = dummyAuthentication
        when:
            Response response = preferencesService.getPreferences()
            log.debug ("Response: {}", response)
        then:
            assert Response.Status.OK == response.getStatusInfo()
            List<UserPreference> result = response.entity as List<UserPreference>
            assert 3 == result.size()
    }

    void "test set simple preferences" () {
        Principal testerPrincipal = [ toString : {"GerdTheTester"} ] as Principal
        Authentication testerAuthentication = [ getPrincipal : { testerPrincipal } ] as Authentication
        SecurityContextHolder.getContext().authentication = testerAuthentication
        List<UserPreference> userPreferences = [
                UserPreference.builder().talkId("17").build(),
                UserPreference.builder().talkId("18").build(),
                UserPreference.builder().talkId("19").build(),
        ]
        when:
            Response responseSet = preferencesService.setPreferences(userPreferences)
            log.debug ("Response (set): {}", responseSet)
            Response responseGet = preferencesService.getPreferences()
            log.debug ("Response (get): {}", responseGet)
        then:
            assert Response.Status.CREATED == responseSet.getStatusInfo()
            assert Response.Status.OK == responseGet.getStatusInfo()
            List<UserPreference> result = (List<UserPreference>)responseGet.entity
            assert 3 == result.size()
    }

    void "test update preferences" () {
        Principal testerPrincipal = [ toString : {"FalkTheTester"} ] as Principal
        Authentication testerAuthentication = [ getPrincipal : { testerPrincipal } ] as Authentication
        SecurityContextHolder.getContext().authentication = testerAuthentication
        List<UserPreference> userPreferences = [
                UserPreference.builder().talkId("17").build(),
                UserPreference.builder().talkId("18").build(),
                UserPreference.builder().talkId("19").build(),
        ]
        List<UserPreference> updatedUserPreferences = [
                UserPreference.builder().talkId("17").version(1).build(),
                UserPreference.builder().talkId("18").build(),
                UserPreference.builder().talkId("19").build(),
        ]
        when:
        Response responseSet = preferencesService.setPreferences(userPreferences)
        log.debug ("Response (initial set): {}", responseSet)
        Response responseUpdate = preferencesService.setPreferences(updatedUserPreferences)
        log.debug ("Response (update): {}", responseUpdate)
        Response responseGet = preferencesService.getPreferences()
        log.debug ("Response (get): {}", responseGet)
        then:
        assert Response.Status.CREATED == responseSet.getStatusInfo()
        assert Response.Status.CREATED == responseUpdate.getStatusInfo()
        assert Response.Status.OK == responseGet.getStatusInfo()
        List<UserPreference> result = (List<UserPreference>)responseGet.entity
        assert 3 == result.size()
    }

    void "test delete preferences" () {
        Principal testerPrincipal = [ toString : {"NikoTheTester"} ] as Principal
        Authentication testerAuthentication = [ getPrincipal : { testerPrincipal } ] as Authentication
        SecurityContextHolder.getContext().authentication = testerAuthentication
        List<UserPreference> userPreferences = [
                UserPreference.builder().talkId("17").build(),
                UserPreference.builder().talkId("18").build(),
                UserPreference.builder().talkId("19").build(),
        ]
        List<UserPreference> updatedUserPreferences = [
                UserPreference.builder().talkId("17").version(1).build(),
                // 18 is deleted
                UserPreference.builder().talkId("19").build(),
        ]
        when:
        Response responseSet = preferencesService.setPreferences(userPreferences)
        log.debug ("Response (initial set): {}", responseSet)
        Response responseUpdate = preferencesService.setPreferences(updatedUserPreferences)
        log.debug ("Response (update): {}", responseUpdate)
        Response responseGet = preferencesService.getPreferences()
        log.debug ("Response (get): {}", responseGet)
        then:
        assert Response.Status.CREATED == responseSet.getStatusInfo()
        assert Response.Status.CREATED == responseUpdate.getStatusInfo()
        assert Response.Status.OK == responseGet.getStatusInfo()
        List<UserPreference> result = (List<UserPreference>)responseGet.entity
        assert 2 == result.size()
    }
}

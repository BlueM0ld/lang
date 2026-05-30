package com.study.mandarin.lang.drill

import com.study.mandarin.lang.drill.dto.DrillResultRequest
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification
import com.study.mandarin.lang.drill.fixtures.DrillFixtures

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@WebMvcTest(DrillController)
class DrillControllerTest extends Specification {

    @Autowired
    MockMvc mockMvc

    @SpringBean
    DrillService drillService = Mock()

    def "GET /drill returns drills"() {
        given:
        drillService.getDrill() >> [DrillFixtures.drillDto()]

        expect:
        mockMvc.perform(get("/drill"))
                .andExpect(status().isOk())
                .andExpect(jsonPath('$[0].vocabItemOptions').isArray())
    }

    def "POST /drill returns result"() {
        given:

        drillService.postDrillVerification(_ as DrillResultRequest) >> "vocab-id-123"

        expect:
        mockMvc.perform(post("/drill")
                .contentType("application/json")
                .content('{"vocabItemId":"vocab-123","qualityOfRecall":true}'))
                .andExpect(status().isOk())
                .andExpect(content().string("vocab-id-123"))
    }
}
package com.study.mandarin.lang.drill

import com.study.mandarin.lang.drill.dto.DrillResultRequest
import com.study.mandarin.lang.drill.dto.DrillType
import com.study.mandarin.lang.drill.rest.DrillController
import com.study.mandarin.lang.drill.service.DrillService
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification
import com.study.mandarin.lang.drill.fixtures.DrillFixtures
import spock.lang.Unroll

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@WebMvcTest(DrillController)
class DrillControllerTest extends Specification {

    @Autowired
    MockMvc mockMvc

    @SpringBean
    DrillService drillService = Mock()


    @Unroll
    def "GET /drill/#drillType returns drills"() {
        given:
        drillService.getDrill(drillType) >> [DrillFixtures.drillDto()]

        expect:
        mockMvc.perform(get("/api/drill/$drillType"))
                .andExpect(status().isOk())
                .andExpect(jsonPath('$[0].vocabItemOptions').isArray())

        where:
        drillType << DrillType.values()
    }

    def "POST /drill returns result"() {
        given:

        drillService.postDrillVerification(_ as DrillResultRequest) >> "vocab-id-123"

        expect:
        mockMvc.perform(post("/api/drill")
                .contentType("application/json")
                .content('{"vocabId":"vocab-123", "qualityOfRecall": "THREE"}'))
                .andExpect(status().isOk())
                .andExpect(content().string("vocab-id-123"))
    }
}
package com.study.mandarin.lang.vocab;

import com.study.mandarin.lang.drill.dto.DrillDto;
import com.study.mandarin.lang.drill.dto.DrillResultRequest;
import com.study.mandarin.lang.drill.dto.DrillService;
import com.study.mandarin.lang.vocab.dto.VocabItemDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class VocabController {
    private final VocabService vocabService;
    private final DrillService drillService;


    @GetMapping("/vocab")
    @ResponseBody
    public List<VocabItemDTO> getVocab() {
        return vocabService.getAllVocab();
    }

    @GetMapping("/drill")
    @ResponseBody
    public List<DrillDto> getDrill(){
        return drillService.getDrill();
    }

    @PostMapping("/drill")
    public ResponseEntity<String> postDrillVerification(@RequestBody DrillResultRequest req) {
        return drillService.postDrillVerification(req);
    }

}

package com.study.mandarin.lang.vocab;


import com.study.mandarin.lang.drill.DrillService;
import com.study.mandarin.lang.vocab.dto.AddVocab;
import com.study.mandarin.lang.vocab.dto.UpdateVocab;
import com.study.mandarin.lang.vocab.dto.VocabItemDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class VocabController {
    private final VocabService vocabService;

    @GetMapping("/vocab/")
    @ResponseBody
    public List<VocabItemDTO> getVocab(
            @RequestParam(required = false) String search,
            @RequestParam(required = false, defaultValue = "false") boolean dueOnly
    ) {
        return vocabService.getVocab(search,dueOnly);
    }

    @GetMapping("/vocab/all")
    @ResponseBody
    public List<VocabItemDTO> getAllVocab() {
        return vocabService.getAllVocab();
    }

    @PostMapping("/vocab")
    @ResponseBody
    public ResponseEntity<String> addVocab(@RequestBody AddVocab req){
        return vocabService.addVocab(req);
    }

    @PutMapping("/vocab/{id}")
    @ResponseBody
    public ResponseEntity<String> updateVocab(@RequestBody UpdateVocab req){
        return vocabService.updateVocab(req);
    }

    @DeleteMapping("/vocab/{id}")
    @ResponseBody
    public ResponseEntity<String> deleteVocab(@PathVariable String id){
        return vocabService.deleteVocab(id);
    }

}

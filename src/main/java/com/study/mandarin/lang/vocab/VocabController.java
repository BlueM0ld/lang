package com.study.mandarin.lang.vocab;


import com.study.mandarin.lang.vocab.dto.AddVocab;
import com.study.mandarin.lang.vocab.dto.UpdateVocab;
import com.study.mandarin.lang.vocab.dto.VocabItemDTO;
import com.study.mandarin.lang.vocab.service.VocabService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class VocabController {
    private final VocabService vocabService;

    @GetMapping("/vocab")
    @ResponseBody
    public List<VocabItemDTO> getVocab(
            @RequestParam(required = false) String search,
            @RequestParam(required = false, defaultValue = "false") boolean dueOnly
    ) {
        return vocabService.getVocab(search,dueOnly);
    }

    @GetMapping("/vocab/dictionary")
    @ResponseBody
    public List<VocabItemDTO> getAllVocab() {
        return vocabService.getAllVocab();
    }

    @PostMapping("/vocab")
    @ResponseBody
    public ResponseEntity<String> addVocab(@RequestBody AddVocab req){
        return ResponseEntity.ok(vocabService.addVocab(req));
    }

    @PutMapping("/vocab/{id}")
    @ResponseBody
    public ResponseEntity<String> updateVocab(@RequestBody UpdateVocab req){
        return ResponseEntity.ok(vocabService.updateVocab(req));
    }

    @DeleteMapping("/vocab/{id}")
    @ResponseBody
    public ResponseEntity<String> deleteVocab(@PathVariable String id){
        return ResponseEntity.ok(vocabService.deleteVocab(id));
    }

}

package com.study.mandarin.lang.drill;

import com.study.mandarin.lang.drill.dto.DrillDto;
import com.study.mandarin.lang.drill.dto.DrillResultRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class DrillController {
    private final DrillService drillService;


    @GetMapping("/drill")
    @ResponseBody
    public List<DrillDto> getDrill(){
        return drillService.getDrill();
    }

    @PostMapping("/drill")
    public ResponseEntity<String> postDrillVerification(@RequestBody DrillResultRequest req) {
        return ResponseEntity.ok(drillService.postDrillVerification(req));
    }

}

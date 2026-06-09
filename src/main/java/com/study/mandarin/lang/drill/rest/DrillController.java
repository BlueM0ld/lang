package com.study.mandarin.lang.drill.rest;

import com.study.mandarin.lang.drill.dto.DrillModalType;
import com.study.mandarin.lang.drill.service.DrillService;
import com.study.mandarin.lang.drill.dto.DrillType;
import com.study.mandarin.lang.drill.dto.DrillDto;
import com.study.mandarin.lang.drill.dto.DrillResultRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class DrillController {
    private final DrillService drillService;


    @GetMapping("/drill/{drillType}")
    @ResponseBody
    public List<DrillDto> getDrill(
            @PathVariable DrillType drillType
    ) {
        return drillService.getDrill(drillType);
    }

    @PostMapping("/drill")
    public ResponseEntity<String> postDrillVerification(@RequestBody DrillResultRequest req) {
        return ResponseEntity.ok(drillService.postDrillVerification(req));
    }

}
